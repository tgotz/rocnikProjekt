<%--
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
<div class="container">
    <h1 class="mt-4">Character Details</h1>
    <div class="row mt-4">
        <div class="col-md-6">
            <h2>Character Name</h2>
            <img src="character-image.jpg" class="img-fluid rounded" alt="Character Image">
        </div>
        <div class="col-md-6">
            <ul class="list-group">
                <li class="list-group-item"><strong>Nickname:</strong> Character's Nickname</li>
                <li class="list-group-item"><strong>Age:</strong> Character's Age</li>
                <!-- Add more details like gender, type, etc. -->
            </ul>
        </div>
    </div>
    <div class="row mt-4">
        <div class="col-md-12">
            <h2>Actor Name</h2>
            <p>Actor's Name</p>
        </div>
    </div>
    <div class="row mt-4">
        <div class="col-md-6">
            <h2>Quotes</h2>
            <ul class="list-group">
                <li class="list-group-item">Quote 1</li>
                <li class="list-group-item">Quote 2</li>
                <!-- Add more quotes -->
            </ul>
            <a href="#" class="btn btn-primary mt-3">Show More Quotes</a>
        </div>
        <div class="col-md-6">
            <h2>Reviews</h2>
            <ul class="list-group">
                <li class="list-group-item">
                    <h4>Review 1</h4>
                    <p>Overall Rating: 4/5</p>
                    <p>Attractiveness Rating: 3/5</p>
                    <p>Review Text: Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                </li>
                <!-- Add more reviews -->
            </ul>
        </div>
    </div>
    <div class="row mt-4">
        <div class="col-md-12">
            <h2>Add Review</h2>
            <form>
                <div class="form-group">
                    <label for="reviewerName">Reviewer Name</label>
                    <input type="text" class="form-control" id="reviewerName" placeholder="Enter your name">
                </div>
                <div class="form-group">
                    <label for="overallRating">Overall Rating</label>
                    <input type="number" class="form-control" id="overallRating" placeholder="Enter overall rating">
                </div>
                <div class="form-group">
                    <label for="attractivenessRating">Attractiveness Rating</label>
                    <input type="number" class="form-control" id="attractivenessRating" placeholder="Enter attractiveness rating">
                </div>
                <div class="form-group">
                    <label for="reviewText">Review Text</label>
                    <textarea class="form-control" id="reviewText" rows="3" placeholder="Enter your review"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Submit Review</button>
            </form>
        </div>
    </div>
</div>
<!-- slick carousel -->
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