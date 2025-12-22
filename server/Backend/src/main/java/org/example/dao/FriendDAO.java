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

        String sql = "SELECT u.id, u.name, u.surname, ui.city, ui.avatarUrl " +
                "FROM Users u " +
                "LEFT JOIN UsersInfo ui ON u.id = ui.user_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Friend friend = new Friend(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("city") != null ? rs.getString("city") : "",       // country
                        rs.getString("avatarUrl") != null ? rs.getString("avatarUrl") : "" // avatar
                );
                friends.add(friend);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка загрузки друзей из БД", e);
        }

        return friends;
    }
}
