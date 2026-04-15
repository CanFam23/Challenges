## Math Contest Data Splitter

This is a Python script that reads a contest results CSV file and normalizes the data into two separate files:

* **Institutions.csv** - unique institutions with generated IDs
* **Teams.csv** - team data linked to institutions via Institution ID

The script removes redundancy by separating institution data from team data and linking them with a deterministic ID.



## Requirements

* Python 3.9+
* pip (Python package manager)



## Setup (Virtual Environment)

From the project root directory:

```bash
python -m venv venv
```
Or `python3` if `python` doesn't work.

Activate the virtual environment:

**Mac/Linux:**

```bash
source venv/bin/activate
```

**Windows:**

```bash
venv\Scripts\activate
```

Install required packages:

```bash
pip install pandas
```

## Input CSV Format

The input file (e.g., `2015.csv`) should contain columns similar to:

```
Institution,City,State/Province,Country,TeamNumber,Advisor,Problem,Ranking
```

* The script assumes these column names exist (after minor cleaning)
* The first row is treated as a header



## How to Run

From the project root directory:

```bash
python script.py input.csv
```

Specify an output directory:

```bash
python script.py input.csv -o output/
```



## Output

Two files will be generated:

### Institutions.csv

```
Institution ID,Institution Name,City,State/Province,Country
```

### Teams.csv

```
Team Number,Advisor,Problem,Ranking,Institution ID
```

* **Institution ID** is deterministically generated from:

  ```
  Institution | City | State/Province | Country
  ```
* This ensures consistent linking across both files



## How It Works

* Cleans column names to remove special characters
* Generates a unique ID for each institution using a deterministic UUID
* Removes duplicate institutions
* Splits team and institution data into separate tables
* Writes both datasets to CSV



## Example

```bash
python script.py 2015.csv -o output/
```

Output:

```
output/
├── Institutions.csv
└── Teams.csv
```



## Notes

* IDs are consistent across runs for the same input data
* Shortened IDs are used for readability
* Collisions are extremely unlikely for this dataset size
* This script doesn't clean the data at all. Consider that for a future improvement


