package org.example.dao;

import org.example.entity.TripCard;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripCardDAO {

    private final Connection connection;

    private static final String INSERT =
            "INSERT INTO TripCard (user_id, location, start_date, end_date, price, type, description, photo_it) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM TripCard WHERE id = ?";
    private static final String FIND_BY_USER = "SELECT * FROM TripCard WHERE user_id = ?";
    private static final String FIND_ALL = "SELECT * FROM TripCard";
    private static final String UPDATE =
            "UPDATE TripCard SET location=?, start_date=?, end_date=?, price=?, type=?, description=?, photo_it=? WHERE id=?";
    private static final String DELETE = "DELETE FROM TripCard WHERE id=?";

    public TripCardDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(TripCard card) {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, card.getUserId());
            stmt.setString(2, card.getLocation());
            stmt.setDate(3, card.getStartDate() != null ? new java.sql.Date(card.getStartDate().getTime()) : null);
            stmt.setDate(4, card.getEndDate() != null ? new java.sql.Date(card.getEndDate().getTime()) : null);
            stmt.setBigDecimal(5, card.getPrice());
            stmt.setString(6, card.getType());
            stmt.setString(7, card.getDescription());
            stmt.setString(8, card.getPhotoIt());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) card.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving TripCard", e);
        }
    }

    public TripCard findById(int id) {
        try (PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return map(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding TripCard by id", e);
        }
    }

    public List<TripCard> findByUserId(int userId) {
        List<TripCard> list = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(FIND_BY_USER)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding TripCards by user_id", e);
        }
    }

    public void update(TripCard card) {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
            stmt.setString(1, card.getLocation());
            stmt.setDate(2, card.getStartDate() != null ? new java.sql.Date(card.getStartDate().getTime()) : null);
            stmt.setDate(3, card.getEndDate() != null ? new java.sql.Date(card.getEndDate().getTime()) : null);
            stmt.setBigDecimal(4, card.getPrice());
            stmt.setString(5, card.getType());
            stmt.setString(6, card.getDescription());
            stmt.setString(7, card.getPhotoIt());
            stmt.setInt(8, card.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating TripCard", e);
        }
    }

    public void delete(int id) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting TripCard", e);
        }
    }

    private TripCard map(ResultSet rs) throws SQLException {
        TripCard card = new TripCard();
        card.setId(rs.getInt("id"));
        card.setUserId(rs.getInt("user_id"));
        card.setLocation(rs.getString("location"));

        Date start = rs.getDate("start_date");
        if (start != null) card.setStartDate(new Date(start.getTime()));

        Date end = rs.getDate("end_date");
        if (end != null) card.setEndDate(new Date(end.getTime()));

        card.setPrice(rs.getBigDecimal("price"));
        card.setType(rs.getString("type"));
        card.setDescription(rs.getString("description"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) card.setCreatedAt(new Date(created.getTime()));

        card.setPhotoIt(rs.getString("photo_it"));
        return card;
    }
}
