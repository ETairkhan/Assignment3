package controllers.interfaces;
import models.AuthUser;

public interface IUserController {
    String createUser(String name, String surname, String gender, String card, double balance);
    String getUserById(int id);
    String getAllUsers();
    String deleteUser(int id);
    String transferMoney(int senderId, int receiverId, double amount);
    String registerUser(String username, String password, String role);
    String loginUser(String username, String password);
    AuthUser getLoggedInUser(String username);

}
