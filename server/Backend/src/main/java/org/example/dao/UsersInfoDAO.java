package org.example.dao;

import org.example.entity.UsersInfo;
import org.example.util.DBConnection;

import java.sql.*;

public class UsersInfoDAO {

    private static final String INSERT =
            "INSERT INTO UsersInfo (user_id, date_of_birth, sex, about) VALUES (?, ?, ?, ?)";

    public void save(UsersInfo info) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {

            ps.setInt(1, info.getUserId());
            if (info.getDateOfBirth() != null) {
                ps.setDate(2, info.getDateOfBirth());
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setString(3, info.getSex());
            ps.setString(4, info.getAbout());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения UsersInfo", e);
        }
    }
}
