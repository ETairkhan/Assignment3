package validate;

import java.util.Map;

public class Validator {

    public static boolean validate(String number) {
        if (number == null || number.isEmpty()) {
            System.out.println("INCORRECT");
            return false;
        }

        number = number.replaceAll("\\s", ""); // Remove spaces
        if (number.length() < 13 || number.length() > 19) {
            System.out.println("INCORRECT");
            return false;
        } else if (isValidLuhn(number)) {
            System.out.println("OK");
            return true;
        } else {
            System.out.println("INCORRECT");
            return false;
        }
    }


    // Luhn algorithm for credit card validation
    public static boolean isValidLuhn(String card) {
        if (card == null || card.isEmpty() || !card.matches("\\d+")) {
            return false;
        }
        int sum = 0;
        boolean doubleDigit = false;

        for (int i = card.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(card.charAt(i));
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return sum % 10 == 0;
    }

    // Validate data in a map structure
    public static void validateData(Map<String, String> data, String fileType) {
        if (data == null || data.isEmpty()) {
            System.out.printf("Error: %s file is empty or null.%n", fileType);
            return;
        }
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
                System.out.printf("Error: Invalid entry in %s file: '%s:%s'%n", fileType, key, value);
            }
        }
    }
}
