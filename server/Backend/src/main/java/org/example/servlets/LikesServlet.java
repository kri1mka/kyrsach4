package org.example.servlets;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.LikesDAO;
import org.example.dto.LikeResponse;
import org.example.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/home_likes")
public class LikesServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        LikeRequest body = gson.fromJson(req.getReader(), LikeRequest.class);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (body == null || body.post_id <= 0 || body.user_id <= 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(
                    gson.toJson(new LikeResponse(false, 0, false))
            );
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            LikesDAO dao = new LikesDAO(conn);

            //уже лайкнут?
            boolean alreadyLiked = dao.isLiked(body.user_id, body.post_id);

            boolean added = false;
            if (!alreadyLiked) {
                added = dao.addLike(body.post_id, body.user_id);
            }

            int likesCount = dao.countLikes(body.post_id);

            LikeResponse response = new LikeResponse(
                    added,
                    likesCount,
                    true // после запроса лайк ТОЧНО есть
            );

            resp.getWriter().write(gson.toJson(response));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(
                    gson.toJson(new LikeResponse(false, 0, false))
            );
        }
    }

    static class LikeRequest {
        int post_id;
        int user_id;
    }
}
