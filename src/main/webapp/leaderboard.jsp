<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Character" %><%--
  Created by IntelliJ IDEA.
  User: T.Gotz
  Date: 10.04.2024
  Time: 19:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                        <% //checking if user is an admin
                            if (session != null && session.getAttribute("user") != null) {
                        %>
                        <li class="nav-item ">
                            <a class="nav-link text-white" href="approve">Schvalování postav</a>
                        </li>
                        <li class="nav-item ">
                            <a class="nav-link text-white" href="dashboard">Administrace</a>
                        </li>
                        <% }%>
                    </ul>
                </div>
            </div>
        </nav>

        <h2 class="mt-5 mb-5 outline-heading"><%=request.getAttribute("headline")%></h2>
    </div>
</header>
<div class="container bg-main pt-2">
    <table class="table table-striped">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Jméno</th>
            <th class="d-none d-lg-table-cell" scope="col">Film</th>
            <th class="d-none d-lg-table-cell" scope="col">Herec</th>
            <th scope="col">Celkové hodnocení</th>
            <th scope="col">Hodnocení atraktivity</th>
        </tr>
        </thead>
        <tbody>
        <% ArrayList<Character> characterList = (ArrayList<Character>) request.getAttribute("characterList");
            for(int i = 0; i < characterList.size(); i++){%>
        <tr>
            <th scope="row"><%=i + 1%>.</th>
            <td><%=characterList.get(i).getName()%></td>
            <td class="d-none d-lg-table-cell"><%=characterList.get(i).getFilmName()%></td>
            <td class="d-none d-lg-table-cell"><%=characterList.get(i).getActorName()%></td>
            <td><%=characterList.get(i).getOverallRating()%></td>
            <td><%=characterList.get(i).getAttractivenessRating()%></td>
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