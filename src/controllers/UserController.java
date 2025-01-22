package controllers;

import controllers.interfaces.IUserController;
import models.User;
import repositories.interfaces.IUserRepository;

import java.util.List;

public class UserController implements IUserController {
    private final IUserRepository repo;

    public UserController(IUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public String createUser(String name, String surname, String gender) {
        // Default values for optional fields
        int defaultAge = 18; // Default age
        int defaultCreditCard = 0; // No credit card assigned
        int defaultBalance = 0; // Zero initial balance
        int defaultWriteOffs = 0; // No write-offs initially
        int defaultDeposit = 0; // Zero deposit initially

        // Validate gender input
        boolean male = gender.equalsIgnoreCase("male");

        // Create user object
        User user = new User(
                name,
                surname,
                defaultAge,
                male,
                defaultCreditCard,
                defaultBalance,
                defaultWriteOffs,
                defaultDeposit
        );

        // Save user in repository
        boolean created = repo.createUser(user);

        return created ? "User was created successfully!" : "User creation failed.";
    }

    @Override
    public String getUserById(int id) {
        User user = repo.getUserById(id);

        if (user == null) {
            return "User not found with ID: " + id;
        }

        return user.toString();
    }

    @Override
    public String getAllUsers() {
        List<User> users = repo.getAllUsers();

        if (users.isEmpty()) {
            return "No users found in the system.";
        }

        StringBuilder response = new StringBuilder("List of Users:\n");
        for (User user : users) {
            response.append(user).append("\n");
        }

        return response.toString();
    }
}
