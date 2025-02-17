package issue;

import validate.Validator;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class IssuerCard {
    public static String generateCardNumber(Map<String, List<String>> brands, Map<String, String> issuers, String brand, String issuer) {
        // Trim input
        brand = brand.trim();
        issuer = issuer.trim();


        List<String> brandPrefixes = brands.get(brand);
        if (brandPrefixes == null) {
            throw new IllegalArgumentException("Error: Brand not found in the provided data.");
        }


        String issuerPrefix = "";
        String issuerLower = issuer.toLowerCase();
        for (Map.Entry<String, String> entry : issuers.entrySet()) {
            if (entry.getValue().toLowerCase().equals(issuerLower)) {
                issuerPrefix = entry.getKey();
                break;
            }
        }



        if (issuerPrefix.isEmpty()) {
            System.err.println("Debug: Issuer '" + issuer + "' not found in issuers data.");
            throw new IllegalArgumentException("Error: Issuer not found in the provided data.");
        }



        boolean validPrefix = false;
        for (String brandPrefix : brandPrefixes) {
            if (issuerPrefix.startsWith(brandPrefix)) {
                validPrefix = true;
                break;
            }
        }

        if (!validPrefix) {
            System.err.println("Debug: No valid prefix in " + brandPrefixes + " matches issuer prefix " + issuerPrefix);
            throw new IllegalArgumentException(
                    String.format("Error: Issuer prefix '%s' does not match any prefix for brand '%s'.", issuerPrefix, brand)
            );
        }



        Random random = new Random();
        int maxAttempts = 10000;

        for (int attempts = 0; attempts < maxAttempts; attempts++) {
            StringBuilder number = new StringBuilder(issuerPrefix);
            while (number.length() < 15) {
                number.append(random.nextInt(10));
            }

            if (Validator.isValidLuhn(number.toString())) {
                return number.toString();
            }
        }

        throw new RuntimeException("Error: Failed to generate a valid Luhn number after maximum attempts.");
    }
}
