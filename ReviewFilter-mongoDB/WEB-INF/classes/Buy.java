import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Date;

public class Buy extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	String productName = " ";
	String imageLocation = " ";
	int productPrice = 0;
	
	public void init(){
		//Connect to Mongo DB
		MongoClient mongo = new MongoClient("localhost", 27017);
						
		// if database doesn't exists, MongoDB will create it for you
		DB db = mongo.getDB("CustomerReviews");
		
		DBCollection myReviews = db.getCollection("myReviews");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		String itemID = request.getParameter("itemID");
						
		try{
			//Get the values from the form
			if (itemID != null){
				productName = itemID;
			}
			/* }else if (request.getParameter("XBox_One") != null){
				productName = "X Box One";
				imageLocation = "images/img_XBoxOne.jpg";
				productPrice = 500;
			
			}else if (request.getParameter("PlayStation_3") != null){
				productName = "PlayStation 3";
				imageLocation = "images/img_PlayStation3.jpg";
				productPrice = 220;
				
			}else if (request.getParameter("PlayStation_4") != null){
				productName = "PlayStation 4";
				imageLocation = "images/img_PlayStation4.jpg";
				productPrice = 400;
			}
			else if (request.getParameter("Wii") != null){
				productName = "Wii";
				imageLocation = "images/img_WiiConsole.jpg";
				productPrice = 300;
			}
			else if (request.getParameter("WiiU") != null){
				productName = "WiiU";
				imageLocation = "images/img_WiiUConsole.jpg";
				productPrice = 200;
			}
			else if (request.getParameter("Fifa") != null){
				productName = "Fifa 2015";
				imageLocation = "images/img_Fifa.jpg";
				productPrice = 40;
			}
			else if (request.getParameter("Titanfall") != null){
				productName = "Titanfall";
				imageLocation = "images/img_Titanfall.jpg";
				productPrice = 20;
			}
			else if (request.getParameter("CODAW") != null){
				productName = "Call of Duty: Advanced Warfare";
				imageLocation = "images/img_CODAW.jpg";
				productPrice = 60;
			}
			else if (request.getParameter("CODBO") != null){
				productName = "Call of Duty: Black Ops";
				imageLocation = "images/img_CODBO.jpg";
				productPrice = 90;
			}
			else if (request.getParameter("NBA") != null){
				productName = "NBA 2K15";
				imageLocation = "images/img_nba.jpg";
				productPrice = 50;
			}
			else if (request.getParameter("WWE") != null){
				productName = "WWE 2K15";
				imageLocation = "images/img_wwe.jpg";
				productPrice = 30;
			} */
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Buy</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Place Order</h1>");
			out.println("<form method=\"get\" action=\"SubmitOrder\">");			
			out.println("<fieldset>");
			out.println("<legend>Personal information:</legend>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td> First name: </td>");
			out.println("<td> <input type=\"text\" name=\"firstName\"> </td>");
			out.println("</tr>");				
			out.println("<tr>");
			out.println("<td> Last name: </td>");
			out.println("<td> <input type=\"text\" name=\"lastName\"> </td>");
			out.println("</tr>");				
			out.println("<tr>");
			out.println("<td> Address: </td>");
			out.println("<td> <input type=\"text\" name=\"address\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Phone: </td>");
			out.println("<td> <input type=\"text\" name=\"phoneNumber\"> </td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<br><br>");
			out.println("<input type=\"submit\" value=\"Place Order\">");			
			out.println("</fieldset>");		
			out.println("</form>");	
			out.println("</body>");
			out.println("</html>");
						
	    } catch (MongoException e) {
		e.printStackTrace();
	    }

	}
}