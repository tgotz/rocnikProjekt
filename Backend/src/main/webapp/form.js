function fetchFilms(input) {
    //Send AJAX request to fetchfilmservlet - with the input from form
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "fetchFilms?input=" + input, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                displaySuggestions(xhr.responseText);
            } else {
                console.error("Failed to fetch films.");
            }
        }
    };
    xhr.send();
}
function displaySuggestions(suggestions) {
    //Clear previous suggestions
    document.getElementById("filmSuggestions").innerHTML = "";
    //Get new suggestiong from JSON response
    var films = JSON.parse(suggestions);
    //Display the suggestions
    films.forEach(function(film) {
        var suggestion = document.createElement("div");
        suggestion.classList += "filmName";
        suggestion.textContent = film;
        suggestion.onclick = function() {
            document.getElementById("filmShow").value = film;
            document.getElementById("filmSuggestions").innerHTML = "";
        };
        document.getElementById("filmSuggestions").appendChild(suggestion);
    });

}

//function to autofill the value of suggested film on click
document.querySelectorAll('.filmName').forEach(function(filmNameLink) {
    filmNameLink.addEventListener('click', function(event) {
        event.preventDefault();

        document.getElementById('selectedFilm').value = this.innerText;
    });
});