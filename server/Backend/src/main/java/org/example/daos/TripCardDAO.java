package org.example.daos;

import org.example.entities.TripCard;
import org.example.entities.User;
import org.example.entities.UserInfo;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TripCardDAO {

    public List<TripCard> findAll() {
        List<TripCard> cards = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // формат даты для Android

        String sql = """
            SELECT 
                t.id AS trip_id,
                t.location AS location,
                t.start_date AS start_date,
                t.end_date AS end_date,
                t.price AS price,
                t.type AS type,
                t.description AS description,

                u.id AS user_id,
                u.name AS name,
                u.surname AS surname,

                ui.id AS userinfo_id,
                ui.sex AS sex,
                ui.age AS age,
                ui.avatarUrl AS avatar_url

            FROM TripCard t
            JOIN Users u ON t.user_id = u.id
            LEFT JOIN UsersInfo ui ON u.id = ui.user_id
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // UserInfo
                UserInfo userInfo = new UserInfo();
                userInfo.setId(rs.getInt("userinfo_id"));
                userInfo.setUserId(rs.getInt("user_id"));
                userInfo.setSex(rs.getString("sex"));
                userInfo.setAge(rs.getInt("age"));
                userInfo.setAvatarUrl(rs.getString("avatar_url"));

                // User
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setInfo(userInfo);

                // TripCard
                TripCard card = new TripCard();
                card.setId(rs.getInt("trip_id"));
                card.setLocation(rs.getString("location"));
                card.setStartDate(rs.getDate("start_date") != null ? sdf.format(rs.getDate("start_date")) : null);
                card.setEndDate(rs.getDate("end_date") != null ? sdf.format(rs.getDate("end_date")) : null);
                card.setPrice(rs.getDouble("price"));
                card.setType(rs.getString("type"));
                card.setDescription(rs.getString("description"));
                card.setUser(user);
                String avatarFile = rs.getString("avatar_url");
                if (avatarFile != null && !avatarFile.isEmpty()) {
                    userInfo.setAvatarUrl("http://10.0.2.2:8080/Backend/images/" + avatarFile);
                }

                cards.add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка чтения TripCard из БД", e);
        }

        return cards;
    }
}
