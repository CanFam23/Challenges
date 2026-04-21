## Math Contest Data Splitter

`split_data.py` reads a contest results CSV and normalizes it into:

- `Institutions.csv`: unique institutions with generated institution IDs
- `Teams.csv`: team rows linked back to institutions by `Institution ID`

## Recent `split_data` Updates

- Added fuzzy institution matching with `rapidfuzz` to group similar institution names.
- Added `--stats` flag to print summary stats after splitting.
- Institution IDs are now generated deterministically from normalized institution-name groups.
- Output rows are sorted and de-duplicated before writing CSV files.

## Requirements

- Python 3.9+
- `pip`

Install dependencies:

```bash
pip install pandas rapidfuzz tabulate
```

`tabulate` is needed for `--stats` table output (`DataFrame.to_markdown`).

## Input CSV Format

Expected columns:

```text
Institution,City,State/Province,Country,TeamNumber,Advisor,Problem,Ranking
```

Notes:

- The first row is treated as the header.
- Column names are lightly cleaned (`[^A-Za-z0-9/_-]` removed), but required fields still need to be present.

## Usage

Basic split:

```bash
python split_data.py input.csv
```

Write to an output directory:

```bash
python split_data.py input.csv -o output/
```

Include summary stats:

```bash
python split_data.py input.csv -o output/ --stats
```

## Output Files

`Institutions.csv` columns:

```text
Institution ID,Institution,City,State/Province,Country
```

`Teams.csv` columns:

```text
TeamNumber,Advisor,Problem,Ranking,Institution ID
```

## How It Works

- Reads the CSV into a pandas DataFrame.
- Normalizes institution names (lowercase, punctuation stripped, whitespace collapsed).
- Uses fuzzy matching (`token_sort_ratio`, threshold `>= 85`) to cluster similar institution names.
- Assigns a deterministic short UUID-derived institution ID to each cluster.
- Asserts all rows receive an institution ID.
- Writes de-duplicated, sorted `Institutions.csv` and `Teams.csv`.

## Example

```bash
python split_data.py 2015.csv -o output/ --stats
```

Expected output files:

```text
output/
├── Institutions.csv
└── Teams.csv
```

## Notes

- IDs are deterministic across runs for the same normalized institution-name grouping.
- IDs are short (first 10 chars of UUID5 hash with hyphens removed).
- Fuzzy grouping helps reduce duplicates caused by naming variation, but thresholds may need tuning for different datasets.

