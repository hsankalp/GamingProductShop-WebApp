

/** A catalog that lists the items available in inventory.
 *  <P>
 *  Taken from Core Servlets and JavaServer Pages 2nd Edition
 *  from Prentice Hall and Sun Microsystems Press,
 *  http://www.coreservlets.com/.
 *  &copy; 2003 Marty Hall; may be freely used or adapted.
 */

public class Catalog {
  
  private static CatalogItem[] items =
    {       
			products.put("X Box 360", new CatalogItem("X Box 360","<I><B>X Box 360</B></I>","Microsoft Gaming Console",300));
			products.put("X Box One", new CatalogItem("X Box One","<I><B>X Box One</B></I>","Microsoft Gaming Console",500));
			products.put("PlayStation 3", new CatalogItem("PlayStation 3","<I><B>PlayStation 3</B></I>","Sony Gaming Console",220));
			products.put("PlayStation 4", new CatalogItem("PlayStation 4","<I><B>PlayStation 4</B></I>","Sony Gaming Console",400));
			products.put("Wii", new CatalogItem("Wii","<I><B>Wii</B></I>","Nintendo Gaming Console",300));
			products.put("WiiU", new CatalogItem("WiiU","<I><B>WiiU</B></I>","Nintendo Gaming Console",200));
			products.put("Fifa 2015", new CatalogItem("Fifa 2015","<I><B>Fifa 2015</B></I>","Football",40));
			products.put("Titanfall", new CatalogItem("Titanfall","<I><B>Titanfall</B></I>","Action",20));
			products.put("Call of Duty: Advanced Warfare", new CatalogItem("Call of Duty: Advanced Warfare","<I><B>Call of Duty: Advanced Warfare</B></I>","Shooting",60));
			products.put("Call of Duty: Black Ops", new CatalogItem("Call of Duty: Black Ops","<I><B>Call of Duty: Black Ops</B></I>","Shooting",90));
			products.put("NBA 2K15", new CatalogItem("NBA 2K15","<I><B>NBA 2K15</B></I>","Basketball",50));
			products.put("WWE 2K15", new CatalogItem("WWE 2K15","<I><B>WWE 2K15</B></I>","Wrestling",30));
			
};
  public static CatalogItem getItem(String itemID) {
    CatalogItem item;
    if (itemID == null) {
      return(null);
    }
    for(int i=0; i<items.length; i++) {
      item = items[i];
      if (itemID.equals(item.getItemID())) {
        return(item);
      }
    }
    return(null);
  }
}
               
