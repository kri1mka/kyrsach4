package org.example.daos;

import org.example.entities.TripCard;
import org.example.entities.User;
import org.example.entities.UserInfo;
import org.example.entities.Photo;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TripCardDAO {

    public List<TripCard> findAll() {
        List<TripCard> cards = new ArrayList<>();

        // SQL с JOIN на UsersInfo
        String sql = """
            SELECT 
                t.id AS trip_id,
                t.location AS location,
                t.start_date AS start_date,
                t.end_date AS end_date,
                t.price AS price,
                t.type AS type,
                t.description AS description,
                t.photo_it AS photo_file,

                u.id AS user_id,
                u.name AS name,
                u.surname AS surname,

                ui.id AS userinfo_id,
                ui.sex AS sex,
                ui.age AS age

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

                // User
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setInfo(userInfo); // связываем UserInfo с User

                // Photo
                Photo photo = new Photo();
                String photoFile = rs.getString("photo_file"); // имя файла из БД
                String photoUrl = "http://10.0.2.2:8080/Backend/images/" + photoFile; // формируем URL
                photo.setPhotoUrl(photoUrl);

                // Логируем URL картинки
                System.out.println("TripCard ID: " + rs.getInt("trip_id") + " | Photo URL: " + photoUrl);

                // TripCard
                TripCard card = new TripCard();
                card.setId(rs.getInt("trip_id"));
                card.setLocation(rs.getString("location"));
                card.setStartDate(rs.getDate("start_date"));
                card.setEndDate(rs.getDate("end_date"));
                card.setPrice(rs.getDouble("price"));
                card.setType(rs.getString("type"));
                card.setDescription(rs.getString("description"));
                card.setUser(user);
                card.setPhoto(photo);

                cards.add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка чтения TripCard из БД", e);
        }

        return cards;
    }
}
