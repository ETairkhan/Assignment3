import controllers.UserController;
import controllers.interfaces.IUserController;
import data.PostgresDB;
import data.interfaceces.IDB;
import repositories.UserRepository;
import repositories.interfaces.IUserRepository;

public class Main {
    public static void main(String[] args) {

        // We have to remake this one and others to our Bank project 'ZEBT'
        IDB db = new PostgresDB("jdbc:postgresql://localhost:5432", "Tair", "0000", "Tair2");
        IUserRepository repo = new UserRepository(db);
        IUserController controller = new UserController(repo);
        MyApplication app = new MyApplication(controller);
        app.start();

        db.close();
    }
}
//hello