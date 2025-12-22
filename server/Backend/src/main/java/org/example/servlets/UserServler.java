package org.example.servlets;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import org.example.dao.PostCardDAO;
import org.example.dao.TripCardDAO;
import org.example.dao.UserDAO;
import org.example.dao.UsersInfoDAO;
import org.example.dto.UpdateProfileRequest;
import org.example.entity.User;
import org.example.entity.UsersInfo;


import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


@WebServlet("/api/users/*")
public class UserServler extends HttpServlet {

    private UserDAO userDao;
    @Override
    public void init() throws ServletException {
        try {
            userDao = new UserDAO();
        } catch (RuntimeException e) {
            throw new ServletException("Cannot initialize DAO: " + e.getMessage(), e);
        }
    }

    private final UsersInfoDAO usersInfoDAO = new UsersInfoDAO();
    private final Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");


        Map<String, Object> result = new HashMap<>();
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.setStatus(400);
                result.put("error", "User ID is required");
                resp.getWriter().print(gson.toJson(result));
                return;
            }


            int userId = Integer.parseInt(pathInfo.substring(1));


            User user = userDao.findById(userId);
            if (user == null) {
                resp.setStatus(404);
                result.put("error", "User not found");
                resp.getWriter().print(gson.toJson(result));
                return;
            }


            UsersInfo info = usersInfoDAO.findByUserId(userId);


            result.put("id", user.getId());
            result.put("name", user.getName());
            result.put("surname", user.getSurname());
            result.put("email", user.getEmail());
            result.put("phoneNumber", user.getPhoneNumber());


            if (info != null) {
                result.put("age", info.getAge());
                result.put("interests", info.getInterests());
                result.put("about", info.getAbout());
                result.put("location", info.getCity());
                result.put("travelType", info.getTravelType());

                if (info.getAvatarUrl() != null && !info.getAvatarUrl().isEmpty()) {
                    result.put(
                            "photo",
                            "http://10.0.2.2:8080/Backend/images/" + info.getAvatarUrl()
                    );
                } else {
                    result.put("photo", null);
                }
            }


            resp.getWriter().print(gson.toJson(result));
        } catch (Exception e) {
            resp.setStatus(500);
            result.put("error", e.getMessage());
            resp.getWriter().print(gson.toJson(result));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> result = new HashMap<>();
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                resp.setStatus(400);
                result.put("error", "User ID is required");
                resp.getWriter().print(gson.toJson(result));
                return;
            }

            int userId = Integer.parseInt(pathInfo.substring(1));
            UsersInfo info = usersInfoDAO.findByUserId(userId);

            if (info == null) {
                resp.setStatus(404);
                result.put("error", "User not found");
                resp.getWriter().print(gson.toJson(result));
                return;
            }

            // Читаем тело запроса
            UpdateProfileRequest request = gson.fromJson(req.getReader(), UpdateProfileRequest.class);

            if (request.getAge() != null) info.setAge(Integer.parseInt(request.getAge()));
            if (request.getLocation() != null) info.setCity(request.getLocation());
            if (request.getTravelType() != null) info.setTravelType(request.getTravelType());
            if (request.getPhoto() != null) info.setAvatarUrl(request.getPhoto());

            User user = userDao.findById(userId);
            if (user != null) {
                if (request.getName() != null) user.setName(request.getName());
                if (request.getSurname() != null) user.setSurname(request.getSurname());
                userDao.update(user);
            }

            usersInfoDAO.update(info);

            resp.setStatus(200);
            result.put("message", "Profile updated successfully");
            resp.getWriter().print(gson.toJson(result));
        } catch (Exception e) {
            resp.setStatus(500);
            result.put("error", e.getMessage());
            resp.getWriter().print(gson.toJson(result));
        }
    }

}