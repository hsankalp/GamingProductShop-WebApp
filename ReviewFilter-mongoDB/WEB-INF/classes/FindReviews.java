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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class FindReviews extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	MongoClient mongo;
	
	public void init() throws ServletException{
      	// Connect to Mongo DB
		mongo = new MongoClient("localhost", 27017);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		
		PrintWriter output = response.getWriter();
					
		DB db = mongo.getDB("CSP595Tutorial");
		
		// If the collection does not exists, MongoDB will create it for you
		DBCollection myReviews = db.getCollection("myReviews");
		System.out.println("Collection myReviews selected successfully");
		
		BasicDBObject query = new BasicDBObject();
				
		try {
			
			// Get the form data
			String productName = request.getParameter("productName");
			int productPrice = Integer.parseInt(request.getParameter("productPrice"));
			String retailerName = request.getParameter("retailerName");
			String retailerZip = request.getParameter("retailerZip");
			String retailerCity = request.getParameter("retailerCity");
			String manufacturerName = request.getParameter("manufacturerName");
			int userAge = Integer.parseInt(request.getParameter("userAge"));
			int reviewRating = Integer.parseInt(request.getParameter("reviewRating"));
			
			String compareRating = request.getParameter("compareRating");
			String compareAge = request.getParameter("compareAge");
			String comparePrice = request.getParameter("comparePrice");
			String returnValueDropdown = request.getParameter("returnValue");
			String groupByDropdown = request.getParameter("groupByDropdown");
			
			//Boolean flags to check the filter settings
			boolean noFilter = false;
			boolean filterByProduct = false;
			boolean filterByPrice = false;
			boolean filterByRName = false;
			boolean filterByZip = false;
			boolean filterByCity = false;
			boolean filterByRating = false;
			boolean filterByAge = false;
			boolean filterByMName = false;
			
			boolean groupBy = false;
			boolean groupByCity = false;
			boolean groupByProduct = false;
			boolean groupByRetailerName = false;
			boolean groupByZip = false;
			
			boolean countOnly = false;
						
			//Get the filters selected
			//Filter - Simple Search
			String[] filters = request.getParameterValues("queryCheckBox");
			//Filters - Group By
			String[] extraSettings = request.getParameterValues("extraSettings");
			
			DBCursor dbCursor = null;
			AggregationOutput aggregateData = null;
			
			//Check for extra settings(Grouping Settings)
			if(extraSettings != null && filters == null){				
				//User has selected extra settings
				groupBy = true;
				
				for(int x = 0; x <extraSettings.length; x++){
					switch (extraSettings[x]){						
						case "COUNT_ONLY":
							//Not implemented functionality to return count only
							countOnly = true;				
							break;
						case "GROUP_BY":	
							//Can add more grouping conditions here
							if(groupByDropdown.equals("GROUP_BY_CITY")){
								groupByCity = true;
							}else if(groupByDropdown.equals("GROUP_BY_PRODUCT")){
								groupByProduct = true;
							}else if(groupByDropdown.equals("GROUP_BY_RETAILER")){
								groupByRetailerName = true;
							}else if(groupByDropdown.equals("GROUP_BY_ZIP")){
								groupByZip = true;
							}
							
							break;
					}		
				}				
			}			
			
			//Check the main filters only if the 'groupBy' option is not selected
			if(filters != null && groupBy != true){
				for (int i = 0; i < filters.length; i++) {
					//Check what all filters are ON
					//Build the query accordingly
					switch (filters[i]){										
						case "productName":							
							filterByProduct = true;
							if(!productName.equals("ALL_PRODUCTS")){
								query.put("productName", productName);
							}						
							break;
												
						case "productPrice":
							filterByPrice = true;
							if (comparePrice.equals("EQUALS_TO")) {
								query.put("productPrice", productPrice);
							}else if(comparePrice.equals("GREATER_THAN")){
								query.put("productPrice", new BasicDBObject("$gt", productPrice));
							}else if(comparePrice.equals("LESS_THAN")){
								query.put("productPrice", new BasicDBObject("$lt", productPrice));
							}
							break;
							
						case "retailerName": 
							filterByRName = true;
							if(!retailerName.equals("All")){
								query.put("retailerName", retailerName);
							}							
							break;
												
						case "retailerZip":
							filterByZip = true;
							query.put("retailerZip", retailerZip);
							break;
												
						case "retailerCity": 
							filterByCity = true;
							if(!retailerCity.equals("All") && !groupByCity){
								query.put("retailerCity", retailerCity);
							}							
							break;
							
						case "manufacturerName": 
							filterByMName = true;
							if(!manufacturerName.equals("All")){
								query.put("manufacturerName", manufacturerName);
							}							
							break;
								
							
						case "userAge":
							filterByAge = true;
							if (compareAge.equals("EQUALS_TO")) {
								query.put("userAge", userAge);
							}else if(compareAge.equals("GREATER_THAN")){
								query.put("userAge", new BasicDBObject("$gt", userAge));
							}
							break;
												
						case "reviewRating":	
							filterByRating = true;
							if (compareRating.equals("EQUALS_TO")) {
								query.put("reviewRating", reviewRating);
							}else if(compareRating.equals("GREATER_THAN")){
								query.put("reviewRating", new BasicDBObject("$gt", reviewRating));
							}else{
								query.put("reviewRating", 5);
							}
							break;
													
						default:
							//Show all the reviews if nothing is selected
							noFilter = true;
							break;						
					}				
				}
			}else{
				//Show all the reviews if nothing is selected
				noFilter = true;
			}
			
						
			//Construct the top of the page
			constructPageTop(output);
			if(extraSettings != null && returnValueDropdown.equals("COUNT") && filters!=null){
				
				groupBy = true;
				groupByCity = true;
				filterByRating = true;
				DBObject match = null;
				DBObject groupFields = null;
				DBObject group = null;
				DBObject projectFields = null;
				DBObject project = null;
				AggregationOutput aggregate = null;
				
				if(groupByCity){
					
					if (compareRating.equals("EQUALS_TO")){
						
					match = new BasicDBObject("$match", new BasicDBObject("reviewRating", reviewRating));
					
					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$retailerCity");
					groupFields.put("count", new BasicDBObject("$sum", 1));
					groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					

					group = new BasicDBObject("$group", groupFields);

					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("City", "$_id");
					projectFields.put("Product Count", "$count");
					projectFields.put("Product", "$productName");
					projectFields.put("Rating", "$rating");
					
					project = new BasicDBObject("$project", projectFields);
					
					
					
					aggregate = myReviews.aggregate(match,group, project);
					
					//Construct the page content
					constructGroupByCityCountContent(aggregate, output, countOnly);
						}
					}
				}else if(extraSettings != null && returnValueDropdown.equals("MAX_PRICE") ){
				
				groupBy = true;
				
				for(int x = 0; x <extraSettings.length; x++){
					switch (extraSettings[x]){						
						case "COUNT_ONLY":
							//Not implemented functionality to return count only
							countOnly = true;				
							break;
						case "GROUP_BY":	
							//Can add more grouping conditions here
							if(groupByDropdown.equals("GROUP_BY_CITY")){
								groupByCity = true;
							}else if(groupByDropdown.equals("GROUP_BY_ZIP")){
								groupByZip = true;
							}
							
							break;
					}		
				}	
				
				//DBObject test = null;
				DBObject match = null;
				DBObject groupFields = null;
				DBObject group = null;
				DBObject projectFields = null;
				DBObject project = null;
				AggregationOutput aggregate = null;
				
				if(groupByCity){
					
					DBObject sort = new BasicDBObject("$sort", new BasicDBObject("productPrice",-1) );
					
					
					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$retailerCity");
					groupFields.put("productPrice", new BasicDBObject("$push", "$productPrice"));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					
					group = new BasicDBObject("$group", groupFields);
					
					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("City", "$_id");
					projectFields.put("Product Price", "$productPrice");
					projectFields.put("Product", "$productName");
					
					project = new BasicDBObject("$project", projectFields);
					
					
					
					aggregate = myReviews.aggregate(sort, group,  project);
					
					//Construct the page content
					constructGroupByMPContent(aggregate, output, countOnly);
					}else if(groupByZip){
					
					DBObject sort = new BasicDBObject("$sort", new BasicDBObject("productPrice",-1) );
					
					
					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$retailerZip");
					groupFields.put("productPrice", new BasicDBObject("$push", "$productPrice"));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					
					group = new BasicDBObject("$group", groupFields);
					
					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("Zip Code", "$_id");
					projectFields.put("Product Price", "$productPrice");
					projectFields.put("Product", "$productName");
					
					project = new BasicDBObject("$project", projectFields);
					
					aggregate = myReviews.aggregate(sort, group,  project);
					
					//Construct the page content
					constructGroupByMPZContent(aggregate, output, countOnly);
					}
				}else if(extraSettings != null && returnValueDropdown.equals("TOP_5") ){
				
				groupBy = true;
				groupByCity = true;
				
				DBObject match = null;
				DBObject groupFields = null;
				DBObject group = null;
				DBObject projectFields = null;
				DBObject project = null;
				AggregationOutput aggregate = null;
				
				if(groupByCity){
					
					DBObject sort = new BasicDBObject("$sort", new BasicDBObject("reviewRating",-1) );
					
					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$retailerCity");
					groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					
					group = new BasicDBObject("$group", groupFields);
					
					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("City", "$_id");
					projectFields.put("Product", "$productName");
					projectFields.put("Rating", "$rating");
					
					project = new BasicDBObject("$project", projectFields);
					
					aggregate = myReviews.aggregate(sort, group,  project);
					
					//Construct the page content
					constructGroupByTopRatingContent(aggregate, output, countOnly);
					}
				}					
			//Run the query 
				else if(extraSettings != null && returnValueDropdown.equals("ALL")){
				
				groupBy = true;
				
				for(int x = 0; x <extraSettings.length; x++){
					switch (extraSettings[x]){						
						case "COUNT_ONLY":
							//Not implemented functionality to return count only
							countOnly = true;				
							break;
						case "GROUP_BY":	
							//Can add more grouping conditions here
							if(groupByDropdown.equals("GROUP_BY_CITY")){
								groupByCity = true;
							}else if(groupByDropdown.equals("GROUP_BY_PRODUCT")){
								groupByProduct = true;
							}else if(groupByDropdown.equals("GROUP_BY_RETAILER")){
								groupByRetailerName = true;
							}else if(groupByDropdown.equals("GROUP_BY_ZIP")){
								groupByZip = true;
							}
							
							break;
					}		
				}	
				//Run the query using aggregate function
				DBObject match = null;
				DBObject groupFields = null;
				DBObject group = null;
				DBObject projectFields = null;
				DBObject project = null;
				AggregationOutput aggregate = null;
				
				if(groupByCity){
					
					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$retailerCity");
					groupFields.put("count", new BasicDBObject("$sum", 1));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					groupFields.put("review", new BasicDBObject("$push", "$reviewText"));
					groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
					
					group = new BasicDBObject("$group", groupFields);

					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("City", "$_id");
					projectFields.put("Review Count", "$count");
					projectFields.put("Product", "$productName");
					projectFields.put("User", "$userName");
					projectFields.put("Reviews", "$review");
					projectFields.put("Rating", "$rating");
					
					project = new BasicDBObject("$project", projectFields);
					
					aggregate = myReviews.aggregate(group, project);
												
					//Construct the page content
					constructGroupByCityContent(aggregate, output, countOnly);
					
				}else if(groupByProduct){	

					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$productName");
					groupFields.put("count", new BasicDBObject("$sum", 1));
					groupFields.put("review", new BasicDBObject("$push", "$reviewText"));
					groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
					
					group = new BasicDBObject("$group", groupFields);

					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("Product", "$_id");
					projectFields.put("Review Count", "$count");
					projectFields.put("Reviews", "$review");
					projectFields.put("Rating", "$rating");
										
					project = new BasicDBObject("$project", projectFields);
					
					aggregate = myReviews.aggregate(group, project);				
							
					//Construct the page content
					constructGroupByProductContent(aggregate, output, countOnly);
					
				}else if(groupByRetailerName){
					
					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$retailerName");
					groupFields.put("count", new BasicDBObject("$sum", 1));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					groupFields.put("review", new BasicDBObject("$push", "$reviewText"));
					groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
					
					group = new BasicDBObject("$group", groupFields);

					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("Retailer", "$_id");
					projectFields.put("Review Count", "$count");
					projectFields.put("Product", "$productName");
					
					projectFields.put("Reviews", "$review");
					projectFields.put("Rating", "$rating");
					
					project = new BasicDBObject("$project", projectFields);
					
					aggregate = myReviews.aggregate(group, project);
												
					//Construct the page content
					constructGroupByRetailerNameContent(aggregate, output, countOnly);
					
				}else if(groupByZip){
					
					groupFields = new BasicDBObject("_id", 0);
					groupFields.put("_id", "$retailerZip");
					groupFields.put("count", new BasicDBObject("$sum", 1));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					groupFields.put("review", new BasicDBObject("$push", "$reviewText"));
					groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
					
					group = new BasicDBObject("$group", groupFields);

					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("Zip Code", "$_id");
					projectFields.put("Review Count", "$count");
					projectFields.put("Product", "$productName");
					
					projectFields.put("Reviews", "$review");
					projectFields.put("Rating", "$rating");
					
					project = new BasicDBObject("$project", projectFields);
					
					aggregate = myReviews.aggregate(group, project);
												
					//Construct the page content
					constructGroupByZipContent(aggregate, output, countOnly);
					
				}			 
			}else{
				//Check the return value selected
				int returnLimit = 0;
				
				//Create sort variable
				DBObject sort = new BasicDBObject();
												
				if (returnValueDropdown.equals("TOP_5")){
					//Top 5 - Sorted by review rating
					returnLimit = 5;
					sort.put("reviewRating",-1);
					dbCursor = myReviews.find(query).limit(returnLimit).sort(sort);
				}else if (returnValueDropdown.equals("DISLIKED_5")){
					
					returnLimit = 5;
					sort.put("reviewRating",1);
					dbCursor = myReviews.find(query).limit(returnLimit).sort(sort);
				}else if (returnValueDropdown.equals("LATEST_5")){
					//Latest 5 - Sort by date
					returnLimit = 5;
					sort.put("reviewDate",-1);
					dbCursor = myReviews.find(query).limit(returnLimit).sort(sort);
				}else if (returnValueDropdown.equals("SORTED")){
					
					
					sort.put("userAge",1);
					dbCursor = myReviews.find(query).sort(sort);
				}else{
					//Run the simple search query(default result)
					dbCursor = myReviews.find(query);
				}		
				
				//Construct the page content
				constructDefaultContent(dbCursor, output, countOnly);
			}			
			
			//Construct the bottom of the page
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
	
	public void constructDefaultContent(DBCursor dbCursor, PrintWriter output, boolean countOnly){
		int count = 0;
		String tableData = " ";
		String pageContent = " ";
		
		if (countOnly == false){
		while (dbCursor.hasNext()) {		
			BasicDBObject bobj = (BasicDBObject) dbCursor.next();
			tableData =  "<tr><td>Name: <b>     " + bobj.getString("productName") + " </b></td></tr>"
						+ "<tr><td>Price:       " + bobj.getString("productPrice") + "</br>"
						+ "Retailer:            " + bobj.getString("retailerName") + "</br>"
						+ "Retailer Zipcode:    " + bobj.getString("retailerZip") + "</br>"
						+ "Retailer City:       " + bobj.getString("retailerCity") + "</br>"
						+ "Retailer State:      " + bobj.getString("retailerState") + "</br>"
						+ "Sale:                " + bobj.getString("productOnSale") + "</br>"
						+ "User ID:             " + bobj.getString("userName") + "</br>"
						+ "User Age:            " + bobj.getString("userAge") + "</br>"
						+ "User Gender:         " + bobj.getString("userGender") + "</br>"
						+ "User Occupation:     " + bobj.getString("userOccupation") + "</br>"
						+ "Manufacturer:        " + bobj.getString("manufacturerName") + "</br>"
						+ "Manufacturer Rebate: " + bobj.getString("manufacturerRebate") + "</br>"
						+ "Rating:              " + bobj.getString("reviewRating") + "</br>"
						+ "Date:                " + bobj.getString("reviewDate") + "</br>"
						+ "Review Text:         " + bobj.getString("reviewText") + "</td></tr>";
				
			count++;
			
				output.println("<h3>"+count+"</h3>");
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
		}
		}
		else if (countOnly == true){
			while (dbCursor.hasNext()) {		
			BasicDBObject bobj = (BasicDBObject) dbCursor.next();
			count++;
			}
			output.println("<h3>Total Number of Reviews Found = "+count+"</h3>");
		}
		//No data found
		if(count == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
	
	public void constructGroupByCityContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		
		output.println("<h1> Grouped By - City </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				BasicDBList rating = (BasicDBList) bobj.get("Rating");
				
				rowCount++;
				tableData = "<tr><td>City: "+bobj.getString("City")+"</td>&nbsp"
						+	"<td>Reviews Found: "+bobj.getString("Review Count")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				while (productCount < productList.size()) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Rating: "+rating.get(productCount)+"</br>"
							+	"Review: "+productReview.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
				}	
				
				//Reset product count
				productCount =0;
		}		
		
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
	
	public void constructGroupByCityCountContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		
		output.println("<h1> Count of products with Rating 5 Grouped By - City  </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				BasicDBList rating = (BasicDBList) bobj.get("Rating");
				
				rowCount++;
				tableData = "<tr><td>City: "+bobj.getString("City")+"</td>&nbsp"
						+	"<td>Number of Products: "+bobj.getString("Product Count")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				while (productCount < productList.size()) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Rating: "+rating.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
				}	
				
				//Reset product count
				productCount =0;
		}
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
	
	public void constructGroupByMPContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		
		output.println("<h1> Products with Maximum Price Grouped By - City  </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList priceList = (BasicDBList) bobj.get("Product Price");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				
				
				rowCount++;
				tableData = "<tr><td>City: "+bobj.getString("City")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				while (productCount ==0) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Price: "+priceList.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
				}	
				
				//Reset product count
				productCount =0;
		}
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
	
	public void constructGroupByMPZContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		
		output.println("<h1> Products with Maximum Price Grouped By - Zip Code  </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList priceList = (BasicDBList) bobj.get("Product Price");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				
				
				rowCount++;
				tableData = "<tr><td>Zip Code: "+bobj.getString("Zip Code")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				while (productCount ==0) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Price: "+priceList.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
				}	
				
				//Reset product count
				productCount =0;
		}
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
	
	public void constructGroupByTopRatingContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		
		output.println("<h1> Top 5 Liked Products Grouped By - City  </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList ratingList = (BasicDBList) bobj.get("Rating");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				
				
				rowCount++;
				tableData = "<tr><td>City: "+bobj.getString("City")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				while (productCount <5) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Rating: "+ratingList.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
				}	
				
				//Reset product count
				productCount =0;
		}
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
	
	public void constructGroupByRetailerNameContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		
		output.println("<h1> Grouped By - Retailer Name </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				BasicDBList rating = (BasicDBList) bobj.get("Rating");
				
				rowCount++;
				tableData = "<tr><td>Retailer: "+bobj.getString("Retailer")+"</td>&nbsp"
						+	"<td>Reviews Found: "+bobj.getString("Review Count")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				while (productCount < productList.size()) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Rating: "+rating.get(productCount)+"</br>"
							+	"Review: "+productReview.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
				}	
				
				//Reset product count
				productCount =0;
		}		
		
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
	
	public void constructGroupByZipContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		
		output.println("<h1> Grouped By - Retailer Zip Code </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				BasicDBList rating = (BasicDBList) bobj.get("Rating");
				
				rowCount++;
				tableData = "<tr><td>Retailer Zip Code: "+bobj.getString("Zip Code")+"</td>&nbsp"
						+	"<td>Reviews Found: "+bobj.getString("Review Count")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				while (productCount < productList.size()) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Rating: "+rating.get(productCount)+"</br>"
							+	"Review: "+productReview.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
				}	
				
				//Reset product count
				productCount =0;
		}		
		
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
	
	public void constructGroupByProductContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly){
		int rowCount = 0;
		int reviewCount = 0;
		String tableData = " ";
		String pageContent = " ";
				
		output.println("<h1> Grouped By - Products </h1>");
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				BasicDBList rating = (BasicDBList) bobj.get("Rating");
				
				rowCount++;
				tableData = "<tr><td>Product: "+bobj.getString("Product")+"</td>&nbsp"
						+	"<td>Products Found: "+bobj.getString("Review Count")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				//Now print the products with the given review rating
				while (reviewCount < productReview.size()) {
					tableData = "<tr rowspan = \"3\"><td>Rating: "+rating.get(reviewCount)+"</br>"
								+ "Review: "+productReview.get(reviewCount)+"</td></tr>";
							
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					reviewCount++;					
				}
					
				//Reset review count
				reviewCount = 0;
					
		}		
		
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
}