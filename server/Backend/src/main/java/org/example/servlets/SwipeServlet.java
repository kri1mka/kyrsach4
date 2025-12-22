package org.example.servlets;

import com.google.gson.Gson;
import org.example.dao.FriendDAO;
import org.example.entity.Friend;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/swipe")
public class SwipeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int cardId = Integer.parseInt(req.getParameter("card_id"));
        boolean liked = Boolean.parseBoolean(req.getParameter("liked"));

        // здесь можно сохранить в таблицу likes
        System.out.println("Card " + cardId + " liked=" + liked);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}