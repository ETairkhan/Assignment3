package validate;

import java.io.*;
import java.util.*;
import validate.Validator;
public class CardInformation {

    public static Map<String, String> loadData(String filename) {
        Map<String, String> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    data.put(parts[1].toLowerCase(), parts[0]);
                }
            }
        } catch (IOException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.exit(1);
        }

        return data;
    }

    /**
     * Validate card information and print results for each card.
     */
    public static void cardInformation(Map<String, String> brands, Map<String, String> issuers, List<String> cards) {
        for (String card : cards) {
            boolean valid = Validator.isValidLuhn(card);
            System.out.println(card);

            if (valid) {
                System.out.println("Correct: yes");
            } else {
                System.out.println("Correct: no");
                System.out.println("Card Brand: -");
                System.out.println("Card Issuer: -");
                System.exit(1);
                return;
            }


            String brandResult = "-";
            for (Map.Entry<String, String> entry : brands.entrySet()) {
                if (card.startsWith(entry.getKey())) {
                    brandResult = entry.getValue();
                    break;
                }
            }
            System.out.println("Card Brand: " + brandResult);


            String issuerResult = "-";
            for (Map.Entry<String, String> entry : issuers.entrySet()) {
                if (card.startsWith(entry.getKey())) {
                    issuerResult = entry.getValue();
                    break;
                }
            }
            System.out.println("Card Issuer: " + issuerResult);
            System.out.println();
        }
    }

    public static Map<String, List<String>> loadDataAsList(String filename) {
        Map<String, List<String>> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String[] prefixes = parts[1].split(",");
                    data.put(parts[0], Arrays.asList(prefixes));
                }
            }
        } catch (IOException e) {
            System.out.printf("Error: %s%n", e.getMessage());
            System.exit(1);
        }
        return data;
    }
}
