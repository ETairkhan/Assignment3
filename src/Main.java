import controllers.UserController;
import data.PostgresDB;
import repositories.UserRepository;
import repositories.interfaces.IUserRepository;
import interfaces.IDB;

public class Main {
    public static void main(String[] args) {
        // Database connection details (ensure to replace with your actual credentials)
        String dbUrl = "jdbc:postgresql://localhost:5432/credit_card_system";
        String dbUser = "your_username";  // Replace with your actual database username
        String dbPassword = "your_password";  // Replace with your actual database password

        // Create an instance of PostgresDB to handle database connections
        IDB db = new PostgresDB(dbUrl, dbUser, dbPassword);

        // Creating the repository and controller instances
        IUserRepository repo = new UserRepository(db);
        UserController controller = new UserController(repo);

        try {
            // Adding a user with all required parameters: name, surname, age, gender, creditCard, balance, writeOffs, deposit
            System.out.println(controller.createUser("John", "Doe", 30, true, 123456789, 1000, 50, 200));

            // Retrieve and print all users
            System.out.println(controller.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();  // You can replace this with a logging framework in production
        } finally {
            // Close the database connection
            db.close();
        }
    }
}
