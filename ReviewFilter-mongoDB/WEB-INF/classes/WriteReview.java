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

public class WriteReview extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	String productName = "";
	String imageLocation = " ";
	int productPrice = 0;
	
	
	public void init(){
		//Connect to Mongo DB
		MongoClient mongo = new MongoClient("localhost", 27017);
						
		// if database doesn't exists, MongoDB will create it for you
		DB db = mongo.getDB("CustomerReviews");
		
		DBCollection myReviews = db.getCollection("myReviews");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		String itemID = request.getParameter("itemID");				
		try{
			//Get the values from the form
			if (itemID != null){
				productName = itemID;
				
		
			}
			
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Write Review</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<h1>Write Review</h1>");							
			out.println(" <h3>" +productName+ "</h3> ");
			out.println("<form method=\"get\" action=\"SubmitReview\">");
			out.println("<fieldset>");
			out.println("<legend>Reviews:</legend>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td> Product Name: </td>");
			out.println("<td> <input type=\"text\" name= \"productName\" value = \""+productName+"\">  </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<tr>");
			out.println("<td> Product Price: </td>");
			out.println("<td> <input type=\"number\" name=\"productPrice\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<tr>");
			out.println("<td> Product Category: </td>");
			out.println("<td> <input type=\"text\" name=\"productCategory\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Retailer Name: </td>");
			out.println("<td> <input type=\"text\" name=\"retailerName\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Retailer Zip: </td>");
			out.println("<td> <input type=\"text\" name=\"retailerZip\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Retailer City: </td>");
			out.println("<td> <input type=\"text\" name=\"retailerCity\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Retailer State: </td>");
			out.println("<td> <input type=\"text\" name=\"retailerState\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Product On Sale: </td>");
			out.println("<td> <input type=\"text\" name=\"productOnSale\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Manufacturer Name: </td>");
			out.println("<td> <input type=\"text\" name=\"manufacturerName\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Manufacturer Rebate: </td>");
			out.println("<td> <input type=\"text\" name=\"manufacturerRebate\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> User Name: </td>");
			out.println("<td> <input type=\"text\" name=\"userName\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> User Age: </td>");
			out.println("<td> <input type=\"text\" name=\"userAge\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> User Gender: </td>");
			out.println("<td> <input type=\"text\" name=\"userGender\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> User Occupation: </td>");
			out.println("<td> <input type=\"text\" name=\"userOccupation\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Review Rating: </td>");
			out.println("<td>");
			out.println("<select name=\"reviewRating\">");
			out.println("<option value=\"1\" selected>1</option>");
			out.println("<option value=\"2\">2</option>");
			out.println("<option value=\"3\">3</option>");
			out.println("<option value=\"4\">4</option>");
			out.println("<option value=\"5\">5</option>");
			out.println("</td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Review Date: </td>");
			out.println("<td> <input type=\"text\" name=\"reviewDate\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Review Text: </td>");
			out.println("<td><textarea name=\"reviewText\" rows=\"4\" cols=\"50\"> </textarea></td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<br><br>");
			out.println("<input type=\"submit\" value=\"Submit Review\">");
			out.println("</fieldset>");
			out.println("</form>");
						
	    } catch (MongoException e) {
		e.printStackTrace();
	    }

	}
}