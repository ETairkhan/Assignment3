package controllers;

import controllers.interfaces.IUserRepository;
import models.User;
import java.util.List;

public class UserController {
    private final IUserRepository userRepository;

    public UserController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(String name, String surname, int age, boolean gender, int creditCard, int balance, int writeOffs, int deposit) {
        models.User user = new User(name, surname, age, gender, creditCard, balance, writeOffs, deposit);
        boolean isCreated = userRepository.createUser(user);
        return isCreated ? "User created successfully." : "Failed to create user.";
    }

    public String getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        StringBuilder response = new StringBuilder();
        for (User user : users) {
            response.append(user.toString()).append("\n");
        }
        return response.toString();
    }
}
