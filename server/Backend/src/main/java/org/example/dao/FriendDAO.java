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

        // Берем данные из Friends + UsersInfo (имя/фамилия и avatarUrl)
        String sql = "SELECT f.id AS friend_id, f.user_id, f.country, " +
                "u.name, u.surname, ui.avatarUrl " +
                "FROM Friends f " +
                "JOIN Users u ON f.user_id = u.id " +
                "JOIN UsersInfo ui ON f.user_id = ui.user_id";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Friend friend = new Friend(
                        rs.getInt("friend_id"),
                        rs.getString("name"),       // вместо firstName
                        rs.getString("surname"),    // вместо lastName
                        rs.getString("country"),
                        rs.getString("avatarUrl")
                );

                friends.add(friend);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка чтения друзей из БД", e);
        }

        return friends;
    }
}
