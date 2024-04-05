<%@ page import="java.sql.*" %>
<%@ page import="com.mysql.cj.protocol.Resultset" %>
<%@ page import="java.awt.*" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.util.Base64" %>
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

  <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.css" />
  <link rel="stylesheet" type="text/css"
        href="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick-theme.css" />

</head>

<body>
<header>
  <div class="container">
    <nav class="navbar navbar-dark navbar-expand-lg ">
      <div class="container-fluid">
        <a class="navbar-brand " href="index.html">
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
                <li><a class="dropdown-item" href="#">Nejoblíbenější</a></li>
                <li><a class="dropdown-item" href="#">Nejnenáviděnejší</a></li>
                <li><a class="dropdown-item" href="#">Nejatraktivější</a></li>
              </ul>
            </li>
            <li class="nav-item ">
              <a class="nav-link text-white" href="#">Přidat postavu</a>
            </li>
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
          System.out.println("v indexu beru parametr z servletu " + pageNumber);
        } else{
          pageNumber = 1;
        }

        //dont mind this :D - this is for paging purposes
        int count = 1;
        int maxCount = count + pageNumber * 24;
        int maxPage = 0;


        try{
          //JDBC - connecting to database
          DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
          Connection con = DriverManager.getConnection(
                  "jdbc:mysql://localhost:3306/characters1",
                  "webik",
                  "webik69"
          );

          Statement st = con.createStatement();

          //getting characters from the database for the "recently added" section
          String recentlyAddedQuery = "SELECT postavy.idpostavy as id, postavy.jmeno as jmeno, postavy.obrazek as obrazek, filmy.nazevFilmu as nazevFilmu FROM postavy JOIN filmy ON filmy.idfilmu = postavy.idfilmu ORDER BY postavy.datumPridani DESC;";
          ResultSet rsRecentlyAdded = st.executeQuery(recentlyAddedQuery);
          int countRecentlyAdded = 0;
          while(rsRecentlyAdded.next()){

                if(countRecentlyAdded < 6){
                  countRecentlyAdded++;
                  byte[] imageData = rsRecentlyAdded.getBytes("obrazek");
                  String base64Image = new String(Base64.getEncoder().encode(imageData));
      %>

      <div class="text-center slick-carousel-item">
        <img class="character-image" src="data:image/jpeg;base64,<%= base64Image %>" alt="postava">
        <div class="caption caption-carousel">
          <h3 class="my-0"><%=rsRecentlyAdded.getString("jmeno")%></h3>
          <p class="my-0"><%=rsRecentlyAdded.getString("nazevFilmu")%></p>
        </div>
      </div>
      <%
          }
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
            <option value="datum ASC">Datum přidání (nové-staré)</option>
            <option value="datum DESC">Datum přidání (staré-nové)</option>
          </select>
          <div>
            <button type="submit" class="btn btn-primary">Filtrovat</button>
          </div>
        </div>
        </div>
      </form>
    </div>

    <div class="col-xl-9">

      <div class="d-flex flex-wrap" id="characterCards">

        <%
            //geting the characters data from database
          String sql;
          if(request.getAttribute("sql") != null){
            sql = (String) request.getAttribute("sql");
          } else{
            sql = "SELECT p.idpostavy as id, p.jmeno as jmeno, p.popis as popis, p.datumPridani as datumPridani, p.typPostavy as typPostavy, p.pohlavi as pohlavi, p.vek as vek, p.obrazek as obrazek, p.prezdivka as prezdivka, herci.jmeno as jmenoHerce, filmy.nazevFilmu as nazevFilmu, administratori.username as username FROM postavy p JOIN administratori ON p.idadministrator = administratori.idadministrator JOIN filmy ON p.idfilmu = filmy.idfilmu JOIN herci ON p.idherce = herci.idherce ORDER BY p.idpostavy;";
          }
            ResultSet rs = st.executeQuery(sql);
            //looping trough characters
            while(rs.next()){

              if(count < maxCount && count > (pageNumber - 1) * 24){
                byte[] imageData = rs.getBytes("obrazek");
                String base64Image = new String(Base64.getEncoder().encode(imageData));

        %>
        <div class="text-center database-item col-12 col-sm-6 col-lg-3">
          <div class="m-2 character-item">
            <img class="character-image" src="data:image/jpeg;base64,<%= base64Image %>"
                 alt="postava">
            <div class="caption">
              <h5 class="my-0"><%=rs.getString("jmeno") %></h5>
              <p class="my-0"><%=rs.getString("id") %></p>
            </div>
          </div>
        </div>
        <%
              }
              count++;
            }
            maxPage = (int)Math.ceil(count / 24.0);
          }catch (SQLException e){
              e.printStackTrace();

            }
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


<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="https://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/slick-carousel@1.8.1/slick/slick.min.js"></script>
<!--Vlastní JS-->
<script type="text/javascript" src="script.js"></script>
<!--Bootstrap-->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>

</body>

</html>