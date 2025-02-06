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

        IUserRepository repo = new UserRepository(db);
        IUserController controller = new UserController(repo);

        MyApplication app = new MyApplication(controller);
        app.start();

        db.close();
    }
}
