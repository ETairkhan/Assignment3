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
    public String createUser(String name, String surname, String gender, String card, double balance) {

        if (!repo.isValidCreditCard(card)) { // Validate credit card number
            return "Invalid credit card number. User creation failed.";
        }

        boolean male = gender.equalsIgnoreCase("male");
        User user = new User(name, surname, male, card, balance);
        boolean created = repo.createUser(user);
        return (created) ? "User was created" : "User creation was failed";
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
    public boolean isValidCreditCard(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }


}
