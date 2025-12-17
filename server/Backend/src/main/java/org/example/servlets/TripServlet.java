package org.example.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.dao.TripCardDAO;
import org.example.entity.TripCard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@WebServlet("/api/users/trips/*") // userId в конце URL
public class TripServlet extends HttpServlet {

    private TripCardDAO tripDAO;
    private final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = org.example.util.DBConnection.getConnection();
            tripDAO = new TripCardDAO(connection);
        } catch (RuntimeException e) {
            throw new ServletException("Cannot initialize DAO: " + e.getMessage(), e);
        }
    }

    private void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(status);
        resp.getWriter().write(gson.toJson(body));
    }

    // GET /api/users/trips/{userId} — получить все поездки пользователя
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // "/{userId}"
        if (pathInfo == null || pathInfo.equals("/")) {
            writeJson(resp, 400, Map.of("error", "User ID is required"));
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            writeJson(resp, 400, Map.of("error", "User ID must be a number"));
            return;
        }

        try {
            List<TripCard> trips = tripDAO.findByUserId(userId);

            // Формируем URL фото и передаём даты
            for (TripCard trip : trips) {
                if (trip.getPhotoIt() != null && !trip.getPhotoIt().isEmpty()) {
                    trip.setPhotoIt("http://10.0.2.2:8080/Backend/images/" + trip.getPhotoIt());
                }
            }

            writeJson(resp, 200, trips);
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
        }
    }

    // POST /api/users/trips — создать новую поездку
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TripCard trip = gson.fromJson(req.getReader(), TripCard.class);
        if (trip.getUserId() == null) {
            writeJson(resp, 400, Map.of("error", "userId is required"));
            return;
        }

        try {
            tripDAO.save(trip);
            writeJson(resp, 201, trip);
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/users/trips — обновить существующую поездку
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TripCard trip = gson.fromJson(req.getReader(), TripCard.class);
        if (trip.getId() == null) {
            writeJson(resp, 400, Map.of("error", "Trip ID is required"));
            return;
        }

        TripCard existing;
        try {
            existing = tripDAO.findById(trip.getId());
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
            return;
        }

        if (existing == null) {
            writeJson(resp, 404, Map.of("error", "Trip not found"));
            return;
        }

        try {
            tripDAO.update(trip);
            writeJson(resp, 200, trip);
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/users/trips/{tripId} — удалить поездку
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // "/{tripId}"
        if (pathInfo == null || pathInfo.equals("/")) {
            writeJson(resp, 400, Map.of("error", "Trip ID is required"));
            return;
        }

        String[] parts = pathInfo.split("/");
        if (parts.length < 2) {
            writeJson(resp, 400, Map.of("error", "Invalid URL format"));
            return;
        }

        int tripId;
        try {
            tripId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            writeJson(resp, 400, Map.of("error", "Trip ID must be a number"));
            return;
        }

        TripCard existing;
        try {
            existing = tripDAO.findById(tripId);
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
            return;
        }

        if (existing == null) {
            writeJson(resp, 404, Map.of("error", "Trip not found"));
            return;
        }

        try {
            tripDAO.delete(tripId);
            writeJson(resp, 200, Map.of("message", "Trip deleted successfully"));
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
        }
    }

    // Фильтры по параметрам
    private List<TripCard> applyFilters(List<TripCard> trips, Map<String, String[]> params) {
        return trips.stream().filter(trip -> {
            if (params.containsKey("type")) {
                String filter = params.get("type")[0];
                if (filter != null && !filter.isEmpty() &&
                        (trip.getType() == null || !trip.getType().toLowerCase().contains(filter.toLowerCase())))
                    return false;
            }

            if (params.containsKey("location")) {
                String filter = params.get("location")[0];
                if (filter != null && !filter.isEmpty() &&
                        (trip.getLocation() == null || !trip.getLocation().toLowerCase().contains(filter.toLowerCase())))
                    return false;
            }

            if (params.containsKey("minPrice")) {
                try {
                    BigDecimal min = new BigDecimal(params.get("minPrice")[0]);
                    if (trip.getPrice() == null || trip.getPrice().compareTo(min) < 0) return false;
                } catch (NumberFormatException ignored) {}
            }

            if (params.containsKey("maxPrice")) {
                try {
                    BigDecimal max = new BigDecimal(params.get("maxPrice")[0]);
                    if (trip.getPrice() == null || trip.getPrice().compareTo(max) > 0) return false;
                } catch (NumberFormatException ignored) {}
            }

            return true;
        }).toList();
    }
}
