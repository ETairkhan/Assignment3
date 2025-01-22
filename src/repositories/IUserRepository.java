package repositories;

public interface IUserRepository {
    package repositories;

import models.User;

import java.util.List;

    public interface IUserRepository {
        boolean addUser(User user);
        User getUserById(int id);
        List<User> getAllUsers();
        boolean updateBalance(int userId, double amount);
        boolean deleteUser(int id);
    }

}
