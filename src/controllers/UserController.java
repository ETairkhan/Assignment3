package controllers;

import controllers.interfaces.IUserController;
import models.User;
import repositories.interfaces.IUserRepository;

import java.util.List;
import java.util.logging.Logger;


public class UserController implements IUserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private final IUserRepository userRepository;

    public UserController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean isValidUserInput(String name, String surname, String gender) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (surname == null || surname.isEmpty()) {
            return false;
        }
        return "male".equalsIgnoreCase(gender) || "female".equalsIgnoreCase(gender);
    }

    @Override
    public String createUser(String name, String surname, String gender) {
        LOGGER.info(String.format("Attempting to create user: name=%s, surname=%s, gender=%s", name, surname, gender));

        if (!isValidUserInput(name, surname, gender)) {
            return "Invalid input: Name and surname cannot be empty, and gender must be 'male' or 'female'.";
        }

        try {
            User user = new User(
                    name,
                    surname,
                    18, // Default age
                    "male".equalsIgnoreCase(gender),
                    0, // Default credit card
                    0, // Default balance
                    0, // Default write-offs
                    0  // Default deposit
            );

            boolean isCreated = userRepository.createUser(user);
            return isCreated ? "User created successfully." : "Failed to create user.";
        } catch (Exception e) {
            LOGGER.severe("Error while creating user: " + e.getMessage());
            return "Error while creating user: " + e.getMessage();
        }
    }

    @Override
    public String getUserById(int id) {
        LOGGER.info("Fetching user with ID: " + id);

        try {
            User user = userRepository.getUserById(id);
            return user != null ? user.toString() : "User not found with ID: " + id;
        } catch (Exception e) {
            LOGGER.severe("Error while fetching user by ID: " + e.getMessage());
            return "Error while fetching user by ID: " + e.getMessage();
        }
    }

    @Override
    public String getAllUsers() {
        LOGGER.info("Fetching all users.");

        try {
            List<User> users = userRepository.getAllUsers();

            if (users == null || users.isEmpty()) {
                return "No users found in the system.";
            }

            StringBuilder responseBuilder = new StringBuilder("List of Users:\n");
            users.forEach(user -> responseBuilder.append(user).append("\n"));

            return responseBuilder.toString();
        } catch (Exception e) {
            LOGGER.severe("Error while fetching all users: " + e.getMessage());
            return "Error while fetching all users: " + e.getMessage();
        }
    }
}
