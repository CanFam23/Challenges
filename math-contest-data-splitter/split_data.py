"""
split_data

Reads a contest results CSV and normalizes it into two files:
- Institutions.csv (unique institutions with generated IDs)
- Teams.csv (team data linked by Institution ID)

Usage:
    python script.py input.csv -o output_dir/
"""

import os
import pandas as pd
import re
import uuid
import argparse

from rapidfuzz import fuzz, process

def make_id(s: str, length=10) -> str:
    """Makes a UUID from the given string after stripping it and
    converting to lowercase. The returned string is the given `length`.

    Args:
        s (str): String to make UUID from
        length (int, optional): How long the returned ID is. Defaults to 10.

    Returns:
        str: Created UUID
    """
    u = uuid.uuid5(uuid.NAMESPACE_DNS, s.strip().lower())
    return str(u).replace('-', '')[:length]

def normalize(name):
    name = name.lower()
    name = re.sub(r"[^\w\s]", "", name)  # remove punctuation
    
    name = re.sub(r"\s+", " ", name).strip()
    return name

def split_file(file_path: str, output_dir: str) -> None:
    """
    Reads a contest results CSV and splits it into two normalized CSV files:
    one containing unique institutions and one containing teams linked to
    those institutions via a generated Institution ID.

    The Institution ID is deterministically generated from institution name,
    city, state/province, and country to ensure consistent mapping.

    Args:
        file_path (str): Path to the input CSV file containing contest results.
        output_dir (str): Directory where the output CSV files
                          (Institutions.csv and Teams.csv) will be saved.
    """
    if not os.path.exists(file_path):
        print(f"{file_path} not found!")
        return

    os.makedirs(output_dir, exist_ok=True)

    data = pd.read_csv(file_path)

    # Clean column names
    data.columns = [re.sub(r'[^A-Za-z0-9/_-]', '', c) for c in data.columns]

    # Placeholder for ID
    data["Institution ID"] = None

    # Normalized institution name
    data['norm_inst'] = data['Institution'].apply(normalize)

    # Get all unique normalized institution names
    institution_names = sorted(set(data["norm_inst"].dropna().tolist()))

    results = {}

    seen_names = set()

    # Fuzzy match name with other institution names who have a similarity >= 85
    for name in institution_names:
        if name in seen_names:
            continue
        matches = process.extract(
            query=name, # type: ignore
            choices=institution_names, # type: ignore
            scorer=fuzz.token_sort_ratio,
            limit=5
        ) # type: ignore
        results[name] = [m[0] for m in matches if m[1] >= 85]

        seen_names.add(name)

        for n in results[name]:
            seen_names.add(n)

    # Make id for each name and assign it to all similar names too
    for key in results.keys():
        id = make_id(key)

        data.loc[data["norm_inst"].isin(results[key]),"Institution ID"] = id

    # Assert no missing IDs
    missing = data[data["Institution ID"].isna()]
    assert missing.empty, (
        f'Missing IDs for: {", ".join(missing["norm_inst"].astype(str).tolist())}'
    )

    # Save institution data after dropping duplicate ids and sorting by id
    institution_data = (
        data[["Institution ID","Institution","City","State/Province","Country"]]
        .drop_duplicates(subset=["Institution ID"])
        .reset_index(drop=True)
        .sort_values(by="Institution ID")
    )
    
    team_data = (
        data[["TeamNumber","Advisor","Problem","Ranking","Institution ID"]]
        .drop_duplicates()
        .reset_index(drop=True)
        .sort_values(by=["TeamNumber","Institution ID"])
    )

    institution_path = os.path.join(output_dir, "Institutions.csv")
    team_path = os.path.join(output_dir, "Teams.csv")

    institution_data.to_csv(institution_path, index=False)
    team_data.to_csv(team_path, index=False)
    print(f"{file_path} successfully split!")
    print(f"Institution data saved to '{institution_path}'")
    print(f"Team data saved to '{team_path}'")


if __name__ == "__main__":
    # parser = argparse.ArgumentParser(description="Split contest data into normalized CSVs")
    
    # parser.add_argument(
    #     "input_file",
    #     help="Path to input CSV file (e.g., 2015.csv)"
    # )
    
    # parser.add_argument(
    #     "-o", "--output_dir",
    #     default=".",
    #     help="Directory to save output CSV files (default: current directory)"
    # )

    # args = parser.parse_args()

    # split_file(args.input_file, args.output_dir)
    split_file("2015.csv",".")