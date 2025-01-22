import controllers.UserController;
import repositories.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
<<<<<<< HEAD
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
=======
        IDB db = new PostgresDB("jdbc:postgresql://localhost:5432", "Tair", "0000", "Tair2");
        IUserRepository repo = new UserRepository(db);
        IUserController controller = new UserController(repo);
        MyApplication app = new MyApplication(controller);
        app.start();

        db.close();
>>>>>>> 2de7b6fd8cd3867d99894cb29d1f28e1e3c44a93
    }
}
<<<<<<< HEAD
=======

>>>>>>> d171716715675c47f30aca96e668f8e7467a9c21
