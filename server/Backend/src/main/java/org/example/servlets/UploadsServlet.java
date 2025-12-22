package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/uploads/posts/*")
public class UploadsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedFile = req.getPathInfo(); // /pheket.jpg
        if (requestedFile == null || requestedFile.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = new File("uploads/posts", requestedFile.substring(1)); // убираем /
        if (!file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(file.getName());
        if (mime == null) mime = "application/octet-stream";
        resp.setContentType(mime);
        resp.setContentLengthLong(file.length());

        Files.copy(file.toPath(), resp.getOutputStream());
    }
}
