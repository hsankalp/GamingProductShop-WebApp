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

public class Analytics extends HttpServlet {
	
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
			String productCategory = request.getParameter("productCategory");
			int productPrice = Integer.parseInt(request.getParameter("productPrice"));
			String retailerName = request.getParameter("retailerName");
			String retailerZip = request.getParameter("retailerZip");
			String retailerCity = request.getParameter("retailerCity");
			String retailerState = request.getParameter("retailerState");
			String manufacturerName = request.getParameter("manufacturerName");
			int userAge = Integer.parseInt(request.getParameter("userAge"));
			int reviewRating = Integer.parseInt(request.getParameter("reviewRating"));
			
			String userGender = request.getParameter("userGender");
			String productOnSale = request.getParameter("productOnSale");
			String manufacturerRebate = request.getParameter("manufacturerRebate");
			
			String compareRating = request.getParameter("compareRating");
			String compareAge = request.getParameter("compareAge");
			String comparePrice = request.getParameter("comparePrice");
			
			
			String returnValueDropdown = request.getParameter("returnValue");
			String groupByDropdown = request.getParameter("groupByDropdown");
			
			boolean noFilter = false;
			boolean filterByProduct = false;
			boolean filterByProductCategory = false;
			boolean filterByPrice = false;
			boolean filterByRetailerName = false;
			boolean filterByZip = false;
			boolean filterByState = false;
			boolean filterByCity = false;
			boolean filterByPOS = false;
			boolean filterByManufacturer = false;
			boolean filterByManufacturerRebate = false;
			boolean filterByRating = false;
			boolean filterByAge = false;
			boolean filterByGender = false;
			
			
			boolean groupBy = false;
			boolean groupByProduct = false;
			boolean groupByProductCategory = false;
			boolean groupByRetailerName = false;
			boolean groupByZip = false;
			boolean groupByState = false;
			boolean groupByCity = false;
			boolean groupByPOS = false;
			boolean groupByManufacturer = false;
			boolean groupByManufacturerRebate = false;
			boolean groupByRating = false;
			boolean groupByGender = false;
			
			boolean countOnly = false;
			
			
			boolean istop5 = false;
			boolean ismax = false;
		
			//Get the filters selected
			//Filter - Simple Search
			String[] filters = request.getParameterValues("queryCheckBox");
			//Filters - Group By
			String[] extraSettings = request.getParameterValues("extraSettings");
			
			DBCursor dbCursor = null;
			AggregationOutput aggregateData = null;
			
			DBObject match = null;
			DBObject groupFields = null;
			DBObject group = null;
			DBObject projectFields = null;
			DBObject project = null;
			AggregationOutput aggregate = null;
			DBObject sort = null;
			
			constructPageTop(output);
			
