# Montana Counties Lookup 2

This is a simple Java console program that reads county data from a CSV file and lets
the user look up a given cities county and license plate prefix. If the user-given city is not
in the database (File), the user has the option to add it. An important note is that
the county for the given city must be one of the existing counties in the dataset. The initial
dataset should have one entry from each of Montana's 56 counties.

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

From the project root directory (`MontanaCounties2`):
```bash
$ java src/Main.java
```

This should compile and run the program.

## How to Use

1. Choose what information to display:

   * `l` - license plate prefix
   * `c` - county name
   * `b` - both
2. Enter the name of a city
3. Information will be displayed
4. If the city isn't in the database, you have the option to add it
5. Enter 'q' at any time to quit the program.

## Example

```
$ java src/MontanaCounties.java
When viewing county data,
do you want to view license plate prefix ('l'), county ('c'), or both ('b')? ('q' to quit) b
Enter city name ('q' to quit): helena // User input

Information for Helena: 
County: Lewis & Clark
License Plate Prefix: 5

Enter city name ('q' to quit): SuperAwesomeCity // User input

The database don't have records for Superawesomecity
Would you like to add it to the database? (y/n) y // User input

Given country must be a valid Montana county, type 'l' to list all counties in Montana
Enter county for Superawesomecity: Lewis & clark // User input

Successfully added superawesomecity to the database

Enter city name ('q' to quit): q
```