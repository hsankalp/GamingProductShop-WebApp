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
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import com.mongodb.AggregationOutput;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class Trending extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	MongoClient mongo;
	
	public void init() throws ServletException{
      	// Connect to Mongo DB
		mongo = new MongoClient("localhost", 27017);
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		
		PrintWriter output = response.getWriter();
					
		DB db = mongo.getDB("CSP595Tutorial");
		
		// If the collection does not exists, MongoDB will create it for you
		DBCollection myReviews = db.getCollection("myReviews");
		System.out.println("Collection myReviews selected successfully");
		
		boolean countOnly = false;
				
		try {
			
			//Construct the top of the page
			constructPageTop(output);
			
				DBObject match = null;
				DBObject groupFields = null;
				DBObject group = null;
				DBObject projectFields = null;
				DBObject project = null;
				AggregationOutput aggregate = null;
				
				
					match = new BasicDBObject("$match", new BasicDBObject("reviewRating", new BasicDBObject("$gte", 4)));
					
					
					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$retailerCity");
					groupFields.put("count", new BasicDBObject("$sum", 1));
					//groupFields.put("review", new BasicDBObject("$push", "$reviewText"));
					groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
					//groupFields.put("productPrice", new BasicDBObject("$push", "$productPrice"));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					
					
					group = new BasicDBObject("$group", groupFields);
					
					
					
					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("City", "$_id");
					projectFields.put("Product Count", "$count");
					//projectFields.put("Product Price", "$productPrice");
					projectFields.put("Product", "$productName");
					//projectFields.put("User", "$userName");
					//projectFields.put("Reviews", "$review");
					projectFields.put("Rating", new BasicDBObject("$max", "$reviewRating"));
					
					
					
					project = new BasicDBObject("$project", projectFields);
					
					
					
					aggregate = myReviews.aggregate(match,group,project);
					
								
					//Construct the page content
					constructGroupByCityCountContent(aggregate, output, countOnly);
					
			constructPageBottom(output);
			
			
		} catch (MongoException e) {
			e.printStackTrace();
		}

	}
	
	public void constructPageTop(PrintWriter output){
		String pageHeading = "Query Result";
		String myPageTop = "<!DOCTYPE html>" + "<html lang=\"en\">"
					+ "<head>	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+ "<title>Game Speed</title>"
					+ "<link rel=\"stylesheet\" href=\"styles.css\" type=\"text/css\" />"
					+ "</head>"
					+ "<body>"
					+ "<div id=\"container\">"
					+ "<header>"
					+ "<h1><a href=\"/\">GameSpeed<span></span></a></h1><h2>Tutorial 3</h2>"
					+ "</header>"
					+ "<nav>"
					+ "<ul>"
					+ "<li class=\"\"><a href=\"index.html\">Home</a></li>"
					+ "<li class = \"start selected\"><a href=\"DataAnalytics.html\">Data Analytics</a></li>"
					+ "</ul>"
					+ "</nav>"
					+ "<div id=\"body\">"
					+ "<section id=\"review-content\">"
					+ "<article>"
					+ "<h2 style=\"color:#DE2D3A;font-weight:700;\">" +pageHeading + "</h2>";
		
		output.println(myPageTop);		
	}
	
	public void constructPageBottom(PrintWriter output){
		String myPageBottom = "</article>"
					+ "</section>"
                    + "<div class=\"clear\"></div>"
					+ "</div>"
					+ "<footer>"
					+ "<div class=\"footer-content\">"
					+ "<ul>"
                    + "<li>"
                    + "<h4>Dummy Link Section 1</h4>"
                    + "</li>"
                    + "<li><a href=\"#\">Dummy Link 1</a>"
                    + "</li>"
                    + "<li><a href=\"#\">Dummy Link 2</a>"
                    + "</li>"
                    + "<li><a href=\"#\">Dummy Link  3</a>"
                    + "</li>"
					+ "</ul>"
					+ "<div class=\"clear\"></div>"
					+ "</div>"
					+ "<div class=\"footer-bottom\">"
					+ "<p>CSP 595 - Enterprise Web Application - Assignment#3</p>"
					+ "</div>"
					+ "</footer>"
					+ "</div>"
					+ "</body>"
					+ "</html>";
		
		output.println(myPageBottom);
	}
	
	
	
	public void constructGroupByCityCountContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		
		output.println("<h1> Trending Products in every City  </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				BasicDBList rating = (BasicDBList) bobj.get("Rating");
				
				//System.out.print(productList);
				
				List<String> res = new ArrayList<String>();

				for(Object el: productList) {
						res.add((String) el);
				}
				
				Map<String, Integer> stringsCount = new HashMap<>();

				for(String s: res)
				{
					Integer c = stringsCount.get(s);
					if(c == null) c = new Integer(0);
					c++;
					stringsCount.put(s,c);
				}

				Map.Entry<String,Integer> mostRepeated = null;
				for(Map.Entry<String, Integer> e: stringsCount.entrySet())
				{
					if(mostRepeated == null || mostRepeated.getValue()<e.getValue())
					mostRepeated = e;
				}

				if(mostRepeated != null)
				System.out.println("Most common string: " + mostRepeated.getKey());
				
				rowCount++;
				tableData = "<tr><td>City: "+bobj.getString("City")+"</td>&nbsp"
						+	"<td>Most Liked Product: "+mostRepeated.getKey()+"</td>"
						+	"<td>Number of times the Product was bought: "+mostRepeated.getValue()+"</td>"
						+	"<td>Rating: "+rating.get(0)+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				
				
				//Reset product count
				productCount =0;
		}
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
}