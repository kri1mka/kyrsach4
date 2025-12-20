package org.example.dao;

import org.example.entity.UsersInfo;
import org.example.util.DBConnection;

import java.sql.*;

public class UsersInfoDAO {

    private static final String REGISTER_INSERT =
            "INSERT INTO UsersInfo (user_id, date_of_birth, sex, about) VALUES (?, ?, ?, ?)";

    private static final String INSERT = "INSERT INTO UsersInfo (user_id, date_of_birth, sex, city, travel_type, avatarUrl, age, interests, about) VALUES (?, ?, ?, ?, ?, ?, ?, ? , ?)";
    private static final String FIND_BY_USER_ID = "SELECT * FROM UsersInfo WHERE user_id = ?";
    private static final String UPDATE = "UPDATE UsersInfo SET date_of_birth=?, sex=?, age=?, interests=?, about=?, city=?, travel_type=?, avatarUrl=? WHERE id=?";

    public void save(UsersInfo info) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(REGISTER_INSERT)) {

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

    public UsersInfo findByUserId(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_USER_ID)) {


            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска UsersInfo", e);
        }
        return null;
    }

    public void update(UsersInfo info) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setDate(1, info.getDateOfBirth() != null
                    ? new java.sql.Date(info.getDateOfBirth().getTime())
                    : null);

            stmt.setString(2, info.getSex());
            stmt.setInt(3, info.getAge());
            stmt.setString(4, info.getInterests());
            stmt.setString(5, info.getAbout());
            stmt.setString(6, info.getCity());
            stmt.setString(7, info.getTravelType());
            stmt.setString(8, info.getAvatarUrl());

            stmt.setInt(9, info.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления UsersInfo", e);
        }
    }


    private UsersInfo map(ResultSet rs) throws SQLException {
        UsersInfo info = new UsersInfo();
        info.setId(rs.getInt("id"));
        info.setUserId(rs.getInt("user_id"));
        info.setDateOfBirth(rs.getDate("date_of_birth"));
        info.setSex(rs.getString("sex"));
        info.setAge(rs.getInt("age"));
        info.setInterests(rs.getString("interests"));
        info.setAbout(rs.getString("about"));
        info.setCity(rs.getString("city"));
        info.setTravelType(rs.getString("travel_type"));
        info.setAvatarUrl(rs.getString("avatarUrl"));
        return info;
    }
}


