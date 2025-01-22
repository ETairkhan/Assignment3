import controllers.UserController;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        String dbUrl = "jdbc:postgresql://localhost:5432/credit_card_system";
        String dbUser = "your_username";
        String dbPassword = "your_password";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            UserRepository userRepository = new UserRepository(connection);
            UserController userController = new UserController(userRepository);

            // Example usage
            System.out.println(userController.addUser("John", "Doe", "1234567812345678", 1000));
            System.out.println(userController.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

