import controllers.UserController;
import controllers.interfaces.IUserController;
import data.PostgresDB;
import data.interfaceces.IDB;
import repositories.UserRepository;
import repositories.interfaces.IUserRepository;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        IDB db = new PostgresDB("jdbc:postgresql://localhost:5432", "Tair", "0000", "Tair2");
        IUserRepository repo = new UserRepository(db);
        IUserController controller = new UserController(repo);
        MyApplication app = new MyApplication(controller);
        app.start();
//add
        db.close();

    }
}