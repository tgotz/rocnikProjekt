<%--
  Created by IntelliJ IDEA.
  User: T.Gotz
  Date: 06.04.2024
  Time: 13:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Česko-Slovenská databáze filmových postav</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
    >
    <!--Animate.css    -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
    <!--Vlastní css-->
    <link rel="stylesheet" href="styles/styles.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css" />
    <link rel="stylesheet" type="text/css"
          href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css" />

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
                    </ul>
                </div>
            </div>
        </nav>
        <h2 class="mt-5 mb-5 outline-heading">Přidejte novou postavu</h2>
    </div>
</header>
<div class="container bg-main pt-2">

    <% if(request.getAttribute("outcome") != null){ %>
     <div class="message"><%=request.getAttribute("outcome")%></div>
      <%  } %>

    <form method="POST" enctype="multipart/form-data" action="/AddCharacterServlet">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterName">Jméno postavy</label>
                    <input name="name" type="text" class="form-control" id="characterName" placeholder="Zadej jméno postavy" required>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterType">Typ</label>
                    <select name="type" class="form-control" id="characterType" required>
                        <option value="Animovaná">Animovaná</option>
                        <option value="Hraná">Hraná</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterGender">Pohlaví</label>
                    <select name="gender" class="form-control" id="characterGender" required>
                        <option value="Muž">Muž</option>
                        <option value="Žena">Žena</option>
                        <option value="Jiné">Jiné</option>
                    </select>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="filmShow">Film/seriál</label>
                    <input name="film" type="text" class="form-control" id="filmShow" onkeyup="fetchFilms(this.value)" placeholder="Zadej jméno filmu/seriál" required>
                    <div  class="suggestions" id="filmSuggestions"></div>
                </div>
            </div>
        </div>
        <div class="form-group mb-2">
            <label for="characterDescription">Popis</label>
            <textarea name="desc" class="form-control" id="characterDescription" rows="3" placeholder="Zadej popis postavy" required></textarea>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterAge">Věk</label>
                    <input name="age" type="number" class="form-control" id="characterAge" placeholder="Zadej věk postavy">
                </div>
            </div>
            <div class="col-md-6 d-flex align-items-center">
                <div class="form-group mb-2">
                    <label for="characterImage">Obrázek</label>
                    <input name="picture" required type="file" class="form-control-file" id="characterImage">
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterActor">Herec/herečka(dabér, dabérka)</label>
                    <input name="actor" required type="text" class="form-control" id="characterActor" placeholder="Zadej jméno herce/herečky (dabéra...)">
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group mb-2">
                    <label for="characterNickname">Přezdívka</label>
                    <input name="nickname" type="text" class="form-control" id="characterNickname" placeholder="Zadej přezdívku postavy">
                </div>
            </div>
        </div>
        <div class="form-group mb-2">
            <label for="characterQuotes">Hlášky</label>
            <textarea name="quotes" class="form-control" id="characterQuotes" rows="3" placeholder="Zadej hlášky postav, kednotlivé hlášky odděl středníkem (;)"></textarea>
        </div>
        <button type="submit" class="btn btn-primary" >Odeslat</button>
    </form>
</div>
<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>
<!--Vlastní JS-->
<script type="text/javascript" src="script.js"></script>
<script type="text/javascript" src="form.js"></script>

<!--Bootstrap-->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>

</body>

</html>