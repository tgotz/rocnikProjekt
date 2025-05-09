<%@ page import="model.Character" %>
<%@ page import="java.util.Base64" %><%--
  Created by IntelliJ IDEA.
  User: T.Gotz
  Date: 22.04.2024
  Time: 13:43
  To change this template use File | Settings | File Templates.
--%>
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
        <h2 class="mt-5 mb-5 outline-heading">Upravte postavu</h2>
    </div>
</header>
<% Character character = (Character) request.getAttribute("character");
    String base64Image = new String(Base64.getEncoder().encode(character.getImage()));
%>
<div class="container bg-main pt-2">
    <form method="POST" enctype="multipart/form-data" action="/UpdateCharacter">
        <div class="row">
            <div class="col-md-6">
                <input type="hidden" name="id" value="<%= character.getId() %>">
                <div class="form-group mb-2">
                    <label for="characterName">Jméno postavy</label>
                    <input name="name" type="text" class="form-control" value="<%=character.getName()%>" id="characterName" placeholder="Zadej jméno postavy" required>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterType">Typ</label>
                    <select name="type" class="form-control" id="characterType" required>
                        <option <%if (character.getType().equals("Animovaná")){ %> selected <%} %>  value="Animovaná">Animovaná</option>
                        <option  <%if (character.getType().equals("Hraná")){ %> selected <%} %> value="Hraná">Hraná</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterGender">Pohlaví</label>
                    <select name="gender" class="form-control" id="characterGender" required>
                        <option  <%if (character.getGender().equals("Muž")){ %> selected <%} %> value="Muž">Muž</option>
                        <option <%if (character.getGender().equals("Žena")){ %> selected <%} %> value="Žena">Žena</option>
                        <option <%if (character.getGender().equals("Jiné")){ %> selected <%} %> value="Jiné">Jiné</option>
                    </select>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="filmShow">Film/seriál</label>
                    <input value="<%=character.getFilmName()%>" name="film" type="text" class="form-control" id="filmShow" onkeyup="fetchFilms(this.value)" placeholder="Zadej jméno filmu/seriál" required>
                    <div  class="suggestions" id="filmSuggestions"></div>
                </div>
            </div>
        </div>
        <div class="form-group mb-2">
            <label for="characterDescription">Popis</label>
            <textarea name="desc" class="form-control" id="characterDescription" rows="3" placeholder="Zadej popis postavy" required><%=character.getDesc()%></textarea>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterAge">Věk</label>
                    <input value="<%= character.getAge() != 0 ? character.getAge() : "" %>" name="age" type="number" class="form-control" id="characterAge" placeholder="Zadej věk postavy">
                </div>
            </div>
            <div class="col-md-6 d-flex align-items-center justify-content-between">
                <div class="form-group mb-2 col-3" >
                    <label for="characterImage">Obrázek</label>
                    <input name="picture" type="file" class="form-control-file" id="characterImage">
                </div>
                <img  src="data:image/jpeg;base64,<%= base64Image %>" class="img-fluid col-3" alt="Character Image">
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterActor">Herec/herečka(dabér, dabérka)</label>
                    <input value="<%=character.getActorName()%>" name="actor" required type="text" class="form-control" id="characterActor" placeholder="Zadej jméno herce/herečky (dabéra...)">
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label  for="characterNickname">Přezdívka</label>
                    <input value="<%= character.getNickname() != null ? character.getNickname() : "" %>" name="nickname" type="text" class="form-control" id="characterNickname" placeholder="Zadej přezdívku postavy">
                </div>
            </div>
        </div>
        <button type="submit" class="btn btn-primary" >Odeslat</button>
    </form>
</div>
<!--Vlastní JS-->
<script type="text/javascript" src="form.js"></script>

<!--Bootstrap-->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
</body>

</html>
<%}else{
    response.sendRedirect("login.jsp");
}%>