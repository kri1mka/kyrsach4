package org.example.dao;

import org.example.entity.UsersInfo;
import org.example.util.DBConnection;

import java.sql.*;

public class UsersInfoDAO {

    private static final String INSERT = "INSERT INTO UsersInfo (user_id, date_of_birth, sex, age, interests, about) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM UsersInfo WHERE id = ?";
    private static final String FIND_BY_USER_ID = "SELECT * FROM UsersInfo WHERE user_id = ?";
    private static final String UPDATE = "UPDATE UsersInfo SET date_of_birth=?, sex=?, age=?, interests=?, about=? WHERE id=?";
    private static final String DELETE = "DELETE FROM UsersInfo WHERE id=?";

    public void save(UsersInfo info) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, info.getUser_id());
            stmt.setDate(2, info.getDate_of_birth() != null ? new java.sql.Date(info.getDate_of_birth().getTime()) : null);
            stmt.setString(3, info.getSex());
            stmt.setInt(4, info.getAge());
            stmt.setString(5, info.getInterests());
            stmt.setString(6, info.getAbout());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) info.setId(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения UsersInfo", e);
        }
    }

    public UsersInfo findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска UsersInfo", e);
        }
        return null;
    }

    public UsersInfo findByUserId(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_USER_ID)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска по user_id", e);
        }
        return null;
    }

    public void update(UsersInfo info) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setDate(1, info.getDate_of_birth() != null ? new java.sql.Date(info.getDate_of_birth().getTime()) : null);
            stmt.setString(2, info.getSex());
            stmt.setInt(3, info.getAge());
            stmt.setString(4, info.getInterests());
            stmt.setString(5, info.getAbout());
            stmt.setInt(6, info.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления UsersInfo", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления UsersInfo", e);
        }
    }

    private UsersInfo map(ResultSet rs) throws SQLException {
        UsersInfo info = new UsersInfo();
        info.setId(rs.getInt("id"));
        info.setUser_id(rs.getInt("user_id"));
        info.setDate_of_birth(rs.getDate("date_of_birth"));
        info.setSex(rs.getString("sex"));
        info.setAge(rs.getInt("age"));
        info.setInterests(rs.getString("interests"));
        info.setAbout(rs.getString("about"));
        return info;
    }
}
