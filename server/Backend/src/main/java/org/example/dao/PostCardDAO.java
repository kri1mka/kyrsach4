package org.example.dao;

import org.example.entity.PostCard;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostCardDAO {

    private static final String INSERT =
            "INSERT INTO PostCard (user_id, description, location, photo_it) VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_ID = "SELECT * FROM PostCard WHERE id = ?";
    private static final String FIND_BY_USER = "SELECT * FROM PostCard WHERE user_id = ?";
    private static final String FIND_LATEST = "SELECT * FROM PostCard ORDER BY created_at DESC LIMIT ?";
    private static final String FIND_ALL = "SELECT * FROM PostCard";
    private static final String UPDATE =
            "UPDATE PostCard SET description=?, location=?, photo_it=? WHERE id=?";
    private static final String DELETE = "DELETE FROM PostCard WHERE id=?";

    public void save(PostCard card) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, card.getUser_id());
            stmt.setString(2, card.getDescription());
            stmt.setString(3, card.getLocation());
            stmt.setString(4, card.getPhoto_it());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) card.setId(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения PostCard", e);
        }
    }

    public PostCard findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return map(rs);

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
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска постов по user_id", e);
        }
        return list;
    }

    public List<PostCard> findLatest(int limit) {
        List<PostCard> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_LATEST)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) list.add(map(rs));

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
            stmt.setString(3, card.getPhoto_it());
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
        card.setUser_id(rs.getInt("user_id"));
        card.setDescription(rs.getString("description"));
        card.setLocation(rs.getString("location"));
        card.setCreated_at(rs.getTimestamp("created_at"));
        card.setPhoto_it(rs.getString("photo_it"));
        return card;
    }
}
