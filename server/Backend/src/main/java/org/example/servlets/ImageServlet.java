package org.example.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {

    private static final String IMAGE_DIR = "/images/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String imageName = req.getPathInfo(); // /piter.jpg

        if (imageName == null || imageName.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String fullPath = getServletContext().getRealPath(IMAGE_DIR + imageName);

        File imageFile = new File(fullPath);

        if (!imageFile.exists()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = getServletContext().getMimeType(imageFile.getName());
        resp.setContentType(contentType != null ? contentType : "image/jpeg");
        resp.setContentLengthLong(imageFile.length());

        try (FileInputStream fis = new FileInputStream(imageFile);
             OutputStream os = resp.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }
}
