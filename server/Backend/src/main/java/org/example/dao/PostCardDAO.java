package org.example.dao;

import org.example.dto.PostDto;
import org.example.entity.PostCard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostCardDAO {

    private final Connection connection;

    public PostCardDAO(Connection connection) {
        this.connection = connection;
    }

    public List<PostDto> getFeed() {
        List<PostDto> list = new ArrayList<>();

        String sql = """
            SELECT 
                p.id,
                p.description,
                p.location,
                p.created_at,
                ph.photo_url AS photo_url,
                CONCAT(u.name, ' ', u.surname) AS user_name
            FROM PostCard p
            JOIN Users u ON p.user_id = u.id
            LEFT JOIN Photo ph ON p.photo_id = ph.id
            ORDER BY p.created_at DESC
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PostDto dto = new PostDto();
                dto.id = rs.getInt("id");
                dto.description = rs.getString("description");
                dto.location = rs.getString("location");
                dto.createdAt = rs.getTimestamp("created_at").toString();
                dto.photoUrl = rs.getString("photo_url");
                dto.userName = rs.getString("user_name");
                list.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки ленты", e);
        }

        return list;
    }


    private PostCard map(ResultSet rs) throws SQLException {
        PostCard card = new PostCard();
        card.setId(rs.getInt("id"));
        card.setUserId(rs.getInt("user_id"));
        card.setDescription(rs.getString("description"));
        card.setLocation(rs.getString("location"));
        card.setCreatedAt(rs.getTimestamp("created_at"));
        card.setPhotoIt(rs.getString("photo_it"));
        return card;
    }
}
