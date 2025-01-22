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

        String sql = "INSERT INTO users(name, surname, gender, card, balance) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = db.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {


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
}
