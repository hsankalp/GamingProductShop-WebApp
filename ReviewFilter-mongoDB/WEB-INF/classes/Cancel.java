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

public class Cancel extends HttpServlet {
	
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
			
			String orderNumber = request.getParameter("orderNumber");				
			System.out.println("Testing Order Number:"+orderNumber);
			
			// If database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB("CSP595Tutorial");
				
			// If the collection does not exists, MongoDB will create it for you
			DBCollection myOrders = db.getCollection("myOrders");
			System.out.println("Collection myOrders selected successfully");
			
			BasicDBObject query = new BasicDBObject();
			query.put("_id", new ObjectId(orderNumber));
			DBObject dbObj = myOrders.findOne(query);
			
			
			myOrders.remove(dbObj);
			
			/* BasicDBObject deleteQuery = new BasicDBObject();
			deleteQuery.put("orderNumber", orderNumber);
			DBCursor cursor = myOrders.find(deleteQuery);
			
			while (cursor.hasNext()) {
			DBObject item = cursor.next();
			myOrders.remove(item);
			} */
			
			
					
			System.out.println("Testing Order Number:"+orderNumber);
				
			System.out.println("Document Removed successfully");
			
			
			PrintWriter out = response.getWriter();
			
			out.println("<html>");
			out.println("<head> </head>");
			out.println("<body>");
			out.println("<h1> Your Order was Cancelled Successfully </h1>");
			
					
			out.println("<table>");
			out.println("<tr>");
			out.println("<td>");
			out.println("<a href='index.html'> HOME </a>");
			
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