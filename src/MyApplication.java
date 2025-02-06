import controllers.interfaces.IUserController;
import issue.IssuerCard;
import repositories.interfaces.IUserRepository;
import validate.CardInformation;
import validate.Validator;
import models.Role;
import models.AuthUser;

import java.io.Console;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;

public class MyApplication {
    private final IUserController controller;
    private final Scanner scanner = new Scanner(System.in);
    private AuthUser currentUser = null;

    public MyApplication(IUserController controller) {
        this.controller = controller;
    }

    public void start() {
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int option = getValidIntegerInput();


            switch (option) {
                case 1:
                    registerMenu();
                    break;
                case 2:
                    loginMenu();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void registerMenu() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        System.out.print("Enter role (admin/user): ");
        String role = scanner.next().toLowerCase();

        String response = controller.registerUser(username, password, role);
        System.out.println(response);
    }

    private void loginMenu() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        String response = controller.loginUser(username, password);
        System.out.println(response);

        if (response.contains("Login successful")) {
            currentUser = controller.getLoggedInUser(username); // Fetch user details
            mainMenu();
        }
    }

    private void mainMenu() {
        while (true) {
            System.out.println();
            System.out.println("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole().getName() + ")");
            System.out.println("Select one of the following options:");

            // Check the role and display options accordingly
            if (currentUser.getRole().getName().equalsIgnoreCase("admin")) {
                // Admin can do everything
                System.out.println("1. Get all users");
                System.out.println("2. Get user by ID");
                System.out.println("3. Create new user");
                System.out.println("4. Delete user");
                System.out.println("5. Generate card number");
                System.out.println("6. Transfer Money");
                System.out.println("7. Get Users with Auth Details");
                System.out.println("0. Logout");
            } else if (currentUser.getRole().getName().equalsIgnoreCase("user")) {
                // Regular user has limited access
                System.out.println("1. Get all users");
                System.out.println("2. Get user by ID");
                System.out.println("3. Transfer Money");
                System.out.println("0. Logout");
            } else {
                System.out.println("Invalid role detected. Logging out...");
                currentUser = null;
                return;
            }

            int option = getValidIntegerInput("Select an option: ");

            if (currentUser.getRole().getName().equalsIgnoreCase("admin")) {
                // Admin has access to all options
                switch (option) {
                    case 1 -> getAllUsersMenu();
                    case 2 -> getUserByIdMenu();
                    case 3 -> createUserMenu();
                    case 4 -> deleteUser();
                    case 5 -> generateCardMenu();
                    case 6 -> transferMoneyMenu();
                    case 7 -> getUsersWithAuthDetailsMenu();
                    case 0 -> {
                        System.out.println("Logging out...");
                        currentUser = null;
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            } else if (currentUser.getRole().getName().equalsIgnoreCase("user")) {
                // Regular user can only access limited options
                switch (option) {
                    case 1 -> getAllUsersMenu();
                    case 2 -> getUserByIdMenu();
                    case 3 -> transferMoneyMenu();
                    case 0 -> {
                        System.out.println("Logging out...");
                        currentUser = null;
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }
    }


    private void createUserMenu() {

        Predicate<String> nameValidator = input -> input.matches("[a-zA-Z]+");
        Predicate<String> genderValidator = input -> input.equalsIgnoreCase("male") || input.equalsIgnoreCase("female");

        System.out.println("Please enter name: ");
        String name = scanner.nextLine().trim();
        if (!nameValidator.test(name)) {
            System.out.println("Invalid name. User creation failed.");
            return;
        }

        System.out.println("Please enter surname: ");
        String surname = scanner.nextLine().trim();
        if (!nameValidator.test(surname)) {
            System.out.println("Invalid surname. User creation failed.");
            return;
        }

        System.out.println("Please enter gender (male/female): ");
        String gender = scanner.nextLine().trim().toLowerCase();
        if (!genderValidator.test(gender)){
            System.out.println("Invalid gender. Please enter 'male' or 'female'.");
            return;
        }

        System.out.println("Please enter credit card number: ");
        String creditCardNumber = scanner.nextLine().trim();
        if (!Validator.validate(creditCardNumber)) {
            System.out.println("Invalid credit card number. User creation failed.");
            return;
        }

        System.out.println("Please enter balance: ");
        double balance = 0;
        try {
            balance = Double.parseDouble(scanner.nextLine().trim());
            if (balance < 0){
                System.out.println("Invalid balance. User creation failed.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for balance. User creation failed.");
        }

        String response = controller.createUser(name, surname, gender, creditCardNumber, balance);
        System.out.println(response);
    }


    private void getUserByIdMenu() {
        int id = getValidIntegerInput("Please enter a valid user ID: ");
        String response = controller.getUserById(id);
        System.out.println(response);
    }

    private void getAllUsersMenu() {
        String response = controller.getAllUsers();
        System.out.println(response);
    }

    private void deleteUser() {
        int id = getValidIntegerInput("Please enter a user ID: ");
        String response = controller.deleteUser(id);
        System.out.println(response);
    }

    private void generateCardMenu() {

        System.out.println("Please enter the card brand (e.g., VISA, MASTERCARD): ");
        String brand = scanner.next().trim().toUpperCase();
        System.out.println("Please enter the card issuer (e.g., Kaspi Gold, Forte Blue): ");
        scanner.nextLine();
        String issuer = scanner.nextLine().trim();


        Map<String, List<String>> brands = CardInformation.loadDataAsList("src/resources/brands.txt");
        Map<String, String> issuers = CardInformation.loadData("src/resources/issuers.txt");

        try {
            String cardNumber = IssuerCard.generateCardNumber(brands, issuers, brand, issuer);
            System.out.println("Generated Card Number: " + cardNumber);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    private int getValidIntegerInput() {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }


    private int getValidIntegerInput(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }


    private double getValidDoubleInput(String message) {
        System.out.print(message);
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }

    private void transferMoneyMenu() {
        int senderId = getValidIntegerInput("Enter Sender ID:");
        int receiverId = getValidIntegerInput("Enter Receiver ID:");
        double amount = getValidDoubleInput("Enter Amount to Transfer:");

        String response = controller.transferMoney(senderId, receiverId, amount);
        System.out.println(response);
    }

    private void getUsersWithAuthDetailsMenu() {
        String response = controller.getUsersWithAuthDetails();
        System.out.println(response);
    }

}