			if(extraSettings !=null){
				
				groupBy = true;
				
			}
			
			
			if(filters != null){
				
				for (int i = 0; i < filters.length; i++) {
					//Check what all filters are ON
					//Build the query accordingly
					switch (filters[i]){										
						case "productName":							
							filterByProduct = true;
							if(!productName.equals("ALL_PRODUCTS")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("productName", productName));
								}else{
								query.put("productName", productName);
								//System.out.print(query);
								}
							}						
							break;
							
						case "productCategory":							
							filterByProductCategory = true;
							if(!productCategory.equals("All")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("productCategory", productCategory));
								}else{
								query.put("productCategory", productCategory);
								}
							}						
							break;
												
						case "productPrice":
							filterByPrice = true;
							if (comparePrice.equals("EQUALS_TO")) {
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("productPrice", productPrice));
								}else{
								query.put("productPrice", productPrice);
								}
							}else if(comparePrice.equals("GREATER_THAN")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("productPrice", new BasicDBObject("$gt", productPrice) ));
								}else{
								query.put("productPrice", new BasicDBObject("$gt", productPrice));
								}
							}else if(comparePrice.equals("LESS_THAN")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("productPrice", new BasicDBObject("$lt", productPrice) ));
								}else{
								query.put("productPrice", new BasicDBObject("$lt", productPrice));
								}
							}
							break;
							
						case "retailerName": 
							filterByRetailerName = true;
							if(!retailerName.equals("All")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("retailerName", retailerName));
								}else{
								query.put("retailerName", retailerName);
								}
							}							
							break;
												
						case "retailerZip":
							filterByZip = true;
							if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("retailerZip", retailerZip));
								}else{
							query.put("retailerZip", retailerZip);
								}
							break;
												
						case "retailerCity": 
							filterByCity = true;
							if(!retailerCity.equals("All")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("retailerCity", retailerCity));
								}else{
								query.put("retailerCity", retailerCity);
								}
							}							
							break;
							
						case "retailerState": 
							filterByState = true;
							if(!retailerState.equals("All")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("retailerState", retailerState));
								}else{
								query.put("retailerState", retailerState);
								}
							}							
							break;
							
						case "productOnSale":
							filterByPOS = true;
							if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("productOnSale", productOnSale));
								}else{
							query.put("productOnSale", productOnSale);
								}
							break;
							
						case "manufacturerName": 
							filterByManufacturer = true;
							if(!manufacturerName.equals("All")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("manufacturerName", manufacturerName));
								}else{
								query.put("manufacturerName", manufacturerName);
								}
							}							
							break;
								
						case "manufacturerRebate":
							filterByManufacturerRebate = true;
							if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("manufacturerRebate", manufacturerRebate));
								}else{
							query.put("manufacturerRebate", manufacturerRebate);
								}
							break;	
						
						case "userAge":
							filterByAge = true;
							if (compareAge.equals("EQUALS_TO")) {
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("userAge", userAge));
								}else{
								query.put("userAge", userAge);
								}
							}else if(compareAge.equals("GREATER_THAN")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("userAge", new BasicDBObject("$gt", userAge)) );
								}else{
								query.put("userAge", new BasicDBObject("$gt", userAge));
								}
							}else if(compareAge.equals("LESS_THAN")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("userAge", new BasicDBObject("$lt", userAge)) );
								}else{
								query.put("userAge", new BasicDBObject("$lt", userAge));
								}
							}
							break;
							
						case "userGender":
							filterByGender = true;
							if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("userGender", userGender));
								}else{
							query.put("userGender", userGender);
								}
							break;
												
						case "reviewRating":	
							filterByRating = true;
							if (compareRating.equals("EQUALS_TO")) {
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("reviewRating", reviewRating));
								}else{
								query.put("reviewRating", reviewRating);
								}
							}else if(compareRating.equals("GREATER_THAN")){
								if(groupBy==true){
								match = new BasicDBObject("$match", new BasicDBObject ("reviewRating", new BasicDBObject("$gt", reviewRating)));
								}else{
								query.put("reviewRating", new BasicDBObject("$gt", reviewRating));
								}
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
				
				
			}
			
			
			if(groupBy == true){
			
			
			if(returnValueDropdown.equals("TOP_5")){
				sort = new BasicDBObject("$sort", new BasicDBObject("reviewRating",-1) );
				istop5 = true;
				
			}else if(returnValueDropdown.equals("MAX_PRICE")){
				sort = new BasicDBObject("$sort", new BasicDBObject("productPrice",-1) );
				ismax = true;
			}else if(returnValueDropdown.equals("COUNT_ONLY")){
				sort = new BasicDBObject("$sort", new BasicDBObject("productPrice",-1) );
				countOnly = true;
										//System.out.print(countOnly);

			}else{
				sort = new BasicDBObject("$sort", new BasicDBObject("productPrice",-1) );
			}
			
			
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
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$retailerCity");
								
							}else if(groupByDropdown.equals("GROUP_BY_PRODUCT")){
								groupByProduct = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$productName");
							}else if(groupByDropdown.equals("GROUP_BY_RETAILER")){
								groupByRetailerName = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$retailerName");
							}else if(groupByDropdown.equals("GROUP_BY_ZIP")){
								groupByZip = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$retailerZip");
							}
							else if(groupByDropdown.equals("GROUP_BY_STATE")){
								groupByState = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$retailerState");
							}
							else if(groupByDropdown.equals("GROUP_BY_POS")){
								groupByPOS = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$productOnSale");
							}
							else if(groupByDropdown.equals("GROUP_BY_MN")){
								groupByManufacturer = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$manufacturerName");
							}
							else if(groupByDropdown.equals("GROUP_BY_MR")){
								groupByManufacturerRebate = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$manufacturerRebate");
							}
							else if(groupByDropdown.equals("GROUP_BY_UGENDER")){
								groupByGender = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$userGender");
							}
							else if(groupByDropdown.equals("GROUP_BY_RATING")){
								groupByRating = true;
								
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$reviewRating");
							}
							else if(groupByDropdown.equals("GROUP_BY_CATEGORY")){
								groupByProductCategory = true;
								groupFields = new BasicDBObject("_id", 0);
								groupFields.put("_id", "$productCategory");
							}
							
							
					}		
				}	
			
					
					
					groupFields.put("count", new BasicDBObject("$sum", 1));
					groupFields.put("productName", new BasicDBObject("$push", "$productName"));
					groupFields.put("productPrice", new BasicDBObject("$push", "$productPrice"));
					groupFields.put("review", new BasicDBObject("$push", "$reviewText"));
					groupFields.put("rating", new BasicDBObject("$push", "$reviewRating"));
					
					
					group = new BasicDBObject("$group", groupFields);

					projectFields = new BasicDBObject("_id", 0);
					projectFields.put("ID", "$_id");
					projectFields.put("Count", "$count");
					projectFields.put("Product", "$productName");
					projectFields.put("Product Price", "$productPrice");
					projectFields.put("User", "$userName");
					projectFields.put("Reviews", "$review");
					projectFields.put("Rating", "$rating");
					
										
					project = new BasicDBObject("$project", projectFields);
					
					
					if(filters != null){
						aggregate = myReviews.aggregate(match,sort,group, project);
					}else{
						aggregate = myReviews.aggregate(sort,group, project);
					}
					
					
					//Construct the page content
					constructGroupByContent(aggregate, output, countOnly, istop5, ismax);
					
					
			}else{
				//Check the return value selected
				int returnLimit = 0;
				
				//Create sort variable
				DBObject sort2 = new BasicDBObject();
												
				if (returnValueDropdown.equals("TOP_5")){
					//Top 5 - Sorted by review rating
					//System.out.print(query);
					returnLimit = 5;
					sort2.put("reviewRating",-1);
					System.out.print(sort);
					dbCursor = myReviews.find(query).limit(returnLimit).sort(sort2);
				}if (returnValueDropdown.equals("MAX_PRICE")){
					//Top 5 - Sorted by review rating
					//System.out.print(query);
					returnLimit = 1;
					sort2.put("reviewRating",-1);
					System.out.print(sort);
					dbCursor = myReviews.find(query).limit(returnLimit).sort(sort2);
				}if (returnValueDropdown.equals("COUNT_ONLY")){
					//Top 5 - Sorted by review rating
					//System.out.print(query);
					countOnly = true;
					dbCursor = myReviews.find(query);
				}else{
					System.out.print(query);
					//Run the simple search query(default result)
					dbCursor = myReviews.find(query);
				}		
				
				//Construct the page content
				constructDefaultContent(dbCursor, output, countOnly);
			}
			
			
			
			
			
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
						+ "Category:            " + bobj.getString("productCategory") + "</br>"
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
	
	
	public void constructGroupByContent(AggregationOutput aggregate, PrintWriter output, boolean countOnly, boolean istop5, boolean ismax){
		int rowCount = 0;
		int productCount = 0;
		String tableData = " ";
		String pageContent = " ";
		int Count = 0;
		
						System.out.print(countOnly);
		
		output.println("<h1> Grouped: </h1>");		
		for (DBObject result : aggregate.results()) {
				BasicDBObject bobj = (BasicDBObject) result;
				BasicDBList productList = (BasicDBList) bobj.get("Product");
				BasicDBList priceList = (BasicDBList) bobj.get("Product Price");
				BasicDBList productReview = (BasicDBList) bobj.get("Reviews");
				BasicDBList rating = (BasicDBList) bobj.get("Rating");
				
				
				
				rowCount++;
				tableData = "<tr><td>ID: "+bobj.getString("ID")+"</td></tr>";
				
				pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
			    output.println(pageContent);
				
				if(istop5 == true){	
				
							
				//Now print the products with the given review rating
				while (productCount < 5) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Price: "+priceList.get(productCount)+"</br>"
							+   "Rating: "+rating.get(productCount)+"</br>"
							+	"Review: "+productReview.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
					
				}
				
				//Reset product count
				productCount =0;
				}else if(ismax == true){	
				
							
				//Now print the products with the given review rating
				while (productCount ==0) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Price: "+priceList.get(productCount)+"</br>"
							+   "Rating: "+rating.get(productCount)+"</br>"
							+	"Review: "+productReview.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
					
				}
				
				//Reset product count
				productCount =0;
				}else if(countOnly == true){	
				
							
				//Now print the products with the given review rating
				while (productCount < productList.size()) {
					/* tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Price: "+priceList.get(productCount)+"</br>"
							+   "Rating: "+rating.get(productCount)+"</br>"
							+	"Review: "+productReview.get(productCount)+"</td></tr>"; */
					
					productCount++;					
					
				}
				output.println("<h3>Count:"+productCount+"</h3>");
				//Reset product count
				productCount =0;
				}else{
					while (productCount < productList.size()) {
					tableData = "<tr rowspan = \"3\"><td> Product: "+productList.get(productCount)+"</br>"
							+   "Price: "+priceList.get(productCount)+"</br>"
							+   "Rating: "+rating.get(productCount)+"</br>"
							+	"Review: "+productReview.get(productCount)+"</td></tr>";
												
					pageContent = "<table class = \"query-table\">"+tableData+"</table>";		
					output.println(pageContent);
					
					productCount++;					
				}
				output.println("<h3>Count:"+productCount+"</h3>");
				productCount =0;
				}
		}		
		
		//No data found
		if(rowCount == 0){
			pageContent = "<h1>No Data Found</h1>";
			output.println(pageContent);
		}
		
	}
}
