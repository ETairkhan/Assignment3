package repositories.interfaces;

import models.User;

import java.util.List;

public interface IUserRepository {
    boolean createUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    boolean deleteUser(int id);
    boolean updateUserBalance(User user);
    boolean transferMoney(int senderId, int receiverId, double amount);



}
