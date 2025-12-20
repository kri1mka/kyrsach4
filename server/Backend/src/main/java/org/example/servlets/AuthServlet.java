package org.example.servlets;

import com.google.gson.Gson;
import org.example.dao.UserDAO;
import org.example.dao.UsersInfoDAO;
import org.example.entity.User;
import org.example.entity.UsersInfo;
import org.example.util.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private UserDAO userDAO;
    private Gson gson = new Gson();

    public AuthServlet() {
        try {
            this.userDAO = new UserDAO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("AuthServlet called: " + req.getRequestURI());

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getPathInfo();
        if (path == null) path = "/";

        switch (path) {
            case "/register":
                handleRegister(req, resp);
                break;
            case "/login":
                handleLogin(req, resp);
                break;
            case "/reset-password":
                handleResetPassword(req, resp);
                break;

            default:
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Неизвестное действие авторизации\"}");
        }
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> map = gson.fromJson(readBody(req), Map.class);

        String email = (String) map.get("email");
        String password = (String) map.get("password");
        String name = (String) map.get("name");
        String surname = (String) map.get("surname");
        String sex = (String) map.get("sex");
        String about = (String) map.get("about");
        String birthdateStr = (String) map.get("birthdate");

        // Проверка обязательных полей
        if (email == null || password == null || name == null || surname == null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Заполните обязательные поля\"}");
            return;
        }

        // Проверка email
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Неверный формат email\"}");
            return;
        }

        // Проверка уникальности email
        if (userDAO.findByEmail(email) != null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Email уже существует\"}");
            return;
        }

        // Создание User
        User user = new User(email, password, name, surname, null);
        try {
            userDAO.save(user);
        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Ошибка сохранения пользователя: " + e.getMessage() + "\"}");
            return;
        }

        // Создание UsersInfo
        UsersInfo info = new UsersInfo();
        info.setUserId(user.getId());
        info.setAbout((about != null && !about.isEmpty()) ? about : null);
        info.setSex((sex != null && !sex.isEmpty()) ? sex.toLowerCase() : null);

        if (birthdateStr != null && !birthdateStr.isEmpty()) {
            try {
                java.util.Date d = new SimpleDateFormat("yyyy-MM-dd").parse(birthdateStr);
                info.setDateOfBirth(new Date(d.getTime()));
            } catch (ParseException e) {
                resp.setStatus(400);
                resp.getWriter().write("{\"error\":\"Неверный формат даты, используйте yyyy-MM-dd\"}");
                return;
            }
        }

        try {
            new UsersInfoDAO().save(info);
        } catch (Exception e) {
            e.printStackTrace(); // ← СМОТРИ ЭТО В CONSOLE TOMCAT
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"DB error: " + e.getMessage() + "\"}");
            return;
        }


        resp.setStatus(201);
        resp.getWriter().write("{\"message\":\"ok\",\"userId\":" + user.getId() + "}");
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Map<String, Object> map = gson.fromJson(readBody(req), Map.class);

        String email = (String) map.get("email");
        String password = (String) map.get("password");

        System.out.println("LOGIN email=" + email + " password=" + password);

        if (email == null || password == null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"status\":\"error\",\"error\":\"Email и пароль обязательны\"}");
            return;
        }

        try {
            User user = userDAO.findByEmail(email);

            if (user == null || !user.getPassword().equals(password)) {
                resp.setStatus(401);
                resp.getWriter().write(
                        "{\"status\":\"error\",\"error\":\"Неверный email или пароль\"}"
                );
                return;
            }

            // УСПЕШНЫЙ ЛОГИН
            resp.setStatus(200);
            resp.getWriter().write(
                    "{\"status\":\"ok\",\"userId\":" + user.getId() + "}"
            );

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write(
                    "{\"status\":\"error\",\"error\":\"Ошибка сервера\"}"
            );
        }
    }

    private void handleResetPassword(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Map<String, Object> map = gson.fromJson(readBody(req), Map.class);

        String email = (String) map.get("email");
        String newPassword = (String) map.get("newPassword");

        if (email == null || newPassword == null || newPassword.length() < 6) {
            resp.setStatus(400);
            resp.getWriter().write(
                    "{\"error\":\"Некорректные данные\"}"
            );
            return;
        }

        try {
            User user = userDAO.findByEmail(email);

            if (user == null) {
                resp.setStatus(404);
                resp.getWriter().write(
                        "{\"error\":\"Пользователь не найден\"}"
                );
                return;
            }

            user.setPassword(newPassword);
            userDAO.updatePassword(user.getId(), newPassword);

            resp.setStatus(200);
            resp.getWriter().write(
                    "{\"message\":\"Пароль успешно изменён\"}"
            );

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write(
                    "{\"error\":\"Ошибка сервера\"}"
            );
        }
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
