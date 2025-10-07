import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private List<CartItem> items;
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status;
    
    public Order(String orderId, String customerName, String customerEmail, List<CartItem> items) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.items = new ArrayList<>(items);
        this.orderDate = LocalDateTime.now();
        this.status = "Pending";
        calculateTotal();
    }
    
    private void calculateTotal() {
        totalAmount = items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
    
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public List<CartItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getFormattedDate() {
        return orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    @Override
    public String toString() {
        return String.format("Order #%s - %s - $%.2f (%s)", orderId, customerName, totalAmount, status);
    }
}