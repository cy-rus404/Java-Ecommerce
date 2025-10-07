import java.util.*;

public class ECommerceSystem {
    private Map<String, Product> products;
    private List<CartItem> cart;
    private List<Order> orders;
    private int orderCounter;
    
    public ECommerceSystem() {
        products = new HashMap<>();
        cart = new ArrayList<>();
        orders = new ArrayList<>();
        orderCounter = 1000;
    }
    
    // Product Management
    public boolean addProduct(Product product) {
        if (products.containsKey(product.getId())) {
            return false;
        }
        products.put(product.getId(), product);
        return true;
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    
    public List<Product> getProductsByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : products.values()) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                result.add(product);
            }
        }
        return result;
    }
    
    public Product getProduct(String id) {
        return products.get(id);
    }
    
    // Cart Management
    public boolean addToCart(String productId, int quantity) {
        Product product = products.get(productId);
        if (product == null || product.getStock() < quantity) {
            return false;
        }
        
        // Check if item already in cart
        for (CartItem item : cart) {
            if (item.getProduct().getId().equals(productId)) {
                int newQuantity = item.getQuantity() + quantity;
                if (product.getStock() >= newQuantity) {
                    item.setQuantity(newQuantity);
                    return true;
                }
                return false;
            }
        }
        
        cart.add(new CartItem(product, quantity));
        return true;
    }
    
    public boolean removeFromCart(String productId) {
        return cart.removeIf(item -> item.getProduct().getId().equals(productId));
    }
    
    public List<CartItem> getCart() {
        return new ArrayList<>(cart);
    }
    
    public double getCartTotal() {
        return cart.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
    
    public void clearCart() {
        cart.clear();
    }
    
    // Order Management
    public Order checkout(String customerName, String customerEmail) {
        if (cart.isEmpty()) {
            return null;
        }
        
        // Check stock availability
        for (CartItem item : cart) {
            if (!item.getProduct().reduceStock(item.getQuantity())) {
                return null; // Insufficient stock
            }
        }
        
        String orderId = "ORD" + (++orderCounter);
        Order order = new Order(orderId, customerName, customerEmail, cart);
        orders.add(order);
        clearCart();
        return order;
    }
    
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
    
    public Order getOrder(String orderId) {
        return orders.stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }
    
    public boolean updateOrderStatus(String orderId, String status) {
        Order order = getOrder(orderId);
        if (order != null) {
            order.setStatus(status);
            return true;
        }
        return false;
    }
    
    // Search functionality
    public List<Product> searchProducts(String keyword) {
        List<Product> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Product product : products.values()) {
            if (product.getName().toLowerCase().contains(lowerKeyword) ||
                product.getCategory().toLowerCase().contains(lowerKeyword) ||
                product.getDescription().toLowerCase().contains(lowerKeyword)) {
                results.add(product);
            }
        }
        return results;
    }
    
    public Set<String> getCategories() {
        Set<String> categories = new HashSet<>();
        for (Product product : products.values()) {
            categories.add(product.getCategory());
        }
        return categories;
    }
}