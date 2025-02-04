package repositories;

import data.interfaceces.IDB;
import models.User;
import models.Role;
import models.AuthUser;
import repositories.interfaces.IUserRepository;
import validate.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserRepository implements IUserRepository {
    private final IDB db;

    public UserRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createUser(User user) {
        if (user == null || user.getCard() == null || !Validator.isValidLuhn(user.getCard())) {
            System.out.println("Invalid input or credit card number.");
            return false;
        }

        if (cardExists(user.getCard())) {
            System.out.println("A user with this credit card number already exists.");
            return false;
        }

        String sqlValues = "INSERT INTO users(name, surname, gender, card, balance, brand, issuer) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sqlValues)) {

            st.setString(1, user.getName());
            st.setString(2, user.getSurname());
            st.setBoolean(3, user.getGender());
            st.setString(4, user.getCard());
            st.setDouble(5, user.getBalance());
            st.setString(6, user.getBrand());
            st.setString(7, user.getIssuer());

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error while creating user: " + e.getMessage());
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
                        rs.getDouble("balance"),
                        rs.getString("brand"),
                        rs.getString("issuer")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching user by ID: " + e.getMessage());
        }
        return null;
    }



    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        try (Connection connection = db.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getBoolean("gender"),
                        rs.getString("card"),
                        rs.getDouble("balance"),
                        rs.getString("brand"),
                        rs.getString("issuer")
                ));
            }
            return users;

        } catch (SQLException e) {
            System.err.println("Error while fetching all users: " + e.getMessage());
        }
        return Collections.emptyList();
    }


    @Override
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error while deleting user: " + e.getMessage());
        }
        return false;
    }
    @Override
    public boolean updateUserBalance(User user) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {

            st.setDouble(1, user.getBalance());
            st.setInt(2, user.getId());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error while updating user balance: " + e.getMessage());
        }
        return false;
    }

    public boolean cardExists(String card) {
        String sql = "SELECT COUNT(*) FROM users WHERE card = ?";
        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {

            st.setString(1, card);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error while checking credit card: " + e.getMessage());
        }
        return false;
    }

    public boolean transferMoney(int senderId, int receiverId, double amount) {
        User sender = getUserById(senderId);
        User receiver = getUserById(receiverId);

        if (sender == null || receiver == null) {
            System.out.println("Error: One or both users not found.");
            return false;
        }

        String senderBank = sender.getIssuer();
        String receiverBank = receiver.getIssuer();
        double fee = calculateTransactionFee(senderBank, receiverBank);
        double totalAmount = amount + fee;

        if (sender.getBalance() < totalAmount) {
            System.out.println("Error: Insufficient balance.");
            return false;
        }

        sender.setBalance(sender.getBalance() - totalAmount);
        updateUserBalance(sender);

        receiver.setBalance(receiver.getBalance() + amount);
        updateUserBalance(receiver);

        System.out.printf("Transaction Successful! Sent: %.2f Dollars, Fee: %.2f Dollars, Total Deducted: %.2f Dollars%n",
                amount, fee, totalAmount);

        return true;
    }

    private double calculateTransactionFee(String senderBank, String receiverBank) {
        return senderBank.equalsIgnoreCase(receiverBank) ? 0 : 150;
    }

    @Override
    public boolean registerUser(String username, String password, String roleId) {
        String sql = "INSERT INTO auth_users (username, password, role_id) VALUES (?, ?, ?)";

        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, password);  // Store plain password
            st.setString(3, roleId);  // Directly store role as String
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
        return false;
    }

    @Override
    public AuthUser authenticateUser(String username, String password) {
        String sql = "SELECT id, username, password, role_id FROM auth_users " +
                "WHERE LOWER(username) = LOWER(?) AND password = ?";

        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Role role = new Role(rs.getString("role_id"), rs.getString("role_id")); // Use role_id directly
                return new AuthUser(rs.getInt("id"), rs.getString("username"), rs.getString("password"), role);
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating user: " + e.getMessage());
        }
        return null;
    }



    @Override
    public String getRoleIdByName(String roleName) {
        // Directly return role name since we don't have a roles table
        if (roleName.equalsIgnoreCase("admin") || roleName.equalsIgnoreCase("user")) {
            return roleName.toLowerCase(); // Normalize to lowercase
        }
        return null; // Return null if role is invalid
    }




    @Override
    public AuthUser getLoggedInUser(String username) {
        String sql = "SELECT id, username, password, role_id FROM auth_users " +
                "WHERE LOWER(username) = LOWER(?)";

        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Role role = new Role(rs.getString("role_id"), rs.getString("role_id")); // Use role_id directly
                return new AuthUser(rs.getInt("id"), rs.getString("username"), rs.getString("password"), role);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching logged-in user: " + e.getMessage());
        }
        return null;
    }




}
