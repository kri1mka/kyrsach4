package org.example.servlets;

import com.google.gson.Gson;
import org.example.dao.ChatDAO;
import org.example.entity.Message;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private ChatDAO dao = new ChatDAO(); // DAO сам подключается к БД

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String user1Str = req.getParameter("user1");
        String user2Str = req.getParameter("user2");

        if(user1Str == null || user2Str == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing user parameters");
            return;
        }

        int user1 = Integer.parseInt(user1Str);
        int user2 = Integer.parseInt(user2Str);

        List<Message> messages = dao.getMessagesBetweenUsers(user1, user2);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        out.print(new Gson().toJson(messages));
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int fromUser = Integer.parseInt(req.getParameter("fromUser"));
        int toUser = Integer.parseInt(req.getParameter("toUser"));
        String message = req.getParameter("message");

        dao.insertMessage(fromUser, toUser, message);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
