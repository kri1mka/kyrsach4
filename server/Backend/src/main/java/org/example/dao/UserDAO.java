package org.example.dao;


import org.example.entity.User;
import org.example.util.DBConnection;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    private final Connection conn;


    private static final String INSERT = "INSERT INTO Users (name, surname, email, phone_number, password) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT id, name, surname, email, phone_number FROM Users WHERE id = ?";
    private static final String FIND_ALL = "SELECT id, name, surname, email, phone_number FROM Users";
    private static final String UPDATE = "UPDATE Users SET name=?, surname=?, email=?, phone_number=? WHERE id=?";
    private static final String DELETE = "DELETE FROM Users WHERE id=?";


    public void save(User user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {


            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getPassword());


            stmt.executeUpdate();


            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения пользователя", e);
        }
    }
    public User findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {


            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setSurname(rs.getString("surname"));
                u.setEmail(rs.getString("email"));
                u.setPhoneNumber(rs.getString("phone_number"));
                return u;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя", e);
        }
        return null;
    }
    public List<User> findAll() {
        List<User> list = new ArrayList<>();


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {


            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setSurname(rs.getString("surname"));
                u.setEmail(rs.getString("email"));
                u.setPhoneNumber(rs.getString("phone_number"));
                list.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения пользователей", e);
        }
        return list;
    }


    public void update(User user) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {


            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setInt(5, user.getId());


            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления пользователя", e);
        }
    }
    public void delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {


            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления пользователя", e);
        }
    }
}
