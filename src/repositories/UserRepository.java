package repositories;

import data.interfaceces.IDB;
import models.User;
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

            return st.executeUpdate() > 0;

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
        String sql = "SELECT id, name, surname, gender, card, balance, brand, issuer FROM users";
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

}
