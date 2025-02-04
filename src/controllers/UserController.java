package controllers;

import controllers.interfaces.IUserController;
import models.AuthUser;
import models.User;
import repositories.interfaces.IUserRepository;
import validate.Validator;
import java.util.Map;
import validate.CardInformation;

import java.util.List;

public class UserController implements IUserController {
    private final IUserRepository repo;

    public UserController(IUserRepository repo)  {
        this.repo = repo;
    }
    @Override
    public String createUser(String name, String surname, String gender, String card, double balance) {

        if (!Validator.isValidLuhn(card)) {
            return "Invalid credit card number. User creation failed.";
        }

        boolean male = gender.equalsIgnoreCase("male");


        Map<String, String> issuers = CardInformation.loadData("src/resources/issuers.txt");
        Map<String, List<String>> brands = CardInformation.loadDataAsList("src/resources/brands.txt");

        String brand = "-";
        String issuer = "-";


        for (Map.Entry<String, List<String>> entry : brands.entrySet()) {
            for (String prefix : entry.getValue()) {
                if (card.startsWith(prefix)) {
                    brand = entry.getKey();
                    break;
                }
            }
            if (!brand.equals("-")) break;
        }


        for (Map.Entry<String, String> entry : issuers.entrySet()) {
            if (card.startsWith(entry.getKey())) {
                issuer = entry.getValue();
                break;
            }
        }


        User user = new User(name, surname, male, card, balance, brand, issuer);
        boolean created = repo.createUser(user);
        return (created) ? "User was created" : "User creation failed";
    }



    @Override
    public String getUserById(int id) {
        User user = repo.getUserById(id);
        return (user == null) ? "User was not found" : user.toString();
    }

    @Override
    public String getAllUsers() {
        List<User> users = repo.getAllUsers();
        StringBuilder responce = new StringBuilder();
        for (User user : users) {
            responce.append(user.toString()).append("\n");
        }
        return responce.toString();
    }
    @Override
    public String deleteUser(int id) {
        boolean deleted = repo.deleteUser(id);
        return (deleted) ? "User was successfully deleted." : "User deletion failed. User may not exist.";
    }

    @Override
    public String transferMoney(int senderId, int receiverId, double amount) {
        if (amount <= 0) {
            return "Invalid transfer amount. Transfer failed.";
        }

        User sender = repo.getUserById(senderId);
        User receiver = repo.getUserById(receiverId);

        if (sender == null) {
            return "Sender not found. Transfer failed.";
        }
        if (receiver == null) {
            return "Receiver not found. Transfer failed.";
        }

        double commission = 1.0;
        boolean isDifferentBank = !sender.getIssuer().equalsIgnoreCase(receiver.getIssuer());


        double totalAmount = isDifferentBank ? amount + commission : amount;

        if (sender.getBalance() < totalAmount) {
            return "Insufficient balance. Transfer failed.";
        }


        sender.setBalance(sender.getBalance() - totalAmount);
        receiver.setBalance(receiver.getBalance() + amount);

        boolean senderUpdated = repo.updateUserBalance(sender);
        boolean receiverUpdated = repo.updateUserBalance(receiver);

        if (senderUpdated && receiverUpdated) {
            return "Transfer successful. Transferred " + amount + " from User " + senderId +
                    " to User " + receiverId + (isDifferentBank ? " with a $1 commission." : " without commission.");
        } else {
            return "Transfer failed due to a database error.";
        }
    }


    @Override
    public String registerUser(String username, String password, String roleName) {
        roleName = roleName.trim().toLowerCase(); // Normalize input
        String roleId = repo.getRoleIdByName(roleName); // Get role as String

        if (roleId == null) {
            return "Invalid role. Choose 'admin' or 'user'.";
        }

        boolean success = repo.registerUser(username, password, roleId);
        return success ? "User registered successfully!" : "User registration failed.";
    }



    @Override
    public String loginUser(String username, String password) {
        AuthUser user = repo.authenticateUser(username, password);
        if (user == null) {
            return "Invalid username or password.";
        }
        return "Login successful! Welcome " + user.getUsername() + ", Role: " + user.getRole().getName();
    }

    @Override
    public AuthUser getLoggedInUser(String username) {
        return repo.getLoggedInUser(username);
    }

}




