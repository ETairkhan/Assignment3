package repositories;

import data.interfaceces.IDB;
import models.User;
import repositories.interfaces.IUserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public  class UserRepository implements IUserRepository {
    private final IDB db;

    public UserRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createUser(User user) {
        if (!isValidCreditCard(user.getCard())) { // Validate the credit card
            System.out.println("Invalid credit card number.");
            return false; // Return early if the card is invalid
        }

        Connection connection = null;
        try {
            connection = db.getConnection();
            String sql = "INSERT INTO users(name, surname, gender, card, balance) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, user.getName());
            st.setString(2, user.getSurname());
            st.setBoolean(3, user.getGender());
            st.setString(4, user.getCard());
            st.setDouble(5, user.getBalance());

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
        return false;
    }



    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getBoolean("gender"),
                        rs.getString("card"),
                        rs.getDouble("balance")
                );
            }

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT id, name, surname, gender, card, balance FROM users";
        try (Connection connection = db.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getBoolean("gender"),
                        rs.getString("card"),
                        rs.getDouble("balance")
                );
                users.add(user);
            }
            return users;

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
        return new ArrayList<>(); // Return empty list if no users found
    }

    @Override
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
        return false;
    }

    public boolean isValidCreditCard(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        // Start from the rightmost digit and iterate left
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (alternate) {
                digit *= 2; // Double every second digit
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit; // Add the digit to the sum
            alternate = !alternate; // Toggle the alternate flag
        }

        return sum % 10 == 0; // Valid if the sum is divisible by 10
    }

}
