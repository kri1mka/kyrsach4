package org.example.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

    private final MessageDAO messageDAO = new MessageDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Message> messages = messageDAO.findAll();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        resp.getWriter().write(gson.toJson(messages));
    }
}