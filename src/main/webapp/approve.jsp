<%--
<%@ page import="model.Character" %>

  Created by IntelliJ IDEA.
  User: T.Gotz
  Date: 06.04.2024
  Time: 13:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Character" %>
<%@ page import="dao.CharacterDAO" %>
<%@ page import="java.lang.reflect.Array" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Base64" %>
<%@ page import="model.User" %>
<%
  //checking if user is logged in - if not - goodbye :D
  if (session != null && session.getAttribute("user") != null) {
%>
<%
  User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Česko-Slovenská databáze filmových postav</title>
  <link rel="stylesheet" href="styles/bootstrap.css">

  <!--Animate.css    -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
  <!--Vlastní css-->
  <link rel="stylesheet" href="styles/styles.css">
</head>

<body>
<header>
  <div class="container">
    <nav class="navbar navbar-dark navbar-expand-lg ">
      <div class="container-fluid">
        <a class="navbar-brand " href="index.jsp">
          <h1><img class="navbar-logo" src="images/logo.png" alt="logo"></h1>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
                aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNavDropdown">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="index.jsp">Domů</a>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link dropdown-toggle text-white" href="#" role="button" data-bs-toggle="dropdown"
                 aria-expanded="false">
                Žebříčky
              </a>
              <ul class="dropdown-menu ">
                <li><a class="dropdown-item" href="leaderBoard?sort=1">Nejoblíbenější</a></li>
                <li><a class="dropdown-item" href="leaderBoard?sort=2">Nejnenáviděnejší</a></li>
                <li><a class="dropdown-item" href="leaderBoard?sort=3">Nejatraktivější</a></li>
              </ul>
            </li>
            <li class="nav-item ">
              <a class="nav-link text-white" href="add_character.jsp">Přidat postavu</a>
            </li>
            <li class="nav-item ">
              <a class="nav-link text-white" href="approve">Schvalování postav</a>
            </li>
            <li class="nav-item ">
              <a class="nav-link text-white" href="dashboard">Administrace</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  </div>
</header>
<%
  //getting data about character and it's quotes
  if(request.getAttribute("character") != null){
    Character character = (Character)request.getAttribute("character");
    String base64Image = new String(Base64.getEncoder().encode(character.getImage()));
    ArrayList<String> quotesList = (ArrayList<String>) request.getAttribute("quoteList");
    int characterCount = (int) request.getAttribute("characterCount");


%>
<div class="container mt-5 bg-main pt-2">
  <div class="d-flex justify-content-between align-items-center">
    <a <% if(characterCount == 0){ %> class="disabled-link"<% }%> href="approve?CharacterCount=<%=characterCount - 1%>">Předchozí</a>
    <h2>Žádost o přidání</h2>
    <a <% if(characterCount == (Integer)request.getAttribute("maxCharacterCount") -1){ %> class="disabled-link"<% }%> href="approve?CharacterCount=<%=characterCount + 1%>">Další</a>
  </div>
  <hr>
  <div class="row">

    <div class="col-md-8">
      <h3>Jméno: <%=character.getName()%></h3>
      <% if(character.getNickname()!=null){ %>
      <p>Přezdívka: <%=character.getNickname()%></p>
      <%}%>
      <% if (character.getAge() != 0){%>
      <p>Věk: <%=character.getAge()%></p>
      <%}%>
      <p>Pohlaví: <%=character.getGender()%></p>
      <p>Typ: <%=character.getType()%></p>
      <p>Herec/dabér: <%=character.getActorName()%></p>
      <p>Název filmu: <%=character.getFilmName()%></p>
      <p><%=character.getDesc()%></p>
    </div>
    <div class="col-md-4">
      <img src="data:image/jpeg;base64,<%= base64Image %>" class="img-fluid" alt="Character Image">
    </div>
  </div>
  <% if(quotesList != null && quotesList.size() > 0){%>
  <h3>Hlášky</h3>
  <ul>
    <% for(int i = 0; i < quotesList.size(); i++){%>

    <li><%=quotesList.get(i)%></li>
    <%} %>
  </ul>
  <%}%>
  <hr>
<div class="d-flex justify-content-between">
  <a class="text-decoration-none" href="/delete-character?id=<%=character.getId()%>&CharacterCount=<%=characterCount%>">
    <span class="fs-2 p-2 text-danger">&#10008;</span>
  </a>
  <a class="text-decoration-none" href="/approve-character?id=<%=character.getId()%>&CharacterCount=<%=characterCount%>">
  <span class="fs-2 p-2 text-success">&#10004;</span>
  </a>
</div>
  <% } else{%>
    <p class="text-center mt-4">Aktuálně žádné postavy nečekají na schválení.</p>
  <%} %>
</div>
<!--Bootstrap-->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>

</html>
<%}else{
  response.sendRedirect("login.jsp");
}%>