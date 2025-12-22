package org.example.dao;

import org.example.entity.Message;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    public List<Message> getMessagesBetweenUsers(int user1, int user2) {
        List<Message> messages = new ArrayList<>();

        String sql =
                "SELECT m.id, m.from_user_id, m.to_user_id, m.message, m.created_at, " +
                        "u.name AS name, u.surname AS surname, ui.avatarUrl AS avatarUrl " +
                        "FROM Messages m " +
                        "JOIN Users u ON m.from_user_id = u.id " +
                        "LEFT JOIN UsersInfo ui ON ui.user_id = u.id " +
                        "WHERE (m.from_user_id = ? AND m.to_user_id = ?) " +
                        "   OR (m.from_user_id = ? AND m.to_user_id = ?) " +
                        "ORDER BY m.created_at ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, user1);
            stmt.setInt(2, user2);
            stmt.setInt(3, user2);
            stmt.setInt(4, user1);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(new Message(
                            rs.getInt("id"),
                            rs.getInt("from_user_id"),
                            rs.getInt("to_user_id"),
                            rs.getString("message"),
                            rs.getTimestamp("created_at"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("avatarUrl")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка чтения сообщений из БД", e);
        }

        return messages;
    }

    public void insertMessage(int fromUser, int toUser, String message) {
        String sql =
                "INSERT INTO Messages (from_user_id, to_user_id, message, created_at) " +
                        "VALUES (?, ?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fromUser);
            stmt.setInt(2, toUser);
            stmt.setString(3, message);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка добавления сообщения в БД", e);
        }
    }
}
