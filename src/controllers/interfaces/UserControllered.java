package controllers;

import models.User;
import repositories.IUserRepository;

import java.util.List;

public class UserControllered {
    private final IUserRepository userRepository;

    public UserControllered(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String addUser(String name, String surname, String creditCard, double balance) {
        User user = new User(name, surname, creditCard, balance);
        boolean success = userRepository.addUser(user);
        return success ? "User added successfully." : "Failed to add user.";
    }

    public String getUserById(int id) {
        User user = userRepository.getUserById(id);
        return (user != null) ? user.toString() : "User not found.";
    }

    public String getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        return users.isEmpty() ? "No users found." : users.toString();
    }

    public String updateBalance(int userId, double amount) {
        boolean success = userRepository.updateBalance(userId, amount);
        return success ? "Balance updated successfully." : "Failed to update balance.";
    }

    public String deleteUser(int id) {
        boolean success = userRepository.deleteUser(id);
        return success ? "User deleted successfully." : "Failed to delete user.";
    }
}

