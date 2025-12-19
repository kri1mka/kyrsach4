package org.example.servlets;


import com.google.gson.Gson;
import org.example.daos.TripCardDAO;
import org.example.entities.TripCard;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/tripcards")
public class GetTripCardsServlet extends HttpServlet {

    private TripCardDAO dao = new TripCardDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        List<TripCard> cards = dao.findAll();

        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(cards));
    }
}
