# Montana Counties Lookup

This is a simple Java console program that reads county data from a CSV file and lets the user look up Montana counties by number. The user can choose to view the county name, county seat, or both.

## Requirements

* Java 21 or newer
* A terminal / command line
* `MontanaCounties.csv` file (Included)

## CSV Format

The CSV file should be formatted like this:

```
County,County Seat,License Plate Prefix
Anaconda-Deer Lodge,Anaconda,30
Beaverhead,Dillon,18
...
```

The first row is treated as a header and skipped.

## How to Run

From the project root directory (`MontanaCounties`):
```bash
$ java src/Main.java
```

This should compile and run the program.

## How to Use

1. Enter a county number (1–56)
2. Choose what information to display:

    * `n` - county name
    * `s` - county seat
    * `b` - both
    * `q` - go back
3. Enter `q` at the county prompt to quit the program

## Example

```
Enter your county number ('q' to quit): 1
Do you want to view county name ('n'), seat ('s'), both ('b') or go back ('q')? b

Information for county number 1:
County: Beaverhead
County Seat: Dillon
```