package advertissment.board;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import advertissment.board.Greeting;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.datastore.Key;

public class SignAdvertissmentBoardServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {
      
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    response.getWriter().print("Hello App Engine!\r\n");

  }
  
//Process the HTTP POST of the form
 @Override
 public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
   String action = req.getServletPath();
   
   try {
	   switch(action) {
	   case "/delete":
		   deleteAds(req, resp);
		   break;
	   case "/filter":
		   filterAds(req, resp);
		   break;
	   default:
		   addAds(req, resp);
		   break;
	   }
   } catch(Exception ex) {
	   throw new ServletException(ex);
   }
  }
 
 
 private void addAds(HttpServletRequest req, HttpServletResponse resp) throws IOException {
   Greeting greeting;

   UserService userService = UserServiceFactory.getUserService();
   User user = userService.getCurrentUser(); // Find out who the user is.

   String advertissmentName = req.getParameter("advertissmentName");
   String content = req.getParameter("content");
   String price = req.getParameter("price");
   
   if((content!=null && !content.isEmpty()) && (price!=null && !price.isEmpty())) {
	   if (user != null) {
	     greeting = new Greeting(advertissmentName, content, price, user.getUserId(), user.getEmail());
	   } else {
	     greeting = new Greeting(advertissmentName, content, price);
	   }
	   greeting.save();
   }
   resp.sendRedirect("/advertissmentBoard.jsp?advertissmentName=default&filterValue=&priceMin=&priceMax=");
 }
 
 private void filterAds(HttpServletRequest req, HttpServletResponse resp) throws IOException {
   String advertissmentName = req.getParameter("advertissmentName");
   String filterValue = req.getParameter("filterValue");
   String priceMin = req.getParameter("priceMin");
   String priceMax = req.getParameter("priceMax");
   if(priceMin==null) {
	   priceMin = "";
   }
   if(priceMax==null) {
	   priceMax = "";
   }
   if(filterValue == null) {
	   filterValue = "";
   }
   resp.sendRedirect("/advertissmentBoard.jsp?advertissmentName="+advertissmentName+"&filterValue="+filterValue+"&priceMin="+priceMin+"&priceMax="+priceMax);
 }
 
 private void deleteAds(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	   String advertissmentName = req.getParameter("advertissmentName");
		// Create a Key given its URL safe encoded form.
	   Key adsKey = Key.fromUrlSafe(req.getParameter("key"));
	   Greeting gr = new Greeting();
	   gr.delete(adsKey);
	   resp.sendRedirect("/advertissmentBoard.jsp?advertissmentName="+advertissmentName+"&filterValue=&priceMin=&priceMax=");
 }
}