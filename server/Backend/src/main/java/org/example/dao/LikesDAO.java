package org.example.dao;

import org.example.entity.Likes;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikesDAO {

    private static final String INSERT = "INSERT INTO Likes (user_id, post_id) VALUES (?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM Likes WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM Likes";
    private static final String DELETE = "DELETE FROM Likes WHERE id=?";
    private static final String COUNT_LIKES = "SELECT COUNT(*) FROM Likes WHERE post_id = ?";
    private static final String IS_LIKED = "SELECT COUNT(*) FROM Likes WHERE user_id = ? AND post_id = ?";

    public void save(Likes like) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, like.getUserId());
            stmt.setInt(2, like.getPostId());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) like.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения лайка", e);
        }
    }

    public Likes findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Likes like = new Likes();
                    like.setId(rs.getInt("id"));
                    like.setUserId(rs.getInt("user_id"));
                    like.setPostId(rs.getInt("post_id"));
                    return like;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска лайка", e);
        }
        return null;
    }

    public List<Likes> findAll() {
        List<Likes> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Likes like = new Likes();
                like.setId(rs.getInt("id"));
                like.setUserId(rs.getInt("user_id"));
                like.setPostId(rs.getInt("post_id"));
                list.add(like);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения лайков", e);
        }
        return list;
    }

    public void delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления лайка", e);
        }
    }

    public int countLikes(int postId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_LIKES)) {

            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подсчёта лайков", e);
        }
        return 0;
    }

    public boolean isLiked(int userId, int postId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(IS_LIKED)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка проверки лайка", e);
        }
        return false;
    }
}
