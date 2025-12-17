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

        // Теперь берем данные прямо из Friends
        String sql = "SELECT f.id AS friend_id, f.user_id, f.country, f.avatarUrl, " +
                "u.name AS firstName, u.surname AS lastName " +
                "FROM Friends f " +
                "JOIN Users u ON f.user_id = u.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Friend friend = new Friend(
                        rs.getInt("friend_id"),           // id в таблице Friends
                        rs.getString("firstName"),        // имя пользователя из Users
                        rs.getString("lastName"),         // фамилия пользователя из Users
                        rs.getString("country"),          // страна из Friends
                        rs.getString("avatarUrl")         // аватар из Friends
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
