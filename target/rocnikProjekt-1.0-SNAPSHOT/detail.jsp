<%@ page import="model.Character" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Review" %><%--
  Created by IntelliJ IDEA.
  User: Tomáš
  Date: 09.04.2024
  Time: 15:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Česko-Slovenská databáze filmových postav</title>
    <link rel="stylesheet" href="styles/bootstrap.css">
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
    </div>
</header>
<%
    Character character = (Character)request.getAttribute("character");
    String base64Image = new String(Base64.getEncoder().encode(character.getImage()));
    ArrayList<String> quotesList = (ArrayList<String>) request.getAttribute("quotesList");
            ArrayList<Review> reviewsList = (ArrayList<Review>) request.getAttribute("reviewList");
%>
<div class="container mt-5 bg-main pt-2">
    <h2>Detaily postavy</h2>
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
    <% if(quotesList.size() != 0){%>
    <h3>Hlášky</h3>
    <ul>
        <% for(int i = 0; i < quotesList.size(); i++){%>

        <li><%=quotesList.get(i)%></li>
        <%} %>
    </ul>
    <%}%>
    <hr>
    <div class="reviews text-center">
    <h3>Přidat recenzi </h3>
        <p></p>
    <i class="arrowDown"></i>
    <div id="reviewForm" class="review-form d-none animate__animated animate__fadeOut">
        <form action="addReviewServlet" method="post">
            <input type="hidden" name="characterId" value="<%= character.getId() %>">
            <div class="form-group">
                <label for="name">Vaše přezdívka(bude zobrazena u recenze):</label>
                <input dirname="name" type="text" class="form-control" id="name" name="name" required>
            </div>
            <div class="form-group">
                <label for="overallRating">Celkové hodnocení:</label>
                <input name="overallRating" min="0" max="10" step="1" type="range" class="form-range" id="overallRating">
            </div>
            <div class="form-group">
                <label for="attractivenessRating">Hodnocení atrakivity postavy:</label>
                <input dirname="attractivenessRating" min="0" max="10" step="1" name="attractivenessRating" type="range" class="form-range" id="attractivenessRating">
            </div>
            <div class="form-group">
                <label for="reviewText">Text recenze:</label>
                <textarea dirname="reviewText" class="form-control" id="reviewText" name="reviewText" rows="3" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary mt-3">Odeslat</button>
        </form>
        <i class="arrowUp d-none"></i>
    </div>
    </div>
    <h3 class="mt-5">Recenze</h3>
    <% if(reviewsList.size() == 0){%>
        <p class="text-center my-2">Nikdo zatím postavu <%=character.getName()%> neohodnotil, nechcete být první?</p>
   <% }%>
    <ul class="list-group">
        <% for(int i = 0; i < reviewsList.size(); i++) {%>
        <li class="list-group-item my-3">
            <strong><%=reviewsList.get(i).getAuthorName()%> říká:</strong><br>
            <strong>Celkové hodnoceí: <%=reviewsList.get(i).getOverallRating()%>/10</strong><br>
            <strong>Hodnocení atraktivity: <%=reviewsList.get(i).getAttractivenessRating()%>/10</strong><br>
            <strong><%=reviewsList.get(i).getReviewText()%></strong>
        </li>
        <%}%>
    </ul>
</div>
<!--Bootstrap-->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>


<script>
    document.querySelector(".arrowDown").addEventListener("click", function() {
        document.querySelector("#reviewForm").classList.toggle("d-none");
        document.querySelector("#reviewForm").classList.toggle("animate__fadeIn");
        document.querySelector("#reviewForm").classList.toggle("animate__fadeOut");

        document.querySelector(".arrowDown").classList.toggle("d-none")
        document.querySelector(".arrowUp").classList.toggle("d-none")

    });
    document.querySelector(".arrowUp").addEventListener("click", function() {
        document.querySelector("#reviewForm").classList.toggle("animate__fadeOut");
        document.querySelector("#reviewForm").classList.toggle("animate__fadeIn");
        setTimeout(function(){
            document.querySelector("#reviewForm").classList.toggle("d-none");
            document.querySelector(".arrowDown").classList.toggle("d-none")
            document.querySelector(".arrowUp").classList.toggle("d-none")
        }, 700);

    });
</script>

</body>

</html>