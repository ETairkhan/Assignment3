package repositories;

import data.interfaces.IDB;
import models.User;
import controllers.interfaces.IUserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository {
    private final IDB db;

    public UserRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO users(name, surname, gender, age, creditCard, balance, writeOffs, deposit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = db.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getName());
            st.setString(2, user.getSurname());
            st.setBoolean(3, user.isGender());
            st.setInt(4, user.getAge());
            st.setInt(5, user.getCreditCard());
            st.setInt(6, user.getBalance());
            st.setInt(7, user.getWriteOffs());
            st.setInt(8, user.getDeposit());

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = db.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getInt("age"),
                            rs.getBoolean("gender"),
                            rs.getInt("creditCard"),
                            rs.getInt("balance"),
                            rs.getInt("writeOffs"),
                            rs.getInt("deposit")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = db.getConnection(); Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getInt("age"),
                        rs.getBoolean("gender"),
                        rs.getInt("creditCard"),
                        rs.getInt("balance"),
                        rs.getInt("writeOffs"),
                        rs.getInt("deposit")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, surname = ?, gender = ?, age = ?, creditCard = ?, balance = ?, writeOffs = ?, deposit = ? WHERE id = ?";
        try (Connection connection = db.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, user.getName());
            st.setString(2, user.getSurname());
            st.setBoolean(3, user.isGender());
            st.setInt(4, user.getAge());
            st.setInt(5, user.getCreditCard());
            st.setInt(6, user.getBalance());
            st.setInt(7, user.getWriteOffs());
            st.setInt(8, user.getDeposit());
            st.setInt(9, user.getId());

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = db.getConnection(); PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
