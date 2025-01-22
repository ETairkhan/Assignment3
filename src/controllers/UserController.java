package controllers;

import controllers.interfaces.IUserController;
import models.User;
import repositories.interfaces.IUserRepository;
import services.UserService;

import java.util.List;
import java.util.logging.Logger;

public class UserController implements IUserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final IUserRepository repo;
    private final UserService userService;

    public UserController(IUserRepository repo) {
        this.repo = repo;
        this.userService = new UserService();
    }

    @Override
    public String createUser(String name, String surname, String gender) {
        logger.info("Creating user: name=" + name + ", surname=" + surname + ", gender=" + gender);
        try {
            // Валидация данных
            if (!userService.validateUserInput(name, surname, gender)) {
                return Messages.INVALID_USER_INPUT;
            }

            boolean male = gender.equalsIgnoreCase("male");
            User user = new User(name, surname, male);
            boolean created = repo.createUser(user);

            return created ? Messages.USER_CREATED : Messages.USER_CREATION_FAILED;
        } catch (Exception e) {
            logger.severe("Error during user creation: " + e.getMessage());
            return Messages.ERROR_DURING_CREATION + e.getMessage();
        }
    }

    @Override
    public String getUserById(int id) {
        logger.info("Fetching user by ID: " + id);
        try {
            User user = repo.getUserById(id);
            return (user == null) ? Messages.USER_NOT_FOUND : user.toString();
        } catch (Exception e) {
            logger.severe("Error fetching user by ID: " + e.getMessage());
            return Messages.ERROR_FETCHING_USER + e.getMessage();
        }
    }

    @Override
    public String getAllUsers() {
        logger.info("Fetching all users...");
        try {
            List<User> users = repo.getAllUsers();
            if (users == null || users.isEmpty()) {
                return Messages.NO_USERS_FOUND;
            }

            // Использование Stream API для преобразования списка пользователей в строку
            return users.stream()
                    .map(User::toString)
                    .reduce("", (user1, user2) -> user1 + user2 + "\n");
        } catch (Exception e) {
            logger.severe("Error fetching all users: " + e.getMessage());
            return Messages.ERROR_FETCHING_USERS + e.getMessage();
        }
    }
}


package services;

public class UserService {
    public boolean validateUserInput(String name, String surname, String gender) {
        return name != null && !name.isEmpty()
                && surname != null && !surname.isEmpty()
                && (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"));
    }
}

package controllers;

public class Messages {
    public static final String USER_CREATED = "User was created successfully.";
    public static final String USER_CREATION_FAILED = "User creation failed.";
    public static final String INVALID_USER_INPUT = "Invalid input: Name and surname cannot be empty, and gender must be 'male' or 'female'.";
    public static final String USER_NOT_FOUND = "User was not found.";
    public static final String NO_USERS_FOUND = "No users found.";
    public static final String ERROR_DURING_CREATION = "Error during user creation: ";
    public static final String ERROR_FETCHING_USER = "Error fetching user by ID: ";
    public static final String ERROR_FETCHING_USERS = "Error fetching users: ";
}