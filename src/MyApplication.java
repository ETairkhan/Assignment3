import controllers.interfaces.IUserController;
import issue.IssuerCard;
import repositories.interfaces.IUserRepository;
import validate.CardInformation;
import validate.Validator;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MyApplication {
    private final IUserController controller;
    private final Scanner scanner = new Scanner(System.in);


    public MyApplication(IUserController controller) {
        this.controller = controller;
    }

    private void mainMenu(){
        System.out.println();
        System.out.println("Welcome to My Application");
        System.out.println("Select one of the following options:");
        System.out.println("1. Get all users");
        System.out.println("2. Get user by id");
        System.out.println("3. Create new user");
        System.out.println("4. Delete user");
        System.out.println("5. Generate card number");
        System.out.println("6. Transfer Money");
        System.out.println("0. Exit");
        System.out.println();
        System.out.print("Select an option (1-6): ");
    }

    public void start(){
        while(true){
            mainMenu();
            try{
                int option = scanner.nextInt();
                switch(option){
                    case 1: getAllUsersMenu(); break;
                    case 2: getUserByIdMenu(); break;
                    case 3: createUserMenu(); break;
                    case 4: deleteUser(); break;
                    case 5: generateCardMenu(); break;
                    case 6: transferMoneyMenu(); break;
                    default:return;
                }
            }catch (InputMismatchException e){
                System.out.println("Please enter a valid option!" + e);
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("----------------------------------------");
        }
    }

    private void createUserMenu() {
        System.out.println("Please enter name: ");
        String name = scanner.next();
        System.out.println("Please enter surname: ");
        String surname = scanner.next();
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
        System.out.println("Please enter a user id: ");
        int id = scanner.nextInt();

        String response = controller.getUserById(id);
        System.out.println(response);
    }

    private void getAllUsersMenu() {
        String response = controller.getAllUsers();
        System.out.println(response);
    }
    private void deleteUser() {
        System.out.println("Please enter a user id: ");
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

        // Load brands and issuers
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
        try {
            System.out.println("Enter Sender ID:");
            int senderId = scanner.nextInt();
            System.out.println("Enter Receiver ID:");
            int receiverId = scanner.nextInt();
            System.out.println("Enter Amount to Transfer:");
            double amount = scanner.nextDouble();

            String response = controller.transferMoney(senderId, receiverId, amount);
            System.out.println(response);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear input buffer
        }
    }


}
