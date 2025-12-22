package org.example.servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import org.example.dao.PostCardDAO;
import org.example.entity.PostCard;
import org.example.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@WebServlet("/api/posts/*")
@MultipartConfig
public class PostServlet extends HttpServlet {

    private PostCardDAO postDAO;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getConnection();
            postDAO = new PostCardDAO(connection);
        } catch (RuntimeException e) {
            throw new ServletException("Cannot initialize DAO: " + e.getMessage(), e);
        }
    }

    private void writeJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.setStatus(status);
        resp.getWriter().write(gson.toJson(body));
    }

    // Получить все посты пользователя
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
            List<PostCard> posts = postDAO.findByUserId(userId);

            for (PostCard post : posts) {
                if (post.getPhotoIt() != null && !post.getPhotoIt().isEmpty()) {
                    post.setPhotoIt("http://10.0.2.2:8080/Backend/images/" + post.getPhotoIt());
                }

                if (post.getUserName() == null || post.getUserName().isEmpty()) {
                    String fullName = postDAO.getUserFullName(post.getUserId());
                    post.setUserName(fullName != null ? fullName : "Пользователь");
                }
            }

            writeJson(resp, 200, posts);
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
        }
    }

    // Создать новый пост
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {

            Part filePart = req.getPart("file");
            String savedFileName = null;
            if (filePart != null && filePart.getSize() > 0) {
                String uploadPath = getServletContext().getRealPath("/images");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                savedFileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                File file = new File(uploadDir, savedFileName);
                try (InputStream in = filePart.getInputStream()) {
                    Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }

            int userId = Integer.parseInt(req.getParameter("user_id"));
            String location = req.getParameter("location");
            String description = req.getParameter("description");

            PostCard post = new PostCard();
            post.setUserId(userId);
            post.setLocation(location);
            post.setDescription(description);
            post.setPhotoIt(savedFileName);

            postDAO.save(post);

            writeJson(resp, 201, post);
        } else {
            // старый способ через JSON
            PostCard post = gson.fromJson(req.getReader(), PostCard.class);
            postDAO.save(post);
            writeJson(resp, 201, post);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PostCard post = gson.fromJson(req.getReader(), PostCard.class);
        if (post.getId() == null) {
            writeJson(resp, 400, Map.of("error", "Post ID is required"));
            return;
        }

        PostCard existing;
        try {
            existing = postDAO.findById(post.getId());
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
            return;
        }

        if (existing == null) {
            writeJson(resp, 404, Map.of("error", "Post not found"));
            return;
        }

        try {
            postDAO.update(post);
            writeJson(resp, 200, post);
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // "/{postId}"
        if (pathInfo == null || pathInfo.equals("/")) {
            writeJson(resp, 400, Map.of("error", "Post ID is required"));
            return;
        }

        String[] parts = pathInfo.split("/");
        if (parts.length < 2) {
            writeJson(resp, 400, Map.of("error", "Invalid URL format"));
            return;
        }

        int postId;
        try {
            postId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            writeJson(resp, 400, Map.of("error", "Post ID must be a number"));
            return;
        }

        PostCard existing;
        try {
            existing = postDAO.findById(postId);
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
            return;
        }

        if (existing == null) {
            writeJson(resp, 404, Map.of("error", "Post not found"));
            return;
        }

        try {
            postDAO.delete(postId);
            writeJson(resp, 200, Map.of("message", "Post deleted successfully"));
        } catch (RuntimeException e) {
            writeJson(resp, 500, Map.of("error", e.getMessage()));
        }
    }
}
