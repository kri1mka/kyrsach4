package org.example.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.example.dao.TripCardDAO;
import org.example.entity.TripCard;

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
        } catch (Exception e) {
            throw new ServletException("Cannot initialize TripCardDAO", e);
        }
    }

    private void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(gson.toJson(body));
    }

    // GET /api/users/trips/{userId}
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

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

        List<TripCard> trips = tripDAO.findByUserId(userId);

        for (TripCard trip : trips) {
            if (trip.getPhotoIt() != null && !trip.getPhotoIt().isEmpty()) {
                trip.setPhotoIt(
                        "http://10.0.2.2:8080/Backend/images/" + trip.getPhotoIt()
                );
            }
        }

        writeJson(resp, 200, trips);
    }

    // POST /api/users/trips
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String savedFileName = null;

        Part filePart = req.getPart("file");
        if (filePart != null && filePart.getSize() > 0) {

            // 🔥 КЛЮЧЕВОЙ МОМЕНТ
            String uploadPath = getServletContext().getRealPath("/images");
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalName = filePart.getSubmittedFileName();
            savedFileName = System.currentTimeMillis() + "_" + originalName;

            File file = new File(uploadDir, savedFileName);
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
        trip.setPhotoIt(savedFileName);

        tripDAO.save(trip);

        writeJson(resp, 201, Map.of(
                "id", trip.getId(),
                "userId", trip.getUserId(),
                "location", trip.getLocation(),
                "startDate", trip.getStartDate(),
                "endDate", trip.getEndDate(),
                "type", trip.getType(),
                "price", trip.getPrice(),
                "description", trip.getDescription(),
                "photoIt",
                savedFileName == null ? null :
                        "http://10.0.2.2:8080/Backend/images/" + savedFileName
        ));
    }

    // DELETE /api/users/trips/{tripId}
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            writeJson(resp, 400, Map.of("error", "Trip ID is required"));
            return;
        }

        int tripId;
        try {
            tripId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            writeJson(resp, 400, Map.of("error", "Trip ID must be a number"));
            return;
        }

        tripDAO.delete(tripId);
        writeJson(resp, 200, Map.of("message", "Trip deleted successfully"));
    }
}
