<%@ page import="java.sql.*" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.awt.*" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.util.Base64" %>
<%@ page import="dao.CharacterDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Character" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.IOException" %>
<%@ page import="model.Character"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
  <link rel="stylesheet" type="text/css" href="styles/styles.css">

  <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css" />
  <link rel="stylesheet" type="text/css"
        href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css" />
  <!-- Slick-->
  <script type="text/javascript" src="https://code.jquery.com/jquery-1.11.0.min.js"></script>
  <script type="text/javascript" src="https://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
  <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>
  <script type="text/javascript" src="script.js"></script>
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

    <h2 class="mt-5 outline-heading">Nově přidané postavy...</h2>

    <div class="slick-carousel">
      <%
        //getting current page number from servlet
        int pageNumber;
        if(request.getAttribute("pageNumber") != null){
          pageNumber = (Integer) request.getAttribute("pageNumber");
        } else{
          pageNumber = 1;
        }

        //dont mind this :D - this is for paging purposes
        int count = 1;
        int maxCount = count + pageNumber * 24;
        int maxPage = 0;

        //getting recent characters
        CharacterDAO characterDAO = new CharacterDAO();
        ArrayList<Character> recentCharacters = characterDAO.getRecentCharacters();
        for(int i = 0; i < recentCharacters.size(); i++){
                  String base64Image = new String(Base64.getEncoder().encode(recentCharacters.get(i).getImage()));
      %>

      <div class="text-center slick-carousel-item">
        <a href="detail?id=<%=recentCharacters.get(i).getId()%>">
        <img class="character-image" src="data:image/jpeg;base64,<%= base64Image %>" alt="postava">
        <div class="caption caption-carousel">
          <h3 class="my-0"><%=recentCharacters.get(i).getName()%></h3>
          <p class="my-0"><%=recentCharacters.get(i).getFilmName()%></p>
        </div>
        </a>
      </div>
      <%
          }
      %>
    </div>
    <p class="d-flex justify-content-center mt-5">
      <a href="#database">

        <i class="arrow"></i>
      </a>

    </p>
  </div>
</header>
<div class="database container">
  <h4 class="text-center fs-2 my-5" id="database">Databáze všech postav</h4>
  <div class="row">
    <div class="col-xl-3">
      <h4>Filtrovat</h4>
      <form method="GET" action="/filter">
      <div class="form-group">
        <input type="text" class="form-control" name="search" placeholder="Jméno postavy / filmu / seriálu">
      </div>
      <div class="d-flex flex-wrap">
        <div class="col-12 col-sm-3 col-xl-12">
          <p class="mb-0 mt-2">Typ postavy</p>
          <div class="form-check">
            <input type="checkbox" class="form-check-input" name="cartoon" checked id="cartoonCheckbox">
            <label class="form-check-label" for="cartoonCheckbox">Animované postavy</label>
          </div>
          <div class="form-check">
            <input type="checkbox" checked class="form-check-input" name="irl" id="irlCheckbox">
            <label class="form-check-label" for="irlCheckbox">Hrané postavy</label>
          </div>
        </div>
        <div class="col-12 col-sm-3  col-xl-12">
          <p class="mb-0 mt-2">Pohlaví postav</p>
          <div class="form-check">
            <input type="checkbox" name="male" class="form-check-input" checked id="male">
            <label class="form-check-label" for="male">Muž</label>
          </div>
          <div class="form-check">
            <input type="checkbox" name="female" class="form-check-input" checked id="female">
            <label class="form-check-label" for="female">Žena</label>
          </div>
          <div class="form-check">
            <input type="checkbox" name="other" class="form-check-input" checked id="other">
            <label class="form-check-label" for="other">Jiné</label>
          </div>
        </div>
        <div class="col-12 col-sm-6  col-xl-12 mt-2">
          <select name="order" id="sort" class="custom-select mb-2">
            <option value="">Seřadit dle</option>
            <option value="jmeno ASC">Jména (a-z)</option>
            <option value="jmeno DESC">Jména (z-a)</option>
            <option value="datum DESC">Datum přidání (nové-staré)</option>
            <option value="datum ASC">Datum přidání (staré-nové)</option>
          </select>
          <div>
            <button type="submit" class="btn btn-primary">Filtrovat</button>
          </div>
        </div>
        </div>
      </form>
    </div>

    <div class="col-xl-9">

      <div class="d-flex flex-wrap" id="characters">

        <%
          ArrayList<Character> characters = new ArrayList<>();

          //checking if servlet has provided filtered data - if not - getting unfiltered data
          if(request.getAttribute("charactersFiltred") != null){
            characters = (ArrayList<Character>) request.getAttribute("charactersFiltred");
          } else{
            characters = characterDAO.getCharacters(request);
          }

            for(int i = 0; i < characters.size(); i++) {
              //paging
              if(count < maxCount && count > (pageNumber - 1) * 24){
                //geting correct image data
                String base64Image = new String(Base64.getEncoder().encode(characters.get(i).getImage()));
        %>
        <div class="text-center database-item col-12 col-sm-6 col-lg-3">
          <a href="detail?id=<%=characters.get(i).getId()%>">
          <div class="m-2 character-item">
            <img class="character-image" src="data:image/jpeg;base64,<%= base64Image %>"
                 alt="postava">
            <div class="caption">
              <h5 class="my-0"><%=characters.get(i).getName()%></h5>
              <p class="my-0"><%=characters.get(i).getFilmName() %></p>
            </div>
          </div>
          </a>
        </div>
        <%
              }
              count++;
            }
            maxPage = (int)Math.ceil(count / 24.0);
          request.setAttribute("maxPage", maxPage);
          request.setAttribute("count", count);
        %>
      </div>
      <%
        //display only on last page - link to add new character
        if(pageNumber >= maxPage){
      %>
      <div class="text-center my-4">
        <p class="fs-3">Nenašel jsi co jsi hledal?</p>
        <a href="add_character.jsp" class="btn btn-primary">Přidej postavu</a>
      </div>
       <%
      }
       %>
      </div>

    <!-- pagination -->
    <jsp:include page="pagination.jsp" />

    </div>
  </div>
</div>
</div>

<!--Bootstrap-->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>


</body>

</html>