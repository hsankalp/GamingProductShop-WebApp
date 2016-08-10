import java.util.HashMap;

public class ProductData {
    
    private HashMap products = new HashMap();
    

    public HashMap getProducts() {
        return products;
    }
    
    public ProductData() {
        
			products.put("1", new Product("1","X Box 360","Microsoft Gaming Console",300.0));
			products.put("2", new Product("2","X Box One","Microsoft Gaming Console",500.0));
			products.put("3", new Product("3","PlayStation 3","Sony Gaming Console",220.0));
			products.put("4", new Product("4","PlayStation 4","Sony Gaming Console",400.0));
			products.put("5", new Product("5","Wii","Nintendo Gaming Console",300.0));
			products.put("6", new Product("6","WiiU","Nintendo Gaming Console",200.0));
			products.put("7", new Product("7","Fifa 2015","Electronic Arts Game",40.0));
			products.put("8", new Product("8","Titanfall","Electronic Arts Game",20.0));
			products.put("9", new Product("9","Call of Duty: Advanced Warfare","ActiVision Game",60.0));
			products.put("10", new Product("10","Call of Duty: Black Ops","ActiVision Game",90.0));
			products.put("11", new Product("11","NBA 2K15","Take-Two Interactive Game",50.0));
			products.put("12", new Product("12","WWE 2K15","Take-Two Interactive Game",30.0));
    }

}
