import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public abstract class CatalogPage extends HttpServlet {
  private CatalogItem[] items;
  private String[] itemIDs;
  private String title;

  protected void setItems(String[] itemIDs) {
    this.itemIDs = itemIDs;
    items = new CatalogItem[itemIDs.length];
    for(int i=0; i<items.length; i++) {
      items[i] = Catalog.getItem(itemIDs[i]);
    }
  }
 
  protected void setTitle(String title) {
    this.title = title;
  }

  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    if (items == null) {
      response.sendError(response.SC_NOT_FOUND,
                         "Missing Items.");
      return;
    }
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String docType =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
      "Transitional//EN\">\n";
    out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
                
                "<H1 ALIGN=\"CENTER\">" + title + "</H1>");
    CatalogItem item;
    for(int i=0; i<items.length; i++) {
      out.println("<HR>");
      item = items[i];
      // Show error message if subclass lists item ID
      // that's not in the catalog.
      if (item == null) {
        out.println("<FONT COLOR=\"RED\">" +
                    "Unknown item ID " + itemIDs[i] +
                    "</FONT>");
      } else {
        out.println();
        String formURL =
          "OrderPage";
		String reviewURL = "WriteReview";
		String viewURL = "ViewReviews";
        // Pass URLs that reference own site through encodeURL.
        formURL = response.encodeURL(formURL);
		reviewURL = response.encodeURL(reviewURL);
		viewURL = response.encodeURL(viewURL);
		
        out.println
          ("<FORM ACTION=\"" + formURL + "\">\n" +
           "<INPUT TYPE=\"HIDDEN\" NAME=\"itemID\" " +
           "       VALUE=\"" + item.getItemID() + "\">\n" +
           "<H2>" + item.getShortDescription() +
           " :$" + item.getCost() + "</H2>\n" +
           item.getLongDescription() + "\n" +
           "<P>\n<CENTER>\n" +
           "<INPUT TYPE=\"SUBMIT\" " +
           "VALUE=\"Add to Shopping Cart\">\n" +
           "</CENTER>\n<P>\n</FORM>"+
		   "<FORM ACTION=\"" + reviewURL + "\">\n" +
           "<INPUT TYPE=\"HIDDEN\" NAME=\"itemID\" " +
           "VALUE=\"" + item.getItemID() + "\">\n" +
		   "<P>\n<CENTER>\n" +
		   "<INPUT TYPE=\"SUBMIT\" " +
		   "VALUE=\"Write Review\">\n" +
		   "</CENTER>\n<P>\n</FORM>"+
		   "<FORM ACTION=\"" + viewURL + "\">\n" +
           "<INPUT TYPE=\"HIDDEN\" NAME=\"itemID\" " +
           "VALUE=\"" + item.getItemID() + "\">\n" +
		   "<P>\n<CENTER>\n" +
		   "<INPUT TYPE=\"SUBMIT\" " +
		   "VALUE=\"View Review\">\n" +
		   "</CENTER>\n<P>\n</FORM>"
		   );
		   }
    }
    out.println("<HR>\n</BODY></HTML>");
  }
}
