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

    // Validate user input
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

        // Validate the user input
        if (!isValidUserInput(name, surname, gender)) {
            return "Invalid input: Name and surname cannot be empty, and gender must be 'male' or 'female'.";
        }

        try {
            // Create a new user with default values
            User user = new User(
                    name,
                    surname,
                    18, // Default age
                    "male".equalsIgnoreCase(gender), // Gender as boolean
                    0, // Default credit card number
                    0, // Default balance
                    0, // Default write-offs
                    0  // Default deposit
            );

            // Attempt to create the user in the repository
            boolean isCreated = userRepository.createUser(user);
            if (isCreated) {
                LOGGER.info("User created successfully.");
                return "User created successfully.";
            } else {
                LOGGER.warning("Failed to create user.");
                return "Failed to create user.";
            }
        } catch (Exception e) {
            LOGGER.severe("Error while creating user: " + e.getMessage());
            return "Error while creating user: " + e.getMessage();
        }
    }

    @Override
    public String getUserById(int id) {
        LOGGER.info("Fetching user with ID: " + id);

        try {
            // Fetch user by ID
            User user = userRepository.getUserById(id);
            if (user != null) {
                return user.toString();
            } else {
                return "User not found with ID: " + id;
            }
        } catch (Exception e) {
            LOGGER.severe("Error while fetching user by ID: " + e.getMessage());
            return "Error while fetching user by ID: " + e.getMessage();
        }
    }

    @Override
    public String getAllUsers() {
        LOGGER.info("Fetching all users.");

        try {
            // Get all users from the repository
            List<User> users = userRepository.getAllUsers();
            if (users == null || users.isEmpty()) {
                return "No users found in the system.";
            }

            // Format the list of users for display
            StringBuilder responseBuilder = new StringBuilder("List of Users:\n");
            users.forEach(user -> responseBuilder.append(user).append("\n"));

            return responseBuilder.toString();
        } catch (Exception e) {
            LOGGER.severe("Error while fetching all users: " + e.getMessage());
            return "Error while fetching all users: " + e.getMessage();
        }
    }
}
