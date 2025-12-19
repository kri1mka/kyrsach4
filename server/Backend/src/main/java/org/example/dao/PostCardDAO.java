package org.example.dao;

import org.example.entity.PostCard;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostCardDAO {

    private final Connection connection;
    private static final String INSERT =
            "INSERT INTO postcard (user_id, description, location, created_at, photo_it) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID = "SELECT * FROM postcard WHERE id = ?";
    private static final String FIND_BY_USER =
            "SELECT p.*, CONCAT(u.name, ' ', u.surname) AS user_name " +
                    "FROM postcard p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "WHERE p.user_id = ? " +
                    "ORDER BY p.created_at DESC";

    //"SELECT * FROM postcard WHERE user_id = ?";
    private static final String FIND_LATEST = "SELECT * FROM postcard ORDER BY created_at DESC LIMIT ?";
    private static final String FIND_ALL = "SELECT * FROM postcard";
    private static final String UPDATE =
            "UPDATE postcard SET description=?, location=?, photo_it=? WHERE id=?";
    private static final String DELETE = "DELETE FROM postcard WHERE id=?";

    public PostCardDAO(Connection connection) {
        this.connection = connection;
    }
    public void save(PostCard card) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, card.getUserId());
            stmt.setString(2, card.getDescription());
            stmt.setString(3, card.getLocation());
            stmt.setTimestamp(4, card.getCreatedAt() != null ? new Timestamp(card.getCreatedAt().getTime()) : new Timestamp(System.currentTimeMillis()));
            stmt.setString(5, card.getPhotoIt());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) card.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения PostCard", e);
        }
    }

    public PostCard findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return map(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска PostCard", e);
        }
        return null;
    }

    public List<PostCard> findByUserId(int userId) {
        List<PostCard> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_USER)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска постов по userId", e);
        }
        return list;
    }

    public List<PostCard> findLatest(int limit) {
        List<PostCard> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_LATEST)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска последних постов", e);
        }
        return list;
    }

    public List<PostCard> findAll() {
        List<PostCard> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех PostCard", e);
        }
        return list;
    }

    public void update(PostCard card) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, card.getDescription());
            stmt.setString(2, card.getLocation());
            stmt.setString(3, card.getPhotoIt());
            stmt.setInt(4, card.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления PostCard", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления PostCard", e);
        }
    }

    private PostCard map(ResultSet rs) throws SQLException {
        PostCard card = new PostCard();
        card.setId(rs.getInt("id"));
        card.setUserId(rs.getInt("user_id"));
        card.setDescription(rs.getString("description"));
        card.setLocation(rs.getString("location"));
        card.setCreatedAt(rs.getTimestamp("created_at"));
        card.setPhotoIt(rs.getString("photo_it"));
        card.setUserName(rs.getString("user_name"));
        return card;
    }

    public String getUserFullName(int userId) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT name, surname FROM users WHERE id = ?")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name") + " " + rs.getString("surname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
