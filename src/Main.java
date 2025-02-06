import controllers.UserController;
import controllers.interfaces.IUserController;
import data.PostgresDB;
import data.interfaceces.IDB;
import repositories.UserRepository;
import repositories.interfaces.IUserRepository;

public class Main {
    public static void main(String[] args) {
        // Use Singleton to get the database instance
        IDB db = PostgresDB.getInstance("jdbc:postgresql://localhost:5432", "postgres", "0000", "simpledb");

        // Initialize repository and controller
        IUserRepository repo = new UserRepository(db);
        IUserController controller = new UserController(repo);

        // Start the application
        MyApplication app = new MyApplication(controller);
        app.start();

        // Close the database connection at the end
        db.close();
    }
}
