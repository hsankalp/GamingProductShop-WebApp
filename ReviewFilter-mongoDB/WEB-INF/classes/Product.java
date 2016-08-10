
public class Product {

    private String id;
    private String firstName;
    private String category;
    private double cost;
    
    
    public Product (String id, String firstName, String category, double cost) {
        this.id = id;
        this.firstName = firstName;
        this.category = category;
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }
    
    public String getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public double getCost() {
        return cost;
    }
}