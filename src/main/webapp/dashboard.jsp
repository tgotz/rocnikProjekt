<%--
  Created by IntelliJ IDEA.
  User: Tomas
  Date: 22.04.2024
  Time: 10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Character" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  //checking if user is logged in - if not - goodbye :D
  if (session != null && session.getAttribute("user") != null) {
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
  <!--custom css-->
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

    <h2 class="mt-5 mb-5 outline-heading">Administrace</h2>
  </div>
</header>

<div class="container bg-main pt-2">
  <form class="my-3" method="GET" action="/dashboard">
    <div class="form-group">
      <input type="text" class="form-control" name="search" placeholder="Jméno postavy / filmu / seriálu">
    </div>
  </form>
  <table class="table table-striped mb-3">
    <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Jméno</th>
      <th scope="col">Admin</th>
      <th class="d-none d-lg-table-cell" scope="col">Film</th>
      <th class="d-none d-lg-table-cell" scope="col">Herec</th>
      <th scope="col"></th>
      <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <% ArrayList<Character> characterList = (ArrayList<Character>) request.getAttribute("characterList");
      for(int i = 0; i < characterList.size(); i++){%>
    <tr>
      <th scope="row"><%=i + 1%>.</th>
      <td><a class="text-dark" target="_blank" href="detail?id=<%=characterList.get(i).getId()%>"> <%=characterList.get(i).getName()%></a></td>
      <td ><%=characterList.get(i).getAdminName()%></td>

      <td class="d-none d-lg-table-cell"><%=characterList.get(i).getFilmName()%></td>
      <td class="d-none d-lg-table-cell"><%=characterList.get(i).getActorName()%></td>
      <td><a class="text-dark" target="_blank" href="/UpdateCharacter?id=<%=characterList.get(i).getId()%>">Upravit</a></td>
      <td><a class="text-dark" href="delete-character?id=<%=characterList.get(i).getId()%>">Smazat</a></td>
    </tr>
    <%   } %>


    </tbody>
  </table>
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