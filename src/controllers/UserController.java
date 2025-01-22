package controllers;

import controllers.interfaces.IUserController;
import models.User;
import repositories.interfaces.IUserRepository;

import java.util.List;
import java.util.logging.Logger;

public class UserController implements IUserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final IUserRepository repo;

    public UserController(IUserRepository repo) {
        this.repo = repo;
    }


    private boolean validateUserInput(String name, String surname, String gender) {
        // Validating that name and surname are not empty, and gender is either "male" or "female"
        return name != null && !name.isEmpty()
                && surname != null && !surname.isEmpty()
                && (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"));
    }

    @Override
    public String createUser(String name, String surname, String gender) {
        logger.info("Creating user: name=" + name + ", surname=" + surname + ", gender=" + gender);
        try {
            // Validation
            if (!validateUserInput(name, surname, gender)) { // Using the validateUserInput method inside UserController
                return "Invalid input: Name and surname cannot be empty, and gender must be 'male' or 'female'.";
            }

            boolean male = gender.equalsIgnoreCase("male");
            // Default values for optional fields
            int defaultAge = 18;
            int defaultCreditCard = 0;
            int defaultBalance = 0;
            int defaultWriteOffs = 0;
            int defaultDeposit = 0;

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
            return created ? "User was created successfully." : "User creation failed.";
        } catch (Exception e) {
            logger.severe("Error during user creation: " + e.getMessage());
            return "Error during user creation: " + e.getMessage();
        }
    }

    @Override
    public String getUserById(int id) {
        logger.info("Fetching user by ID: " + id);
        try {
            User user = repo.getUserById(id);
            return (user == null) ? "User not found with ID: " + id : user.toString();
        } catch (Exception e) {
            logger.severe("Error fetching user by ID: " + e.getMessage());
            return "Error fetching user by ID: " + e.getMessage();
        }
    }

    @Override
    public String getAllUsers() {
        logger.info("Fetching all users...");
        try {
            List<User> users = repo.getAllUsers();
            if (users == null || users.isEmpty()) {
                return "No users found in the system.";
            }

            // Using StringBuilder for efficient concatenation
            StringBuilder response = new StringBuilder("List of Users:\n");
            for (User user : users) {
                response.append(user).append("\n");
            }

            return response.toString();
        } catch (Exception e) {
            logger.severe("Error fetching all users: " + e.getMessage());
            return "Error fetching all users: " + e.getMessage();
        }
    }
}
