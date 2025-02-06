import controllers.interfaces.IUserController;
import issue.IssuerCard;
import repositories.interfaces.IUserRepository;
import validate.CardInformation;
import validate.Validator;
import models.Role;
import models.AuthUser;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
            System.out.println("1. Get all users");
            System.out.println("2. Get user by ID");
            System.out.println("3. Create new user");
            System.out.println("4. Delete user");
            System.out.println("5. Generate card number");
            System.out.println("6. Transfer Money");
            System.out.println("0. Logout");


            int option = getValidIntegerInput("Select an option (0-6): ");

            switch (option) {
                case 1 -> getAllUsersMenu();
                case 2 -> getUserByIdMenu();
                case 3 -> createUserMenu();
                case 4 -> deleteUser();
                case 5 -> generateCardMenu();
                case 6 -> transferMoneyMenu();
                case 0 -> {
                    System.out.println("Logging out...");
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void createUserMenu() {
        System.out.println("Please enter name: ");
        String name = scanner.next();
        if (!name.matches("[a-zA-Z]+")) {
            System.out.println("Invalid name.User creation failed.");
            return;
        }
        System.out.println("Please enter surname: ");
        String surname = scanner.next();
        if (!surname.matches("[a-zA-Z]+")) {
            System.out.println("Invalid surname.User creation failed.");
            return;
        }
        System.out.println("Please enter gender (male/female): ");
        String gender = scanner.next().toLowerCase();
        if (!gender.equals("male") && !gender.equals("female")){
            System.out.println("Invalid gender. Please enter 'male' or 'female'");
            return;
        }
        System.out.println("Please enter credit card number: ");
        String creditCardNumber = scanner.next();
        if (!Validator.validate(creditCardNumber)) {
            System.out.println("Invalid credit card number. User creation failed.");
            return;
        }
        System.out.println("Please enter balance: ");
        double balance = 0;
        try {
            balance = scanner.nextDouble();
            if (balance < 0){
                System.out.println("Invalid balance. User creation failed.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for balance. User creation failed.");
            scanner.nextLine();
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
}
