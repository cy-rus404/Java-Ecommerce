public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String description;
    
    public Product(String id, String name, String category, double price, int stock, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getDescription() { return description; }
    
    public void setStock(int stock) { this.stock = stock; }
    public void setPrice(double price) { this.price = price; }
    
    public boolean reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("%s - $%.2f (Stock: %d)", name, price, stock);
    }
}