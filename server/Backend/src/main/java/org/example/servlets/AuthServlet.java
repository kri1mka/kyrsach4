package org.example.servlets;

import com.google.gson.Gson;
import org.example.dao.UserDAO;
import org.example.entity.User;
import org.example.util.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {
    private UserDAO userDAO;
    private Gson gson = new Gson();

    public AuthServlet() {
        try {
            Connection conn = DBConnection.getConnection();
            this.userDAO = new UserDAO(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        if (path == null) path = "/";

        if (path.equals("/register")) {
            handleRegister(req, resp);
        } else if (path.equals("/login")) {
            handleLogin(req, resp);
        } else {
            resp.setStatus(404);
            resp.getWriter().write("{\"error\":\"Неизвестное действие авторизации\"}");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = readBody(req);
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if ((email == null || password == null) && body != null && !body.isEmpty()) {
            try {
                Map<String, Object> map = gson.fromJson(body, Map.class);
                if (map != null) {
                    if (email == null) email = (String) map.get("email");
                    if (password == null) password = (String) map.get("password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (email == null || password == null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Необходимо указать email и пароль\"}");
            return;
        }

        if (userDAO.findByEmail(email) != null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Пользователь с таким email уже существует\"}");
            return;
        }

        User user = new User(email, password, "", "", "");
        userDAO.save(user);

        resp.setStatus(201);
        resp.getWriter().write("{\"message\":\"Пользователь успешно создан\",\"id\":" + user.getId() + "}");
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = readBody(req);
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if ((email == null || password == null) && body != null && !body.isEmpty()) {
            try {
                Map<String, Object> map = gson.fromJson(body, Map.class);
                if (map != null) {
                    if (email == null) email = (String) map.get("email");
                    if (password == null) password = (String) map.get("password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (email == null || password == null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Необходимо указать email и пароль\"}");
            return;
        }

        User user = userDAO.findByEmail(email);
        if (user == null || !password.equals(user.getPassword())) {
            resp.setStatus(401);
            resp.getWriter().write("{\"error\":\"Неверный email или пароль\"}");
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userEmail", user.getEmail());

        resp.setStatus(200);
        resp.getWriter().write("{\"message\":\"Авторизация успешна\",\"userId\":" + user.getId() +
                ",\"email\":\"" + user.getEmail() + "\"}");
    }

    private String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}
