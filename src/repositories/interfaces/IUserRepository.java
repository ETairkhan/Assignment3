package repositories.interfaces;

import models.User;
import models.AuthUser;
import java.util.List;

public interface IUserRepository {
    boolean createUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();
    boolean deleteUser(int id);
    boolean updateUserBalance(User user);
    boolean transferMoney(int senderId, int receiverId, double amount);

    boolean registerUser(String username, String password, String roleId);
    AuthUser authenticateUser(String username, String password);
    String getRoleIdByName(String roleName);
    AuthUser getLoggedInUser(String username);



}
