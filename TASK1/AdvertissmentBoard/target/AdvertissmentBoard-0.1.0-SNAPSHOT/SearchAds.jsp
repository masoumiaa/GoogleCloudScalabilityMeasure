<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>


<%-- //[START imports]--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="advertissment.board.Greeting" %>
<%@ page import="advertissment.board.Advertissment" %>
<%@ page import="java.util.List" %>
<%-- //[END imports]--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<title>Ads Search Page</title>
</head>
<body class="container bg-info">
<h2>Ads Search Page</h2>
<h3>Advertisement List : </h3>

<%-- //[START datastore]--%>
<%
    // Create the correct Ancestor key
    Advertissment advert = new Advertissment("default");

    // Run an ancestor query to ensure we see the most up-to-date
    // view of the Greetings belonging to the selected Guestbook.
    List<Greeting> greetings;
    greetings = advert.getGreetings();

    if (greetings.isEmpty()) {
%>
<p>Guestbook has no messages.</p>
<%	} else { %>
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
          }
          pageContext.setAttribute("greeting_user", author);
%>
  <tr>
    <td><b>${fn:escapeXml(greeting_user)}</b></td>
    <td>${fn:escapeXml(greeting_content)}</td>
    <td>${fn:escapeXml(greeting_price)}</td>
    <td> 
		<button type="button" class="btn btn-danger">Delete</button>
	</td><!-- TODO add delete value-->
  </tr>
<%
      }
  }
%>
</table>
</body>
</html>