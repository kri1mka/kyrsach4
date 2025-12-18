package org.example.servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
            List<PostCard> posts = postDAO.findByUserId(userId);

            // Формируем корректные URL фото и имя пользователя
            for (PostCard post : posts) {
                // Фотография
                if (post.getPhotoIt() != null && !post.getPhotoIt().isEmpty()) {
                    post.setPhotoIt("http://10.0.2.2:8080/Backend/images/" + post.getPhotoIt());
                }

                // Имя пользователя (если DAO его возвращает)
                if (post.getUserName() == null || post.getUserName().isEmpty()) {
                    // Если DAO возвращает только userId, можно запросить имя из базы
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
        String contentType = req.getContentType();

        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            handleUpload(req, resp);
        } else {
            // Обычный JSON POST
            PostCard post = gson.fromJson(req.getReader(), PostCard.class);
            if (post.getUserId() == null) {
                writeJson(resp, 400, Map.of("error", "userId is required"));
                return;
            }

            try {
                postDAO.save(post);
                writeJson(resp, 201, post);
            } catch (RuntimeException e) {
                writeJson(resp, 500, Map.of("error", e.getMessage()));
            }
        }
    }

    // Загрузка изображения
    private void handleUpload(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Part filePart = req.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            writeJson(resp, 400, Map.of("error", "File not found in request"));
            return;
        }

        String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
        File uploadDir = new File("images/");
        if (!uploadDir.exists()) uploadDir.mkdirs();

        File file = new File(uploadDir, fileName);
        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        writeJson(resp, 200, Map.of("fileName", fileName));
    }

    // Обновить существующий пост
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

    // Удалить пост
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
