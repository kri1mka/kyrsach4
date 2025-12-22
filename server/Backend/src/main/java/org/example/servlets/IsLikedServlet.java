package org.example.servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.LikesDAO;
import org.example.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/home_is_liked")
public class IsLikedServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        LikeRequest body = gson.fromJson(req.getReader(), LikeRequest.class);

        try (Connection conn = DBConnection.getConnection()) {
            LikesDAO dao = new LikesDAO(conn);
            boolean isLiked = dao.isLiked(body.user_id, body.post_id);

            resp.setContentType("application/json");
            resp.getWriter().write(
                    "{\"is_liked\": " + isLiked + "}"
            );
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }

    static class LikeRequest {
        int post_id;
        int user_id;
    }
}
