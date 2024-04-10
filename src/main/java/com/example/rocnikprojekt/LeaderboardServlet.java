package com.example.rocnikprojekt;

import dao.CharacterDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
@WebServlet(name = "LeaderboardServlet", value = "/leaderBoard")
public class LeaderboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int LeaderBoardId = Integer.parseInt(request.getParameter("sort"));
        String orderBy;
        String headline;
        switch (LeaderBoardId){
            case 1:
                orderBy = "overall DESC";
                headline = "Nejoblíbenější postavy";
                break;
            case 2:
                orderBy = "overall";
                headline = "Nejnenáviděnější postavy";
                break;
            case 3:
                orderBy = "attractiveness DESC";
                headline = "Nejatraktivnější postavy";
                break;
            default:
                orderBy = "overall DESC";
                headline = "Nejoblíbenější postavy";

        }
        CharacterDAO characterDAO = new CharacterDAO();
        request.setAttribute("characterList", characterDAO.getCharactersLB(orderBy));
        request.setAttribute("headline", headline);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/leaderboard.jsp");
        dispatcher.forward(request, response);
    }


}