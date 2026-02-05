import java.io.*;
import java.util.*;

/**
 * The MontanaCounties class provides functionality for loading, querying,
 * displaying, and updating Montana county data stored in a CSV file.
 *
 * The program allows users to look up counties by city name, view associated
 * county information and license plate prefixes, and optionally add new
 * city-to-county mappings to the data file.
 */
public class MontanaCounties {

  /**
   * Loads county data from a CSV file into a nested map.
   * The outer map is keyed by city name (lowercase).
   * The inner map stores county attributes such as:
   *  - "County": county name
   *  - "LP": license plate prefix
   *
   * @param filePath path to the input CSV file
   * @return a map containing county data indexed by city name
   */
  public static Map<String, Map<String, String>> loadCountyData(String filePath) {
    final Map<String, Map<String, String>> countyMap = new HashMap<>();

    // Read the CSV file line by line
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;

      while ((line = br.readLine()) != null) {
        final String[] values = line.split(",");

        // Skip header row
        if (values[0].equals("County")) {
          continue;
        }

        final Map<String, String> countyData = new HashMap<>();

        // Store county name and license plate prefix
        countyData.put("LP", values[2]);
        countyData.put("County", values[0]);

        // Use lowercase city name as key for consistent lookup
        countyMap.put(values[1].toLowerCase(), countyData);
      }
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      System.exit(1);
    }

    return countyMap;
  }

  /**
   * Capitalizes a string into title case.
   * Letters following spaces, hyphens, or ampersands are capitalized.
   * All other letters are converted to lowercase.<br>
   * Examples: <br>
   *  - "new mexico" -> "New Mexico" <br>
   *  - "silver-bow" -> "Silver-Bow"
   *
   * @param str input string
   * @return capitalized string, or the original string if null/blank
   */
  public static String capitalizeString(String str) {
    if (str == null || str.isBlank()) {
      return str;
    }

    final StringBuilder sb = new StringBuilder();

    for (int i = 0; i < str.length(); i++) {
      // Capitalize first character or characters following delimiters
      if (i == 0 || Set.of(' ', '-', '&').contains(str.charAt(i - 1))) {
        sb.append(str.substring(i, i + 1).toUpperCase());
      } else {
        sb.append(str.substring(i, i + 1).toLowerCase());
      }
    }

    return sb.toString().strip();
  }

  /**
   * Writes county data to a CSV output file.
   * The output includes a header row followed by county records.
   *
   * @param outputFilePath path to the output CSV file
   * @param countyMap map containing county data to write
   */
  public static void writeCountyData(String outputFilePath, String cityName,
                                     Map<String, String> countyData) {
    // Append data 
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath, true))) {

      // Write new city data
      bw.write(
        capitalizeString(countyData.get("County")) + "," +
            capitalizeString(cityName) + "," +
            countyData.get("LP") + "\n"
      );

    } catch (IOException e) {
      System.err.println("Error writing to file: " + e.getMessage());
      System.exit(1);
    }
  }

  /**
   * Exits the program if the user enters "q".
   *
   * @param str user input string
   */
  private static void checkExit(String str) {
    if (str.equalsIgnoreCase("q")) {
      System.exit(0);
    }
  }

  /**
   * Main program loop.
   * Loads county data, prompts the user for view options,
   * allows lookup by city name, and optionally updates
   * the underlying CSV file.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {

    final String filePath = "data/MontanaCounties.csv";

    // Load initial county data
    Map<String, Map<String, String>> countyMap = loadCountyData(filePath);

    // Track known counties for validation when adding new cities
    final Set<String> counties = new HashSet<>();
    for (String key : countyMap.keySet()) {
      counties.add(countyMap.get(key).get("County").strip().toLowerCase());
    }

    String viewMode = null;

    // Prompt user for preferred view mode
    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.println("When viewing county data,");
        System.out.print(
            "do you want to view license plate prefix ('l'), county ('c'), " +
                "or both ('b')? ('q' to quit) "
        );

        viewMode = scanner.nextLine().strip().toLowerCase();

        final Set<String> viewModes = Set.of("l", "c", "b", "q");

        if (!viewModes.contains(viewMode)) {
          System.out.println("Invalid view mode: " + viewMode);
        } else {
          break;
        }
      }

      checkExit(viewMode);

      // Main interaction loop
      while (true) {
        System.out.print("Enter city name ('q' to quit): ");

        final String cityName = scanner.nextLine().strip().toLowerCase();
        checkExit(cityName);

        if (cityName.isBlank()) {
          System.out.println("Please enter some text");
          continue;
        }

        // City exists in database
        if (countyMap.containsKey(cityName)) {
          final Map<String, String> countyData = countyMap.get(cityName);

          // Display selected data
            switch (viewMode) {
              case "l":
                System.out.println("\nInformation for " +
                    capitalizeString(cityName) + ": ");
                System.out.println("License Plate Prefix: " +
                    countyData.get("LP") + "\n");
                break;

              case "c":
                System.out.println("\nInformation for " +
                    capitalizeString(cityName) + ": ");
                System.out.println("County: " +
                    countyData.get("County") + "\n");
                break;

              case "b":
                System.out.println("\nInformation for " +
                    capitalizeString(cityName) + ": ");
                System.out.println("County: " +
                    countyData.get("County"));
                System.out.println("License Plate Prefix: " +
                    countyData.get("LP") + "\n");
                break;

              default:
                break;
          }
        } else {
          // City not found in database
          System.out.println("\nThe database don't have records for " +
              capitalizeString(cityName));

          addDb:
          while (true) {
            System.out.print("Would you like to add it to the database? (y/n) ");

            final String addToDB = scanner.nextLine().strip().toLowerCase();
            checkExit(addToDB);

            if (addToDB.equals("y")) {
              while (true) {
                System.out.println();
                System.out.println("Given county must be a valid Montana county, type 'l' to list all counties in Montana" );
                System.out.print("Enter county for " +
                    capitalizeString(cityName) + ": ");

                final String countyName =
                    scanner.nextLine().strip().toLowerCase();
                checkExit(countyName);

                if (countyName.equals("l")) {
                  for (String str: counties){
                    System.out.println(MontanaCounties.capitalizeString(str));
                  }
                } else if (counties.contains(countyName)) {
                  final Map<String, String> countyData = new HashMap<>();

                  // Find existing county data to copy license plate prefix
                  for (String key : countyMap.keySet()) {
                    if (countyMap.get(key).get("County")
                        .equalsIgnoreCase(countyName)) {
                      countyData.put("LP",
                          countyMap.get(key).get("LP"));
                      countyData.put("County",
                          countyMap.get(key).get("County"));
                      break;
                    }
                  }

                  // Persist updated data
                  writeCountyData(filePath, cityName, countyData);
                  countyMap = loadCountyData(filePath);

                  System.out.println("\nSuccessfully added " +
                      cityName + " to the database\n");

                  break addDb;
                } else {
                  System.out.println(
                      countyName +
                          " is not a county in Montana, consider checking your spelling\n"
                  );
                }
              }
            } else if (addToDB.equals("n")) {
              break;
            } else {
              System.out.println("Invalid option, please enter y or n.");
            }
          }
        }
      }
    }
  }
}
