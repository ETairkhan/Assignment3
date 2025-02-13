package repositories;

import data.interfaceces.IDB;
import models.*;
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

        String sql = "INSERT INTO users(name, surname, gender, card, balance, brand, issuer) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {

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
                return UserFactory.createUser(
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

        TransactionService transactionService = new TransactionService();

        try {
            double fee = transactionService.calculateTransactionFee(sender, receiver);
            double totalAmount = amount + fee;

            if (sender.getBalance() < totalAmount) {
                System.out.println("Error: Insufficient balance.");
                return false;
            }

            // Process the transaction
            transactionService.processTransaction(sender, receiver, amount);

            // Update the balances in the database
            updateUserBalance(sender);
            updateUserBalance(receiver);

            System.out.printf("Transaction Successful! Sent: %.2f Dollars, Fee: %.2f Dollars, Total Deducted: %.2f Dollars%n",
                    amount, fee, totalAmount);

            return true;
        } catch (IllegalArgumentException ex) {
            System.err.println("Transaction failed: " + ex.getMessage());
            return false;
        }
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
                Role role = new Role(rs.getString("role_id"), rs.getString("role_id")); // Use Role constructor
                return UserFactory.createAuthUser(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        role,
                        "", "", false, "", 0.0, "", ""  // Default values since no `User` fields are available here
                );
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
                Role role = new Role(rs.getString("role_id"), rs.getString("role_id")); // Use Role constructor
                return UserFactory.createAuthUser(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        role,
                        "", "", false, "", 0.0, "", ""  // Default values for User fields
                );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching logged-in user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getUsersWithAuthDetails() {
        String sql = "SELECT u.id AS user_id, u.name, u.surname, u.gender, u.card, u.balance, u.brand, u.issuer, " +
                "au.username, au.role_id " +
                "FROM users u " +
                "JOIN auth_users au ON u.name = au.username";  // Joining on name and username

        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            List<User> usersWithAuth = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getBoolean("gender"),
                        rs.getString("card"),
                        rs.getDouble("balance"),
                        rs.getString("brand"),
                        rs.getString("issuer")
                );

                // Fetch auth-related info
                String username = rs.getString("username");
                String roleId = rs.getString("role_id");

                System.out.println("======================= User Details =======================");
                System.out.printf("ID:             %-10d%n", user.getId());
                System.out.printf("Name:           %-20s%n", user.getName());
                System.out.printf("Username:       %-20s%n", username);
                System.out.printf("Role ID:        %-10s%n", roleId);
                System.out.printf("Surname:        %-20s%n", user.getSurname());
                System.out.printf("Gender:         %-10s%n", user.getGender() ? "Male" : "Female");
                System.out.printf("Card:           %-20s%n", user.getCard());
                System.out.printf("Balance:        $%-10.2f%n", user.getBalance());
                System.out.printf("Brand:          %-20s%n", user.getBrand());
                System.out.printf("Issuer:         %-20s%n", user.getIssuer());
                System.out.println("===========================================================");

                usersWithAuth.add(user);
            }
            return usersWithAuth;

        } catch (SQLException e) {
            System.err.println("Error while fetching users with auth details: " + e.getMessage());
        }
        return Collections.emptyList();
    }

}
