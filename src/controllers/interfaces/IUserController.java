package controllers.interfaces;

public interface IUserController {
    String createUser(String name, String surname, String gender, String card, double balance, String role);
    String getUserById(int id);
    String getAllUsers();
    String deleteUser(int id, String role);
    String transferMoney(int senderId, int receiverId, double amount);
}
