import controllers.UserController;
import data.PostgresDB;
import repositories.UserRepository;
import controllers.interfaces.IUserRepository;


public class Main {
    public static void main(String[] args) {
        data.interfaces.IDB db = new PostgresDB("jdbc:postgresql://localhost:5432/credit_card_system", "your_username", "your_password");
        IUserRepository repo = new UserRepository(db);
        UserController controller = new UserController(repo);

        try {
            System.out.println(controller.createUser("John", "Doe", 30, true, 123456789, 1000, 50, 200));
            System.out.println(controller.getAllUsers());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
