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
    public String createUser(String name, String surname, String gender, String card, double balance) {

        if (!Validator.isValidLuhn(card)) { // Validate credit card number

            return "Invalid credit card number. User creation failed.";
        }

        boolean male = gender.equalsIgnoreCase("male");

        // Determine the brand and issuer using CardInformation
        Map<String, String> brands = CardInformation.loadData("src/resources/brands.txt");
        Map<String, String> issuers = CardInformation.loadData("src/resources/issuers.txt");

        String brand = "-";
        String issuer = "-";

        for (Map.Entry<String, String> entry : brands.entrySet()) {
            if (card.startsWith(entry.getKey())) {
                brand = entry.getValue();
                break;
            }
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

}
