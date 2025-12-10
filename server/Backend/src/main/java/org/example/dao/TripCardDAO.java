package org.example.dao;

import org.example.entity.TripCard;
import org.example.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripCardDAO {

    private static final String INSERT =
            "INSERT INTO TripCard (user_id, location, start_date, end_date, price, type, description, photo_it) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID = "SELECT * FROM TripCard WHERE id = ?";
    private static final String FIND_BY_USER = "SELECT * FROM TripCard WHERE user_id = ?";
    private static final String FIND_BY_TYPE = "SELECT * FROM TripCard WHERE type = ?";
    private static final String FIND_ALL = "SELECT * FROM TripCard";
    private static final String UPDATE =
            "UPDATE TripCard SET location=?, start_date=?, end_date=?, price=?, type=?, description=?, photo_it=? WHERE id=?";
    private static final String DELETE = "DELETE FROM TripCard WHERE id=?";

    public void save(TripCard card) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, card.getUser_id());
            stmt.setString(2, card.getLocation());
            stmt.setDate(3, new java.sql.Date(card.getStart_date().getTime()));
            stmt.setDate(4, new java.sql.Date(card.getEnd_date().getTime()));
            stmt.setDouble(5, card.getPrice());
            stmt.setString(6, card.getType());
            stmt.setString(7, card.getDescription());
            stmt.setString(8, card.getPhoto_it());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) card.setId(rs.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка сохранения TripCard", e);
        }
    }

    public TripCard findById(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска TripCard", e);
        }
        return null;
    }

    public List<TripCard> findByUserId(int userId) {
        List<TripCard> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_USER)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска TripCard по user_id", e);
        }
        return list;
    }

    public List<TripCard> findByType(String type) {
        List<TripCard> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_TYPE)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска TripCard по типу", e);
        }
        return list;
    }

    public List<TripCard> findAll() {
        List<TripCard> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения всех TripCard", e);
        }
        return list;
    }

    public void update(TripCard card) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, card.getLocation());
            stmt.setDate(2, new java.sql.Date(card.getStart_date().getTime()));
            stmt.setDate(3, new java.sql.Date(card.getEnd_date().getTime()));
            stmt.setDouble(4, card.getPrice());
            stmt.setString(5, card.getType());
            stmt.setString(6, card.getDescription());
            stmt.setString(7, card.getPhoto_it());
            stmt.setInt(8, card.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления TripCard", e);
        }
    }

    public void delete(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления TripCard", e);
        }
    }

    private TripCard map(ResultSet rs) throws SQLException {
        TripCard card = new TripCard();
        card.setId(rs.getInt("id"));
        card.setUser_id(rs.getInt("user_id"));
        card.setLocation(rs.getString("location"));
        card.setStart_date(rs.getDate("start_date"));
        card.setEnd_date(rs.getDate("end_date"));
        card.setPrice(rs.getDouble("price"));
        card.setType(rs.getString("type"));
        card.setDescription(rs.getString("description"));
        card.setCreated_at(rs.getTimestamp("created_at"));
        card.setPhoto_it(rs.getString("photo_it"));
        return card;
    }
}
