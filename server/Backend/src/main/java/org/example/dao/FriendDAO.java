package org.example.dao;

import org.example.entity.Friend;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {

    public List<Friend> findAll() {
        List<Friend> friends = new ArrayList<>();

        String sql = "SELECT id, firstName, lastName, country, avatarUrl FROM friends";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Friend friend = new Friend(
                        rs.getInt("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("country"),
                        rs.getString("avatarUrl")
                );
                friends.add(friend);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Можно пробросить RuntimeException, если нужно, чтобы сервлет увидел ошибку
            throw new RuntimeException("Ошибка чтения друзей из БД", e);
        }

        return friends;
    }
}
