<%--
  Created by IntelliJ IDEA.
  User: Tomas
  Date: 15.04.2024
  Time: 12:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Česko-Slovenská databáze filmových postav</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <!--Animate.css    -->

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
    <!--Vlastní css-->
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
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
                            <a class="nav-link active" aria-current="page" href="#">Domů</a>
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
                    </ul>
                </div>
            </div>
        </nav>
    </div>
    </header>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <h2 class="mt-4 mb-4">Přihlaš se</h2>
                <% if(request.getAttribute("error") != null){%>
                <p><%=request.getAttribute("error")%></p>
                <%}%>
                <form action="LoginServlet" method="post">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary mt-3">Přihlásit</button>
                </form>
            </div>
        </div>
    </div>
<!--Bootstrap-->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>


</body>

</html>