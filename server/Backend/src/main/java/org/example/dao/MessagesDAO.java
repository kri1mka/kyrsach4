package org.example.dao;

import org.example.entity.Messages;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessagesDAO {

    private static final String INSERT =
            "INSERT INTO Messages (from_user_id, to_user_id, message, created_at) VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_ID =
            "SELECT * FROM Messages WHERE id = ?";

    private static final String FIND_ALL =
            "SELECT * FROM Messages";

    private static final String DELETE =
            "DELETE FROM Messages WHERE id=?";

    private static final String DIALOG =
            """
            SELECT * FROM Messages 
            WHERE (from_user_id=? AND to_user_id=?) 
               OR (from_user_id=? AND to_user_id=?) 
            ORDER BY created_at
            """;

    private static final String INBOX =
            "SELECT * FROM Messages WHERE to_user_id=? ORDER BY created_at DESC";

    // SAVE
    public void save(Messages msg) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, msg.getFrom_user_id());
            stmt.setInt(2, msg.getTo_user_id());
            stmt.setString(3, msg.getMessage());
            stmt.setTimestamp(4, new Timestamp(msg.getCreated_at().getTime()));

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) msg.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения сообщения", e);
        }
    }

    // FIND BY ID
    public Messages findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска сообщения", e);
        }
        return null;
    }

    // FIND ALL
    public List<Messages> findAll() {
        List<Messages> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения сообщений", e);
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
            throw new RuntimeException("Ошибка удаления сообщения", e);
        }
    }

    // EXTRA: GET DIALOG
    public List<Messages> getDialog(int u1, int u2) {
        List<Messages> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DIALOG)) {

            stmt.setInt(1, u1);
            stmt.setInt(2, u2);
            stmt.setInt(3, u2);
            stmt.setInt(4, u1);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения диалога", e);
        }

        return list;
    }

    // EXTRA: GET INBOX
    public List<Messages> getInbox(int userId) {
        List<Messages> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INBOX)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения входящих сообщений", e);
        }

        return list;
    }

    // MAPPING
    private Messages map(ResultSet rs) throws SQLException {
        Messages msg = new Messages();
        msg.setId(rs.getInt("id"));
        msg.setFrom_user_id(rs.getInt("from_user_id"));
        msg.setTo_user_id(rs.getInt("to_user_id"));
        msg.setMessage(rs.getString("message"));
        msg.setCreated_at(new java.util.Date(rs.getTimestamp("created_at").getTime()));
        return msg;
    }
}
