package models;

import models.AuthUser;
import models.Role;
import models.User;

public class UserFactory {

    // Factory method to create a basic User
    public static User createUser(int id, String name, String surname, boolean gender, String card,
                                  double balance, String brand, String issuer) {
        return new User(id, name, surname, gender, card, balance, brand, issuer);
    }

    // Factory method to create an AuthUser with roles
    public static AuthUser createAuthUser(int id, String username, String password, Role role,
                                          String name, String surname, boolean gender,
                                          String card, double balance, String brand, String issuer) {
        AuthUser authUser = new AuthUser(id, username, password, role, name, surname, gender,
                card, balance, brand, issuer);
        return authUser;
    }
}