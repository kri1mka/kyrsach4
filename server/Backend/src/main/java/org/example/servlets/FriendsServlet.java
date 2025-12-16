package org.example.servlets;

import com.google.gson.Gson;
import org.example.dao.FriendDAO;
import org.example.entity.Friend;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/friends")
public class FriendsServlet extends HttpServlet {

    private FriendDAO dao = new FriendDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Friend> friends = dao.findAll();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(friends));
        out.flush();
    }
}
