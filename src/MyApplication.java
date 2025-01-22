import controllers.interfaces.IUserController;
import repositories.interfaces.IUserRepository;
import validate.Validator;

import java.util.InputMismatchException;
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
        System.out.println("0. Exit");
        System.out.println();
        System.out.print("Select an option (1-4): ");
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
        String gender = scanner.next();
        System.out.println("Please enter credit card number: ");
        String creditCardNumber = scanner.next();
        if (!Validator.validate(creditCardNumber)) {
            System.out.println("Invalid credit card number. User creation failed.");
            return; // Exit if validation fails
        }
        System.out.println("Please enter balance: "); // Prompt for balance
        double balance = scanner.nextDouble();




        String response = controller.createUser(name, surname, gender, creditCardNumber, balance);
        System.out.println(response);


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
}
