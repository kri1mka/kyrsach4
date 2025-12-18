package org.example.dao;

import org.example.dto.PostDto;
import org.example.entity.PostCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostCardDAO {

    private final Connection conn;

    public PostCardDAO(Connection conn) {
        this.conn = conn;
    }

    private static final String INSERT =
            "INSERT INTO PostCard (user_id, description, location, photo_it) VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_ID =
            "SELECT * FROM PostCard WHERE id = ?";

    private static final String FIND_BY_USER =
            "SELECT * FROM PostCard WHERE user_id = ?";

    private static final String FIND_LATEST =
            "SELECT * FROM PostCard ORDER BY created_at DESC LIMIT ?";

    private static final String FIND_ALL =
            "SELECT * FROM PostCard ORDER BY created_at DESC";

    private static final String UPDATE =
            "UPDATE PostCard SET description=?, location=?, photo_it=? WHERE id=?";

    private static final String DELETE =
            "DELETE FROM PostCard WHERE id=?";

    // ======================= CRUD =======================

    public void save(PostCard card) {
        try (PreparedStatement stmt =
                     conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, card.getUser_id());
            stmt.setString(2, card.getDescription());
            stmt.setString(3, card.getLocation());
            stmt.setString(4, card.getPhoto_it());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    card.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения PostCard", e);
        }
    }

    public PostCard findById(int id) {
        try (PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

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

        try (PreparedStatement stmt = conn.prepareStatement(FIND_BY_USER)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска постов по user_id", e);
        }
        return list;
    }

    public List<PostCard> findLatest(int limit) {
        List<PostCard> list = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(FIND_LATEST)) {

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

        try (PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех PostCard", e);
        }
        return list;
    }

    public void update(PostCard card) {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

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
        try (PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления PostCard", e);
        }
    }

    // ======================= FEED =======================

    public List<PostDto> getFeed() {
        List<PostDto> list = new ArrayList<>();

        String sql = """
            SELECT 
                p.id,
                p.description,
                p.location,
                p.created_at,
                ph.photo_url AS photo_url,
                CONCAT(u.name, ' ', u.surname) AS user_name
            FROM PostCard p
            JOIN Users u ON p.user_id = u.id
            LEFT JOIN Photo ph ON p.photo_id = ph.id
            ORDER BY p.created_at DESC
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PostDto dto = new PostDto();
                dto.id = rs.getInt("id");
                dto.description = rs.getString("description");
                dto.location = rs.getString("location");
                dto.createdAt = rs.getTimestamp("created_at").toString();
                dto.photoUrl = rs.getString("photo_url");
                dto.userName = rs.getString("user_name");
                list.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки ленты", e);
        }

        return list;
    }

    // ======================= MAPPER =======================

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
