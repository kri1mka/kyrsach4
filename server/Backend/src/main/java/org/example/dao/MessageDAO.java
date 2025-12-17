package org.example.dao;

import org.example.entity.Message;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT m.id, m.from_user_id, m.to_user_id, m.message, m.created_at, " +
                "u.name AS firstName, u.surname AS lastName, ui.avatarUrl " +
                "FROM Messages m " +
                "JOIN Users u ON m.from_user_id = u.id " +
                "LEFT JOIN UsersInfo ui ON u.id = ui.user_id " +  // присоединяем UsersInfo для аватара
                "ORDER BY m.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Message msg = new Message(
                        rs.getInt("id"),
                        rs.getInt("from_user_id"),
                        rs.getInt("to_user_id"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("avatarUrl") // теперь берём из UsersInfo
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
