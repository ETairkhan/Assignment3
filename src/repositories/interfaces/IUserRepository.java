package repositories.interfaces;

import models.User;

import java.util.List;

public interface IUserRepository {
    boolean createUser(User user);
    User getUserById(int id);
    List<User> getAllUsers();

    // Update a user
    boolean updateUser(User user);

    // Delete a user by ID
    boolean deleteUser(int id);
}
