/*
 * LoginStaff.java
 *
 */
 

import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class LoginStaff extends HttpServlet implements java.io.Serializable {
   
    protected Map users = new HashMap();
    /** 
     * Initializes the servlet with some usernames/password
    */  
     public void init() {
                users.put("store", "manager");
				users.put("sales","man");
    } 
 
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    

    /**
     * Actually shows the <code>HTML</code> result page
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
		
		
		
        String userid = request.getParameter("userid");
        String password = request.getParameter("password");
		
		HttpSession session = request.getSession(true);
		
		synchronized(session){
		
        if(userid != null && userid.length() != 0) {
            userid = userid.trim();
        }
        if(password != null && password.length() != 0) {
            password = password.trim();
        }
        if(userid != null &&
            password != null && userid!="") {
				
			
					
                String realpassword = (String)users.get(userid);
                if(realpassword != null &&
                    realpassword.equals(password) && password.equals("manager")) {
                    showPage(response,"Login Success for Store Manager!");
                } 
				else if (realpassword != null &&
                    realpassword.equals(password) && password.equals("man")) {
                    showPage(response,"Login Success for Salesman!");
					}
				else {
                    showPage(response,"Login Failure! Username or password is incorrect");
                }
			
			}
        
		else {
            showPage(response,"Login Failure!  You must supply a username and password");
        }
		}
    } 
	
	protected void showPage(HttpServletResponse response, String message)
    throws ServletException, java.io.IOException {
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
		out.println("<meta http-equiv="+"Content-Type"+" content="+"text/html; charset=utf-8"+" />");
		out.println("<title>CSP 595 - Enterprise Web Application</title>");
		out.println("<link rel="+"stylesheet"+" href="+"styles.css"+" type="+"text/css"+" />");
        out.println("<title>Login Servlet Result</title>");  
        out.println("</head>");
        out.println("<body>");
/* 		out.println("<div id="+"container>");
        //out.println("<h2>" + message + "</h2>");
		out.println("<header>");
    	out.println("<h1><a href="+"index.html"+">Game<span>Speed</span></a></h1>");
        out.println("<h2>Buy and Sell New and Pre-owned Game Consoles, Games and Tablets</h2><br>");
		//out.println("<a href="+"cart.html"+"class="+"button"+" style="+"float: right;"+" >My Shopping Cart</a>");
		out.println("<br><br>");
		out.println("</header>");
		out.println("<nav>");
    	out.println("<ul>");
        out.println("<li><a href="+"index.html"+">Home</a></li>");
        out.println("<li><a href="+"XBox.html"+">Microsoft</a></li>");
        out.println("<li><a href="+"PlayStation.html"+">Sony</a></li>");
        out.println("<li><a href="+"Wii.html"+">Nintendo</a></li>");
		out.println("<li><a href="+"EA.html"+">Electronic Arts</a></li>");
		out.println("<li><a href="+"AV.html"+">Activision</a></li>");
		out.println("<li><a href="+"TTI.html"+">Take-Two Interactive</a></li>");
		out.println("<li class="+"end"+"><a href="+"#"+">Accessories</a></li>");
		out.println("</ul>");
		out.println("</nav>");
		out.println("<div id="+"body"+">");		
		out.println("<section id="+"content"+">");
	    out.println("<article>"); */
		out.println("<h2>"+message+"</h2>");
		if(message == "Login Success for Store Manager!"){
			out.println("<br><br>");
										
			out.println("<form method=\"get\" action=\"Add\">");
			out.println("<fieldset>");
			out.println("<h1>Add Product</h1>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td> Product Name: </td>");
			out.println("<td> <input type=\"text\" name=\"productName\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Product Price: </td>");
			out.println("<td> <input type=\"text\" name=\"Value\"> </td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<input type=\"submit\" value=\"ADD\">");
			out.println("</fieldset>");
			out.println("</form>");
			out.println("<br><br>");
										
			out.println("<form method=\"get\" action=\"Remove\">");
			out.println("<fieldset>");
			out.println("<h1>Remove Product</h1>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td> Product Name: </td>");
			out.println("<td> <input type=\"text\" name=\"productName\"> </td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<input type=\"submit\" value=\"REMOVE\">");
			out.println("</fieldset>");
			out.println("</form>");
			out.println("<br><br>");
										
			out.println("<form method=\"get\" action=\"Update\">");
			out.println("<fieldset>");
			out.println("<h1>Update Product</h1>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td> Product Name: </td>");
			out.println("<td> <input type=\"text\" name=\"productName\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Product Price: </td>");
			out.println("<td> <input type=\"text\" name=\"Value\"> </td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<input type=\"submit\" value=\"UPDATE\">");
			out.println("</fieldset>");
			out.println("</form>");
			
		}
		else if(message == "Login Success for Salesman!"){
			out.println("<br><br>");
										
			out.println("<form method=\"get\" action=\"Add\">");
			out.println("<fieldset>");
			out.println("<h1>Add Customer Order</h1>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td> Customer Order: </td>");
			out.println("<td> <input type=\"text\" name=\"Value\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Product Name: </td>");
			out.println("<td> <input type=\"text\" name=\"productName\"> </td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<input type=\"submit\" value=\"ADD\">");
			out.println("</fieldset>");
			out.println("</form>");
			out.println("<br><br>");
										
			out.println("<form method=\"get\" action=\"Remove\">");
			out.println("<fieldset>");
			out.println("<h1>Remove Customer Order</h1>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td> Customer Order: </td>");
			out.println("<td> <input type=\"text\" name=\"Value\"> </td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<input type=\"submit\" value=\"REMOVE\">");
			out.println("</fieldset>");
			out.println("</form>");
			out.println("<br><br>");
										
			out.println("<form method=\"get\" action=\"Update\">");
			out.println("<fieldset>");
			out.println("<h1>Update Product</h1>");
			out.println("<table>");
			out.println("<tr>");
			out.println("<td> Customer Order: </td>");
			out.println("<td> <input type=\"text\" name=\"Value\"> </td>");
			out.println("</tr>");
			out.println("<tr>");
			out.println("<td> Product Name: </td>");
			out.println("<td> <input type=\"text\" name=\"productName\"> </td>");
			out.println("</tr>");
			out.println("</table>");
			out.println("<input type=\"submit\" value=\"UPDATE\">");
			out.println("</fieldset>");
			out.println("</form>");
			
			out.println("<a href="+"signup.html"+" class="+"button"+">Create Account</a>");
			out.println("<br><br>");
			
		}
/* 		out.println("</article>");
		out.println("</section>");
        out.println("<aside class="+"sidebar"+">");
		out.println("<ul>");	
        out.println("<li>");
        out.println("<h4>Game Consoles</h4>");
        out.println("<ul>");
        out.println("<li><a href="+"XBox.html"+">Microsoft</a></li>");
        out.println("<li><a href="+"PlayStation.html"+">Sony</a></li>");
		out.println("<li><a href="+"Wii.html"+">Nintendo</a></li>");				
        out.println("<li><a href="+"#"+">Accessories</a></li>");                                
        out.println("</ul>");
        out.println("</li>");
        out.println("<li>");
        out.println("<h4>Games</h4>");
        out.println("<ul>");
        out.println("<li><a href="+"EA.html"+">Electronic Arts</a></li>");
        out.println("<li><a href="+"AV.html"+">Activision</a></li>");
        out.println("<li><a href="+"TTI.html"+">Take-Two Interactive</a></li>");
        out.println("</ul>");
        out.println("</li>");
		out.println("<li>");
        out.println("<h4>Contact us</h4>");
        out.println("<ul>");
        out.println("<li class="+"text"+">");
        out.println("<p style="+"margin: 0;"+">1234 South King Drive,");
		out.println("Chicago,");
		out.println("Illinois - 60616");
		out.println("Contact: 123456789</p>");
        out.println("</li>");
        out.println("</ul>");
        out.println("</li>");
        out.println("<li>");
		out.println("<h4>Search site</h4>");
		out.println("<ul>");
		out.println("<li class="+"text"+">");
		out.println("<form method="+"get"+" class="+"searchform"+" action="+"#"+" >");
		out.println("<p>");
		out.println("<input type="+"text"+" size="+"25"+" name="+"s"+" class="+"s"+" />");
		out.println("</p>");
		out.println("</form>");	
		out.println("</li>");
		out.println("</ul>");
		out.println("</li>");
		out.println("<li>");
		out.println("<h4>Games Information</h4>");
		out.println("<ul>");
		out.println("<li><a href="+"http://www.ea.com/pc"+" title="+"ea games"+">Electronic Arts</a></li>");
		out.println("<li><a href="+"https://www.activision.com/games"+" title="+"activision games"+">Activision</a></li>");
		out.println("<li><a href="+"http://www.take2games.com/games/"+" title="+"tti games"+">Take-Two Interactive</a></li>");
		out.println("</ul>");
		out.println("</li>");
		out.println("</ul>");
		out.println("</aside>");
		out.println("<div class="+"clear"+"></div>");
		out.println("</div>");
		out.println("<footer>");
		out.println("<div class="+"footer-content"+">");
		out.println("<ul>");
		out.println("<li><h4>Connect With Us</h4></li>");
		out.println("<li><a href="+"#"+">Help</a></li>");
		out.println("<li><a href="+"#"+">Forum</a></li>");
		out.println("<li><a href="+"#"+">Facebook</a></li>");
		out.println("</ul>");
		out.println("<div class="+"clear"+"></div>");
		out.println("</div>");
		out.println("<div class="+"footer-bottom"+">");
		out.println("<p>CSP 595 - Enterprise Web Application</p>");
		out.println("</div>");
		out.println("</footer>");
		out.println("</div>"); */
		out.println("</body>");
		out.println("</html>");
		out.close();
 
    } 
	
	   
    /** Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
}
