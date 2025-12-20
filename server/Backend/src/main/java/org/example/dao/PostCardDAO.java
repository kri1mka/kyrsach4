package org.example.dao;

import org.example.dto.PostDto;
import org.example.entity.PostCard;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostCardDAO {

    private final Connection connection;
    public PostCardDAO(Connection connection) {
        this.connection = connection;
    }

    private static final String INSERT =
            "INSERT INTO postcard (user_id, description, location, created_at, photo_id) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID = "SELECT * FROM postcard WHERE id = ?";
    private static final String FIND_BY_USER =
            "SELECT p.*, CONCAT(u.name, ' ', u.surname) AS user_name " +
                    "FROM postcard p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "WHERE p.user_id = ? " +
                    "ORDER BY p.created_at DESC";

    private static final String FIND_LATEST = "SELECT * FROM postcard ORDER BY created_at DESC LIMIT ?";
    private static final String FIND_ALL = "SELECT * FROM postcard";
    private static final String UPDATE =
            "UPDATE postcard SET description=?, location=?, photo_id=? WHERE id=?";
    private static final String DELETE = "DELETE FROM postcard WHERE id=?";
    private static final String IMAGE_BASE_URL =
            "http://10.0.2.2:8080/Backend/images/";


    public List<PostDto> getFeed() {
        List<PostDto> list = new ArrayList<>();

        String sql = """
            SELECT
                p.id,
                p.user_id,
                p.description,
                p.location,
                p.created_at,
                p.photo_id AS photo_url,
                CONCAT(u.name, ' ', u.surname) AS user_name,
                ui.avatarUrl AS avatar_url,
                COUNT(l.post_id) AS likes_count
            FROM PostCard p
            JOIN Users u
                ON p.user_id = u.id
            LEFT JOIN UsersInfo ui
                ON ui.user_id = u.id
            LEFT JOIN Likes l
                ON l.post_id = p.id
            GROUP BY
                p.id,
                p.user_id,
                p.description,
                p.location,
                p.created_at,
                p.photo_id,
                u.name,
                u.surname,
                ui.avatarUrl
            ORDER BY p.created_at DESC;
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PostDto dto = new PostDto();
                dto.id = rs.getInt("id");
                dto.user_id = rs.getInt("user_id");
                dto.description = rs.getString("description");
                dto.location = rs.getString("location");
                dto.createdAt = rs.getTimestamp("created_at").toString();
                String fileName = rs.getString("photo_url");
                dto.photoUrl = IMAGE_BASE_URL + fileName;
                dto.userName = rs.getString("user_name");
                String avatar_filename = rs.getString("avatar_url");
                dto.avatarUrl = IMAGE_BASE_URL + avatar_filename;
                dto.likes_count = rs.getInt("likes_count");
                list.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки ленты", e);
        }

        return list;
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
        card.setPhotoIt(rs.getString("photo_id"));
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