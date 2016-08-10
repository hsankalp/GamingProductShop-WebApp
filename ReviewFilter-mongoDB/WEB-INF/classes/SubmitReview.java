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

public class SubmitReview extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	MongoClient mongo;
	
	public void init() throws ServletException{
      	// Connect to Mongo DB
		mongo = new MongoClient("localhost", 27017);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		try{
			//Get the values from the form
			String productName = request.getParameter("productName");
			String productCategory = request.getParameter("productCategory");
			int productPrice = Integer.parseInt(request.getParameter("productPrice"));
			String retailerName = request.getParameter("retailerName");
			String retailerZip = request.getParameter("retailerZip");
			String retailerCity = request.getParameter("retailerCity");
			String retailerState = request.getParameter("retailerState");
			String productOnSale = request.getParameter("productOnSale");
			String manufacturerName = request.getParameter("manufacturerName");
			String manufacturerRebate = request.getParameter("manufacturerRebate");
			String userName = request.getParameter("userName");
			int userAge = Integer.parseInt(request.getParameter("userAge"));
			String userGender = request.getParameter("userGender");
			String userOccupation = request.getParameter("userOccupation");
			int reviewRating = Integer.parseInt(request.getParameter("reviewRating"));	
			String reviewDate = request.getParameter("reviewDate");
			String reviewText = request.getParameter("reviewText");
										
			// If database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB("CSP595Tutorial");
				
			// If the collection does not exists, MongoDB will create it for you
			DBCollection myReviews = db.getCollection("myReviews");
			System.out.println("Collection myReviews selected successfully");
				
			BasicDBObject doc = new BasicDBObject("title", "myReviews").
				append("productName", productName).
				append("productCategory", productCategory).
				append("productPrice", productPrice).
				append("retailerName", retailerName).
				append("retailerZip", retailerZip).
				append("retailerCity", retailerCity).
				append("retailerState", retailerState).
				append("productOnSale", productOnSale).
				append("manufacturerName", manufacturerName).
				append("manufacturerRebate", manufacturerRebate).
				append("userName", userName).
				append("userAge", userAge).
				append("userGender", userGender).
				append("userOccupation", userOccupation).
				append("reviewRating", reviewRating).
				append("reviewDate", reviewDate).
				append("reviewText", reviewText)
				;
									
			myReviews.insert(doc);
				
			System.out.println("Document inserted successfully");
			
			//Send the response back to the JSP
			PrintWriter out = response.getWriter();
						
			out.println("<html>");
			out.println("<head> </head>");
			out.println("<body>");
			out.println("<h1> Review submitted for:"+ productName + "</h1>");
			
			out.println("<table>");
			
			out.println("<tr>");
			out.println("<td>");
			out.println("<a href='index.html'> Index </a>");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td>");
			out.println("<a href='XBox.html'> X Box </a>");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("<tr>");
			out.println("<td>");
			out.println("<a href='PlayStation.html'> Play Station </a>");
			out.println("</td>");
			out.println("</tr>");
			
			out.println("</table>");
			
			out.println("</body>");
			out.println("</html>");
		
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy()	{
      // do nothing.
	}
	
}