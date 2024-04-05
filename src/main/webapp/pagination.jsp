<!-- to use special czech characters -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.*" %>

<!--getting varibles from index.jsp, which I need to use here -->

<%
    int pageNumber;
    if(request.getAttribute("pageNumber") != null){
        pageNumber = (int)(request.getAttribute("pageNumber"));
    } else{
        pageNumber = 1;
    }
    int maxPage = (int)request.getAttribute("maxPage");
    int count = (int)request.getAttribute("count");
    //pageination



    if(count >= 24){
%>


<%!
    //function to make url links based on current page number + also int a = what chnange do you want to make to current based on current page number
    //a -1 = previous page ; a + 1 = next etc
    String makeNewUrl(HttpServletRequest request, int a, int pageNumber) {
        String currentURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            //check if the page parameter already exists
            if (queryString.contains("page=")) {
                if (queryString.contains("page=")) {
                    //replace the value of the page parameter with the new page number
                    queryString = queryString.replaceAll("page=[0-9]+", "page=" + (pageNumber +a));
                }
            } else {
                //append the page parameter with  "&"
                queryString += "&page=" + (pageNumber + a);
            }
        } else {
            //URL doesnt have existing parameters
            queryString = "page=" + (pageNumber + a);
        }
        return "filter?" + queryString;
}
%>

<!--the pagination -->
<nav aria-label="Page navigation" class="my-5">
    <ul class="pagination justify-content-center mt-3">
        <!--previous page-->
        <li class="page-item <% if(pageNumber == 1){%> disabled <% }%>" >
            <a class="page-link"  href="<%= makeNewUrl(request, -1, pageNumber) %>"> Předchozí</a>
        </li>
        <!--page number - 2-->
        <% if(pageNumber == maxPage && pageNumber >= 3) {%>
        <li class="page-item"><a class="page-link" href="/filter?page=<%=(pageNumber -2)%>"><%=pageNumber - 2%></a></li>
        <%} %>
        <!--page number -1 -->
        <% if(pageNumber > (maxPage -2) && pageNumber >= 2) {%>
        <li class="page-item"><a class="page-link" href="/filter?page=<%=(pageNumber -1)%>"><%=pageNumber - 1%></a></li>
        <%} %>

        <!--current page number-->
        <li class="page-item font-weight-bold"><a class="page-link" href="/filter?page=<%=(pageNumber)%>"><%=pageNumber%></a></li>

        <!--page number + 1-->
        <% if(pageNumber < maxPage) {%>
        <li class="page-item"><a class="page-link" href="/filter?page=<%=(pageNumber + 1)%>"><%=pageNumber + 1%></a></li>
        <%} %>

        <!--page number + 2-->
        <% if(pageNumber < (maxPage - 1)) {%>
        <li class="page-item"><a class="page-link" href="/filter?page=<%=(pageNumber + 2)%>"><%=pageNumber + 2%></a></li>
        <%} %>
        <!--next page-->
        <li class="page-item">
            <a class="page-link <% if(pageNumber == maxPage){%> disabled <% }%>" href="<%= makeNewUrl(request, +1, pageNumber) %>">Další</a>
        </li>
    </ul>
</nav>

<%
    }

%>