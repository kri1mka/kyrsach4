package org.example.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;

@WebServlet("/avatar")
public class AvatarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fileName = req.getParameter("file");
        if (fileName == null || fileName.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Не указан файл");
            return;
        }

        String avatarPath = "avatars/" + fileName;

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(avatarPath)) {
            if (is != null) {
                if (avatarPath.endsWith(".png")) resp.setContentType("image/png");
                else if (avatarPath.endsWith(".jpg") || avatarPath.endsWith(".jpeg")) resp.setContentType("image/jpeg");

                resp.getOutputStream().write(is.readAllBytes());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Файл не найден");
            }
        }
    }
}
