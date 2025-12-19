package org.example.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import org.example.dao.TripCardDAO;
import org.example.entity.TripCard;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@WebServlet("/api/users/trips/*")
@MultipartConfig
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
        String pathInfo = req.getPathInfo(); //
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
            for (TripCard trip : trips) {
                if (trip.getPhotoIt() != null && !trip.getPhotoIt().isEmpty()) {
                    String photo = trip.getPhotoIt();
                    if (!photo.startsWith("http")) {
                        trip.setPhotoIt("http://10.0.2.2:8080/Backend/images/" + photo);
                    }
                }
            }

            writeJson(resp, 200, trips);
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
        }
    }


    // POST /api/users/trips — создать новую поездку
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            Part filePart = req.getPart("file");
            String originalName = null;
            if (filePart != null && filePart.getSize() > 0) {
                originalName = filePart.getSubmittedFileName();
                File uploadDir = new File("images/");
                if (!uploadDir.exists()) uploadDir.mkdirs();
                File file = new File(uploadDir, originalName);
                try (InputStream in = filePart.getInputStream()) {
                    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            int userId = Integer.parseInt(req.getParameter("user_id"));
            String location = req.getParameter("location");
            java.sql.Date startDate = java.sql.Date.valueOf(req.getParameter("startDate"));
            java.sql.Date endDate = java.sql.Date.valueOf(req.getParameter("endDate"));
            String type = req.getParameter("type");
            BigDecimal price = new BigDecimal(req.getParameter("price"));
            String description = req.getParameter("description");


            TripCard trip = new TripCard();
            trip.setUserId(userId);
            trip.setLocation(location);
            trip.setStartDate(new java.util.Date(startDate.getTime()));
            trip.setEndDate(new java.util.Date(endDate.getTime()));
            trip.setType(type);
            trip.setPrice(price);
            trip.setDescription(description);
            trip.setPhotoIt(originalName);

            tripDAO.save(trip);

            // Формируем JSON для ответа
            Map<String, Object> result = Map.of(
                    "id", trip.getId(),
                    "userId", trip.getUserId(),
                    "location", trip.getLocation(),
                    "startDate", trip.getStartDate(),
                    "endDate", trip.getEndDate(),
                    "type", trip.getType(),
                    "price", trip.getPrice(),
                    "description", trip.getDescription(),
                    "photoIt", "http://10.0.2.2:8080/Backend/images/" + originalName
            );

            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write(new Gson().toJson(result));

        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
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