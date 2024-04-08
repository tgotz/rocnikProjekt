package com.example.rocnikprojekt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "filter", value = "/filter")
public class FilterServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(request.getParameter("page") != null){
            int pageNumber = Integer.parseInt(request.getParameter("page"));
            request.setAttribute("pageNumber", pageNumber);
        }


        //sql for selecting characters
        String sql = "SELECT postavy.idpostavy as id, postavy.typPostavy as typ, postavy.pohlavi as pohlavi, postavy.jmeno as jmeno, postavy.obrazek as obrazek, postavy.datumPridani as datum, filmy.nazevFilmu as nazevFilmu FROM postavy JOIN filmy ON filmy.idfilmu = postavy.idfilmu ";
        ArrayList<String> sqlWhere = new ArrayList<String>();

        //adding search option
        if(request.getParameter("search") != null && !request.getParameter("search").isEmpty() ){
                String search =request.getParameter("search");
                sqlWhere.add("(jmeno like '%" +  search + "%' OR nazevFilmu like '%" + search + "%')");

        }

        //adding select type option
        if( (request.getParameter("cartoon") == null) ^ request.getParameter("irl") == null){
            if(request.getParameter("cartoon") == null){
                sqlWhere.add(" typPostavy = 'Hraná'");
            } else{
                sqlWhere.add(" typPostavy = 'Animovaná'");
            }
        }

        //adding gender option
        //checks if at least one gender parameter is not selected and at least one is selected.
        if ((request.getParameter("male") == null || request.getParameter("female") == null || request.getParameter("other") == null) && (request.getParameter("male") != null || request.getParameter("female") != null || request.getParameter("other") != null)){
            String genderSqlWhere ="";
            if(request.getParameter("male") != null ){
                genderSqlWhere = "(pohlavi = 'Muž' ";
            }
            if(request.getParameter("female") != null ){
                if(genderSqlWhere == ""){
                    genderSqlWhere = "(pohlavi = 'Žena' ";
                } else{
                    genderSqlWhere += "OR pohlavi = 'Žena' ";
                }
            }
            if(request.getParameter("other") != null ){
                if(genderSqlWhere == ""){
                    genderSqlWhere = "(pohlavi = 'Jiné' ";
                } else{
                    genderSqlWhere += "OR pohlavi = 'Jiné' ";
                }
            }
            genderSqlWhere += ")";
            sqlWhere.add(genderSqlWhere);
        }

        //adding all the where condition to the sql
        if(!sqlWhere.isEmpty()){
            sql += " WHERE ";
            for(int i = 0; i < sqlWhere.size(); i++){
                if( i >= 1){
                    sql += " AND ";
                }
                sql += sqlWhere.get(i);
            }
        }

        //ading order by option
        if(request.getParameter("order") != null && !request.getParameter("order").isEmpty()){
            sql += " ORDER BY " + request.getParameter("order");
        }


        //sending new sql to index.jsp as attribute
        request.setAttribute("sql", sql);
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);

    }

    public void destroy() {
    }
}