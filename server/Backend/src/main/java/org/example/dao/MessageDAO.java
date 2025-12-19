package org.example.dao;

import org.example.entity.Message;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT m.id, m.from_user_id, m.to_user_id, m.message, m.created_at, " +
                "u.name AS name, u.surname AS surname, ui.avatarUrl " +
                "FROM Messages m " +
                "JOIN Users u ON m.from_user_id = u.id " +
                "LEFT JOIN UsersInfo ui ON u.id = ui.user_id " +
                "ORDER BY m.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Timestamp createdAt = rs.getTimestamp("created_at");

                Message msg = new Message(
                        rs.getInt("id"),
                        rs.getInt("from_user_id"),
                        rs.getInt("to_user_id"),
                        rs.getString("message"),
                        createdAt,
                        rs.getString("name"),      // вместо firstName
                        rs.getString("surname"),   // вместо lastName
                        rs.getString("avatarUrl")
                );
                messages.add(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка чтения сообщений из БД", e);
        }

        return messages;
    }
}
