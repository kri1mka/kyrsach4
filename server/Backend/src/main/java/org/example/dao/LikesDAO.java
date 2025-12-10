package org.example.dao;

import org.example.entity.Likes;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikesDAO {

    private static final String INSERT =
            "INSERT INTO Likes (user_id, post_id) VALUES (?, ?)";

    private static final String FIND_BY_ID =
            "SELECT * FROM Likes WHERE id = ?";

    private static final String FIND_ALL =
            "SELECT * FROM Likes";

    private static final String DELETE =
            "DELETE FROM Likes WHERE id=?";

    private static final String COUNT_LIKES =
            "SELECT COUNT(*) FROM Likes WHERE post_id = ?";

    private static final String IS_LIKED =
            "SELECT COUNT(*) FROM Likes WHERE user_id = ? AND post_id = ?";

    // SAVE
    public void save(Likes like) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, like.getUser_id());
            stmt.setInt(2, like.getPost_id());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) like.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения лайка", e);
        }
    }

    // FIND BY ID
    public Likes findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Likes like = new Likes();
                like.setId(rs.getInt("id"));
                like.setUser_id(rs.getInt("user_id"));
                like.setPost_id(rs.getInt("post_id"));
                return like;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска лайка", e);
        }
        return null;
    }

    // FIND ALL
    public List<Likes> findAll() {
        List<Likes> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Likes like = new Likes();
                like.setId(rs.getInt("id"));
                like.setUser_id(rs.getInt("user_id"));
                like.setPost_id(rs.getInt("post_id"));
                list.add(like);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения лайков", e);
        }

        return list;
    }

    // DELETE
    public void delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления лайка", e);
        }
    }

    // EXTRA: COUNT LIKES
    public int countLikes(int postId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_LIKES)) {

            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подсчёта лайков", e);
        }
        return 0;
    }

    // EXTRA: CHECK IF LIKED
    public boolean isLiked(int userId, int postId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(IS_LIKED)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка проверки лайка", e);
        }
        return false;
    }
}
