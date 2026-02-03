import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static Map<Integer, Map<String, String>> loadCountyData(String filePath){
        final Map<Integer, Map<String, String>> countyMap = new HashMap<>();

        // Load county data in
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] values = line.split(",");

                // Skip first line
                if (values[0].equals("County")) {
                    continue;
                }

                final Map<String, String> countyData = new HashMap<>();

                // Map for county name and seat
                countyData.put("County", values[0]);
                countyData.put("County Seat", values[1]);

                countyMap.put(Integer.parseInt(values[2]), countyData);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return countyMap;
    }


    public static void main(String[] args) {
        final String filePath = "data/MontanaCounties.csv"; // Path to your CSV file

        final Map<Integer, Map<String, String>> countyMap = Main.loadCountyData(filePath);

        // User input
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter your county number ('q' to quit): ");

                final String userInput = scanner.nextLine().strip().toLowerCase();

                // Check for blank input or exit letter
                if (userInput.isBlank()) {
                    System.out.println("Please enter some text");
                    continue;
                } else if (userInput.equals("q")) {
                    break;
                }

                // Try to parse county number and get data for it.
                try {
                    final int countyNum = Integer.parseInt(userInput);

                    // Check county number exists
                    if (countyMap.containsKey(countyNum)) {
                        final Map<String, String> countyData = countyMap.get(countyNum);

                        // Ask user for what data to display
                        viewMode:
                        while (true) {
                            System.out.print("Do you want to view county name ('n'), seat ('s'), both ('b') or go back ('q')? ");

                            final String viewMode = scanner.nextLine().strip().toLowerCase();

                            switch (viewMode) {
                                case "n":
                                    System.out.println();
                                    System.out.println("Information for county number " + countyNum + ": ");
                                    System.out.println("County: " + countyData.get("County"));
                                    System.out.println();
                                    break viewMode;
                                case "s":
                                    System.out.println();
                                    System.out.println("Information for county number " + countyNum + ": ");
                                    System.out.println("County Seat: " + countyData.get("County Seat"));
                                    System.out.println();
                                    break viewMode;
                                case "b":
                                    System.out.println();
                                    System.out.println("Information for county number " + countyNum + ": ");
                                    System.out.println("County: " + countyData.get("County"));
                                    System.out.println("County Seat: " + countyData.get("County Seat"));
                                    System.out.println();
                                    break viewMode;
                                case "q":
                                    break viewMode;
                                default:
                                    System.out.println("Invalid view mode, please enter 'n' for county name, 's' for seat, 'b' for both, or 'q' to go back.");
                                    break;
                            }
                        }
                    } else {
                        System.err.println("Invalid county code, please enter a number from 1-56");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid input, please enter a number from 1-56.");
                    System.out.println();
                }
            }
        }
    }
}