import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession; 
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.bson.types.ObjectId;

public class SubmitOrder extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	MongoClient mongo;
	
	public void init() throws ServletException{
      	// Connect to Mongo DB
		mongo = new MongoClient("localhost", 27017);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		try{
			//Get the values from the form
			HttpSession session=request.getSession();	
			
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String address = request.getParameter("address");
			int phoneNumber = Integer.parseInt(request.getParameter("phoneNumber"));							
										
			// If database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB("CSP595Tutorial");
				
			// If the collection does not exists, MongoDB will create it for you
			DBCollection myOrders = db.getCollection("myOrders");
			System.out.println("Collection myOrders selected successfully");
			
					
			SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(date); 
			cal.add(Calendar.DATE, 14);
			date = cal.getTime();
			String deliveryDate = f.format(date);
			
			BasicDBObject doc = new BasicDBObject("title", "myOrders").
				
				append("firstName", firstName).
				append("lastName", lastName).
				append("address", address).
				append("phoneNumber", phoneNumber).
				//append("orderNumber",orderNumber).
				append("deliveryDate",deliveryDate);
					
			myOrders.insert(doc);
			
			ObjectId id = (ObjectId)doc.get( "_id" );
			
			
			System.out.println("Document inserted successfully");
			
			
			PrintWriter out = response.getWriter();
			
			out.println("<html>");
			out.println("<head> </head>");
			out.println("<body>");
			out.println("<h1> Your Order was placed Successfully </h1>");
			
			out.println("Your order number is "+id+"<br>");
						
			out.println("\nDelivery Date: "+deliveryDate+"\n");
			
			out.println("<table>");
			out.println("<tr>");
			out.println("<td>");
			out.println("<a href='index.html'> HOME </a>");
			
			
			out.println("<br><br>Enter Order Number to Cancel Order:<br><br>");
			
			
			
			
				
			out.println("<form action="+"Cancel"+">");
			
			out.println("Order Number:<br>");
			out.println("<input type="+"text"+" name="+"orderNumber"+" value= "+id+">");
			out.println("<br>");
			
			
			
			
			
			out.println("<input type="+"submit"+" value="+"Submit to Cancel"+">");
			out.println("</form>");
			
			out.println("</td>");
			out.println("</tr>");
			
					
			out.println("</table>");
			
			out.println("</body>");
			out.println("</html>");
			
			//}else{
				
			out.println("You can only cancel within 5 Days of purchase");
			
			out.println("</form>");
			
			out.println("</td>");
			out.println("</tr>");
			
					
			out.println("</table>");
			
			out.println("</body>");
			out.println("</html>");
			
			session.invalidate(); 
			
			out.close();
			
			
			
			
		
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy()	{
      // do nothing.
	}
	
}