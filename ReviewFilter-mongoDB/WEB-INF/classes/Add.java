import java.io.*;

import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession; 
import java.io.*;


import java.util.*;

public class Add extends HttpServlet implements java.io.Serializable {  
        protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                                throws ServletException, IOException { 
								
			HttpSession session=request.getSession();								
            response.setContentType("text/html"); 			
            PrintWriter out=response.getWriter(); 
			//FileWriter file = new FileWriter("Usernames.txt"); 
			File file= new File("Products.txt");
			//HashMap hashmap=new HashMap<String,String>();
			HashMap mapOutFile=new HashMap<String,String>();
			HashMap<String,String> h=new HashMap<String,String>();
			String productName = request.getParameter("productName");
					
			String productPrice = request.getParameter("Value");
			
			h.put(productName,productPrice);
			if(!file.exists()){
    			file.createNewFile();				
    		}
						
			try{
    			FileInputStream fis=new FileInputStream(file);
    			ObjectInputStream pw=new ObjectInputStream(fis);
    			mapOutFile = (HashMap<String,String>)pw.readObject();
    			for(Map.Entry<String, String> entry :h.entrySet() ){
    				String k = entry.getKey();
    				String v = entry.getValue();
    				

    				mapOutFile.put(k, v);
    			}
    			pw.close();
    			fis.close();    			
    			}
    			catch (Exception ex) {
    		         ex.printStackTrace();
    		      }
    		
			
			try{
			FileOutputStream fos=new FileOutputStream(file,false);
			ObjectOutputStream pw=new ObjectOutputStream(fos);
			
			pw.writeObject(mapOutFile);
			
        pw.flush();
        pw.close();
        fos.close();
		} catch (Exception ex) {
         ex.printStackTrace();
      }
			out.println("<h1>Product Added<h1>");
		    session.invalidate();  
            out.close();  
    }  
}  



