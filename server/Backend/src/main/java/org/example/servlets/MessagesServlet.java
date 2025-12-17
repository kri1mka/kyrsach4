package org.example.servlets;

import com.google.gson.Gson;
import org.example.dao.MessageDAO;
import org.example.entity.Message;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/messages")
public class MessagesServlet extends HttpServlet {

    private MessageDAO messageDAO = new MessageDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Message> messages = messageDAO.findAll();
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(new Gson().toJson(messages));
    }
}
