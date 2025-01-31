package controllers;

import controllers.interfaces.IUserController;
import models.User;
import repositories.interfaces.IUserRepository;
import validate.Validator;
import java.util.Map;
import validate.CardInformation;

import java.util.List;

public class UserController implements IUserController {
    private final IUserRepository repo;

    public UserController(IUserRepository repo) {
        this.repo = repo;
    }
    @Override
    public String createUser(String name, String surname, String gender, String card, double balance, String role) {


        if (!Validator.isValidLuhn(card) || card.length() > 19) {
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

        // Create user object
        User user = new User(name, surname, male, card, balance, brand, issuer, role);
        boolean created = repo.createUser(user);
        return (created) ? "User was created" : "User creation failed";
    }



    @Override
    public String getUserById(int id) {
        User user = repo.getUserById(id);
        if (user == null) {
            return "User with ID " + id + " not found.";
        }
        return user.toString();
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
    private boolean hasAccess(User user, String requiredRole) {
        if (user == null) {
            return false;
        }
        return user.getRole().equalsIgnoreCase(requiredRole) || user.getRole().equalsIgnoreCase("admin");
    }

    @Override
    public String deleteUser(int id, String adminId) {
        User admin = repo.getUserById(id);
        if (!hasAccess(admin, "admin")) {
            return "Access Denied: Only admins can delete users.";
        }
        boolean deleted = repo.deleteUser(id);
        return (deleted) ? "User successfully deleted." : "User deletion failed.";
    }

    @Override
    public String transferMoney(int senderId, int receiverId, double amount) {
        if (amount <= 0) {
            return "Invalid transfer amount. Transfer failed.";
        }

        User sender = repo.getUserById(senderId);
        User receiver = repo.getUserById(receiverId);

        if (sender == null || receiver == null) {
            return "Sender or receiver not found. Transfer failed.";
        }

        if (sender.getBalance() < amount) {
            return "Insufficient balance. Transfer failed.";
        }

        // Check if sender has privileges (only admins or financial managers)
        if (!hasAccess(sender, "manager")) {
            return "Access Denied: Only managers or admins can transfer money.";
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        boolean senderUpdated = repo.updateUserBalance(sender);
        boolean receiverUpdated = repo.updateUserBalance(receiver);

        return (senderUpdated && receiverUpdated) ?
                "Transfer successful." :
                "Transfer failed due to a database error.";
    }


}
