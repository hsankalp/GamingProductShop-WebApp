/*
 * LoginServlet.java
 *
 */
 

import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class Remove extends HttpServlet implements java.io.Serializable {
   
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    

    /**
     * Actually shows the <code>HTML</code> result page
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
		
		
		
        String productName = request.getParameter("productName");
        
		HttpSession session = request.getSession(true);
		
		
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		
		synchronized(session){
        
        if(productName != null) {
				
			
				
				out.println("<html>");
				out.println("<body>");
				String sRootPath = new File("Products.txt").getAbsolutePath();
				File file = new File(sRootPath);
				Map<String, String> mapOutFile = new HashMap<String, String>();
				if (!file.exists()) {
					file.createNewFile();
					}else{
				try {
				File toRead = new File(sRootPath);
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream pw = new ObjectInputStream(fis);
				mapOutFile = (HashMap<String, String>) pw.readObject();
				for (Map.Entry<String, String> map : mapOutFile.entrySet()) {
					if (map.getKey().equals(productName))
							{

							
							mapOutFile.remove(map.getKey());
						

							} 
									pw.close();
									fis.close();
								}
						out.print("Login Failure!");
						out.println("<br><br><a href='index.html'> HOME </a>");
									
			} catch (Exception ex) {
				ex.printStackTrace();
					}
				}
				out.println("</body>");
				out.println("</html>");
				out.close();
			}
			
			 
        
		else {
            out.println("Login Failure!  You must supply a username and password");
			out.println("<br><br><a href='index.html'> HOME </a>");
        }
		}
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
