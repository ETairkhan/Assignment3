package controllers.interfaces;

public interface IUserController {
    String createUser(String name, String surname, String gender, String card, double balance);
    String getUserById(int id);
    String getAllUsers();
    String deleteUser(int id);
}
