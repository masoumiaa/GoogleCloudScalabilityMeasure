<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%-- //[START imports]--%>
<%@ page import="advertissment.board.Greeting" %>
<%@ page import="advertissment.board.Advertissment" %>
<%-- //[END imports]--%>

<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
</head>

<body class="container bg-info">

<%
    String advertissmentName = request.getParameter("advertissmentName");
	String filterValue = request.getParameter("filterValue");
	String priceMin = request.getParameter("priceMin");
	String priceMax = request.getParameter("priceMax");
    if (advertissmentName == null) {
    	advertissmentName = "default";
    }
    pageContext.setAttribute("advertissmentName", advertissmentName);
    pageContext.setAttribute("filterValue", filterValue);
    pageContext.setAttribute("priceMin", priceMin);
    pageContext.setAttribute("priceMax", priceMax);
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
        pageContext.setAttribute("user", user);
%>

<p>${fn:escapeXml(user.nickname)}! (You can
    <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
%>
<p>
    <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
    to include your name with greetings you post.</p>
<%
    }
%>
	
<div>
	<h1>Search</h1>
	<form action="/filter" method="post">
	    <div>content <input type="text" name="filterValue" value="${fn:escapeXml(filterValue)}"></input></div>
	    <div>price min <input type="text" name="priceMin" value="${fn:escapeXml(priceMin)}" disabled>, max <input type="text" name="priceMax" value="${fn:escapeXml(priceMax)}" disabled></input></div>
	    <div><input type="submit" value="Filter Advertissment"/></div>
    	<input type="hidden" name="advertissmentName" value="${fn:escapeXml(advertissmentName)}"/>
	</form>
</div>


<%-- //[START datastore]--%>
<%
    // Create the correct Ancestor key
    Advertissment advert = new Advertissment(advertissmentName);

    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    List<Greeting> greetings;
    
    String min = "0";
	String max = "999999";
	if(!priceMin.isEmpty()){
		min = priceMin;
	}
	if(!priceMax.isEmpty()){
		max = priceMax;
	}
	
    if(!filterValue.equals("") && !filterValue.isEmpty()){
    	if(!priceMin.isEmpty() || !priceMax.isEmpty()){
      		greetings = advert.getGreetings(filterValue, priceMin, priceMax);
    	} else {
    		greetings = advert.getGreetings(filterValue);
    	}
    } else {
    	if(!priceMin.isEmpty() || !priceMax.isEmpty()){
      		greetings = advert.getGreetings(priceMin, priceMax);
    	} else {
    		greetings = advert.getGreetings();
    	}
    }
    
    if (greetings.isEmpty()) {
%>
<p>There is no ads for your research.</p>
<%
    } else {
%>


	<h1>Advertisements</h1>
<table class="table table-bordered table-striped">
  <thead class="thead-dark">
	  <tr>
	    <th>User </th>
	    <th>Ads Description</th>
	    <th>Price</th>
	    <th></th>
	  </tr>
  </thead>

<%
      // Look at all of our greetings
        for (Greeting greeting : greetings) {
        	
            pageContext.setAttribute("greeting_content", greeting.content);
            pageContext.setAttribute("greeting_price", greeting.price);
            String author;
            if (greeting.authorEmail == null) {
                author = "An anonymous person";
            } else {
                author = greeting.authorEmail;
                String author_id = greeting.authorId;
                if (user != null && user.getUserId().equals(author_id)) {
                    author += " (You)";
                }
            }
            pageContext.setAttribute("greeting_user", author);
            
         	// Returns the key in an encoded form that can be used as part of a URL.
            String key = (greeting.key).toUrlSafe();
            pageContext.setAttribute("greeting_key", key );
%>
	<tbody>
	<tr>
	    <td><b>${fn:escapeXml(greeting_user)}</b></td>
	    <td>${fn:escapeXml(greeting_content)}</td>
	    <td>${fn:escapeXml(greeting_price)}</td>
	    <td> 
	    	<form action="/delete" method="post">
	    	 	<input type="hidden" name="key" value="${fn:escapeXml(greeting_key)}" />
	  			<input type="submit" name="button" value="Delete" class="btn btn-danger"/>
	  			<input type="hidden" name="advertissmentName" value="${fn:escapeXml(advertissmentName)}"/>
	  		</form>
		</td>
	  </tr>
	  </tbody>
<%
        }
%></table><%
    }
%>
<div>
	<h1>Add advertisement</h1>
	<form action="/add" method="post">
	    <div><textarea name="content" rows="3" cols="60"></textarea></div>
	    <div><input type="text" name="price" rows="3" cols="60"></input></div>
	    <div><input type="submit" value="Post Greeting"/></div>
	    <input type="hidden" name="advertissmentName" value="${fn:escapeXml(advertissmentName)}"/>
	</form>
</div>

</body>
</html>
<%-- //[END all]--%>
