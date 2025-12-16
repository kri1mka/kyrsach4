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

        String sql = "SELECT id, first_name, last_name, country, avatar_url FROM friends";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Friend friend = new Friend(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("country"),
                        rs.getString("avatar_url")
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
