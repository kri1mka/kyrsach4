package org.example.servlets;

import com.google.gson.Gson;
import org.example.dao.PostCardDAO;
import org.example.dto.PostDto;
import org.example.util.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/posts")
public class HomePostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (Connection conn = DBConnection.getConnection()) {

            PostCardDAO dao = new PostCardDAO(conn);
            List<PostDto> posts = dao.getFeed();

            Gson gson = new Gson();
            resp.getWriter().write(gson.toJson(posts));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}

