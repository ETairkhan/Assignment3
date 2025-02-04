import controllers.interfaces.IUserController;
import issue.IssuerCard;
import repositories.interfaces.IUserRepository;
import validate.CardInformation;
import validate.Validator;
import models.Role;
import models.AuthUser; // New class for authentication

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MyApplication {
    private final IUserController controller;
    private final Scanner scanner = new Scanner(System.in);
    private AuthUser currentUser = null; // Track logged-in user

    public MyApplication(IUserController controller) {
        this.controller = controller;
    }

    public void start() {
        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

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
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        String response = controller.loginUser(username, password);
        System.out.println(response);

        if (response.contains("Login successful")) {
            currentUser = controller.getLoggedInUser(username); // Fetch user details
            mainMenu(); // Start user operations menu
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

            System.out.print("Select an option (0-6): ");
            int option = scanner.nextInt();

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
        System.out.print("Please enter name: ");
        String name = scanner.next();

        System.out.println("Please enter surname: ");
        String surname = scanner.next();
        System.out.println("Please enter gender (male/female): ");

        String gender = scanner.next().toLowerCase();

        System.out.print("Please enter credit card number: ");
        String creditCardNumber = scanner.next();
        if (!Validator.validate(creditCardNumber)) {
            System.out.println("Invalid credit card number. User creation failed.");
            return;
        }

        System.out.println("Please enter balance: ");
        try {
            double balance = scanner.nextDouble();
            if (balance < 0){
                System.out.println("Invalid balance. User creation failed.");
                return;
            }
            String response = controller.createUser(name, surname, gender, creditCardNumber, balance);
            System.out.println(response);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input for balance. User creation failed.");
            scanner.nextLine();
        }


    }

    private void getUserByIdMenu() {
        System.out.print("Please enter a user ID: ");
        int id = scanner.nextInt();
        String response = controller.getUserById(id);
        System.out.println(response);
    }

    private void getAllUsersMenu() {
        String response = controller.getAllUsers();
        System.out.println(response);
    }

    private void deleteUser() {
        System.out.print("Please enter a user ID: ");
        int id = scanner.nextInt();
        String response = controller.deleteUser(id);
        System.out.println(response);
    }

    private void generateCardMenu() {

        System.out.println("Please enter the card brand (e.g., VISA, MASTERCARD): ");
        String brand = scanner.next().trim().toUpperCase(); // Normalize input
        System.out.println("Please enter the card issuer (e.g., Kaspi Gold, Forte Blue): ");
        scanner.nextLine(); // Consume newline
        String issuer = scanner.nextLine().trim(); // Normalize input


        Map<String, List<String>> brands = CardInformation.loadDataAsList("src/resources/brands.txt");
        Map<String, String> issuers = CardInformation.loadData("src/resources/issuers.txt");

        try {
            String cardNumber = IssuerCard.generateCardNumber(brands, issuers, brand, issuer);
            System.out.println("Generated Card Number: " + cardNumber);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    private void transferMoneyMenu() {
        System.out.print("Enter Sender ID: ");
        int senderId = scanner.nextInt();
        System.out.print("Enter Receiver ID: ");
        int receiverId = scanner.nextInt();
        System.out.print("Enter Amount to Transfer: ");
        double amount = scanner.nextDouble();

        String response = controller.transferMoney(senderId, receiverId, amount);
        System.out.println(response);
    }
}
