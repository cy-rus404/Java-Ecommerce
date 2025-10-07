import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ECommerceGUI extends JFrame {
    private ECommerceSystem ecommerce;
    private JTabbedPane tabbedPane;
    
    // Product Management Tab
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTextField prodIdField, prodNameField, prodCategoryField, prodPriceField, prodStockField, prodDescField;
    
    // Shopping Tab
    private JTable shopTable;
    private DefaultTableModel shopTableModel;
    private JList<CartItem> cartList;
    private DefaultListModel<CartItem> cartListModel;
    private JLabel cartTotalLabel;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    
    // Orders Tab
    private JTable orderTable;
    private DefaultTableModel orderTableModel;
    
    public ECommerceGUI() {
        ecommerce = new ECommerceSystem();
        loadSampleData();
        initializeGUI();
        refreshAllTables();
    }
    
    private void initializeGUI() {
        setTitle("eCommerce Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Product Management", createProductManagementTab());
        tabbedPane.addTab("Shopping", createShoppingTab());
        tabbedPane.addTab("Orders", createOrdersTab());
        
        add(tabbedPane);
        setSize(1200, 700);
        setLocationRelativeTo(null);
    }
    
    private JPanel createProductManagementTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Input form
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add/Edit Product"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        prodIdField = new JTextField(10);
        inputPanel.add(prodIdField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        prodNameField = new JTextField(15);
        inputPanel.add(prodNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        prodCategoryField = new JTextField(10);
        inputPanel.add(prodCategoryField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        inputPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 3;
        prodPriceField = new JTextField(10);
        inputPanel.add(prodPriceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        prodStockField = new JTextField(10);
        inputPanel.add(prodStockField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 3;
        prodDescField = new JTextField(20);
        inputPanel.add(prodDescField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Product");
        JButton clearBtn = new JButton("Clear");
        
        addBtn.addActionListener(e -> addProduct());
        clearBtn.addActionListener(e -> clearProductFields());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(clearBtn);
        
        // Table
        String[] columns = {"ID", "Name", "Category", "Price", "Stock", "Description"};
        productTableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(productTableModel);
        
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    loadProductToFields(row);
                }
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createShoppingTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchProducts());
        searchPanel.add(searchBtn);
        
        searchPanel.add(new JLabel("Category:"));
        categoryFilter = new JComboBox<>();
        categoryFilter.addActionListener(e -> filterByCategory());
        searchPanel.add(categoryFilter);
        
        JButton showAllBtn = new JButton("Show All");
        showAllBtn.addActionListener(e -> showAllProducts());
        searchPanel.add(showAllBtn);
        
        // Products table
        String[] shopColumns = {"ID", "Name", "Category", "Price", "Stock"};
        shopTableModel = new DefaultTableModel(shopColumns, 0);
        shopTable = new JTable(shopTableModel);
        
        // Cart panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        
        cartListModel = new DefaultListModel<>();
        cartList = new JList<>(cartListModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        cartTotalLabel = new JLabel("Total: $0.00");
        cartTotalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        JPanel cartButtonPanel = new JPanel(new FlowLayout());
        JButton addToCartBtn = new JButton("Add to Cart");
        JButton removeFromCartBtn = new JButton("Remove from Cart");
        JButton checkoutBtn = new JButton("Checkout");
        
        addToCartBtn.addActionListener(e -> addToCart());
        removeFromCartBtn.addActionListener(e -> removeFromCart());
        checkoutBtn.addActionListener(e -> checkout());
        
        cartButtonPanel.add(addToCartBtn);
        cartButtonPanel.add(removeFromCartBtn);
        cartButtonPanel.add(checkoutBtn);
        
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        cartPanel.add(cartTotalLabel, BorderLayout.NORTH);
        cartPanel.add(cartButtonPanel, BorderLayout.SOUTH);
        
        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(shopTable), BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, cartPanel);
        splitPane.setDividerLocation(700);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createOrdersTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] orderColumns = {"Order ID", "Customer", "Email", "Date", "Total", "Status"};
        orderTableModel = new DefaultTableModel(orderColumns, 0);
        orderTable = new JTable(orderTableModel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton viewDetailsBtn = new JButton("View Details");
        JButton updateStatusBtn = new JButton("Update Status");
        
        viewDetailsBtn.addActionListener(e -> viewOrderDetails());
        updateStatusBtn.addActionListener(e -> updateOrderStatus());
        
        buttonPanel.add(viewDetailsBtn);
        buttonPanel.add(updateStatusBtn);
        
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Product Management Methods
    private void addProduct() {
        try {
            String id = prodIdField.getText().trim();
            String name = prodNameField.getText().trim();
            String category = prodCategoryField.getText().trim();
            double price = Double.parseDouble(prodPriceField.getText().trim());
            int stock = Integer.parseInt(prodStockField.getText().trim());
            String description = prodDescField.getText().trim();
            
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name are required!");
                return;
            }
            
            Product product = new Product(id, name, category, price, stock, description);
            if (ecommerce.addProduct(product)) {
                refreshAllTables();
                clearProductFields();
                JOptionPane.showMessageDialog(this, "Product added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Product ID already exists!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }
    
    private void loadProductToFields(int row) {
        prodIdField.setText((String) productTableModel.getValueAt(row, 0));
        prodNameField.setText((String) productTableModel.getValueAt(row, 1));
        prodCategoryField.setText((String) productTableModel.getValueAt(row, 2));
        prodPriceField.setText(productTableModel.getValueAt(row, 3).toString().replace("$", ""));
        prodStockField.setText(productTableModel.getValueAt(row, 4).toString());
        prodDescField.setText((String) productTableModel.getValueAt(row, 5));
    }
    
    private void clearProductFields() {
        prodIdField.setText("");
        prodNameField.setText("");
        prodCategoryField.setText("");
        prodPriceField.setText("");
        prodStockField.setText("");
        prodDescField.setText("");
    }
    
    // Shopping Methods
    private void searchProducts() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            List<Product> results = ecommerce.searchProducts(keyword);
            updateShopTable(results);
        }
    }
    
    private void filterByCategory() {
        String category = (String) categoryFilter.getSelectedItem();
        if (category != null && !category.equals("All Categories")) {
            List<Product> results = ecommerce.getProductsByCategory(category);
            updateShopTable(results);
        }
    }
    
    private void showAllProducts() {
        updateShopTable(ecommerce.getAllProducts());
    }
    
    private void addToCart() {
        int row = shopTable.getSelectedRow();
        if (row >= 0) {
            String productId = (String) shopTableModel.getValueAt(row, 0);
            String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity:", "1");
            
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (ecommerce.addToCart(productId, quantity)) {
                    refreshCart();
                    JOptionPane.showMessageDialog(this, "Added to cart!");
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient stock or invalid product!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product!");
        }
    }
    
    private void removeFromCart() {
        CartItem selected = cartList.getSelectedValue();
        if (selected != null) {
            ecommerce.removeFromCart(selected.getProduct().getId());
            refreshCart();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item from cart!");
        }
    }
    
    private void checkout() {
        if (ecommerce.getCart().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        
        String name = JOptionPane.showInputDialog(this, "Enter customer name:");
        if (name == null || name.trim().isEmpty()) return;
        
        String email = JOptionPane.showInputDialog(this, "Enter customer email:");
        if (email == null || email.trim().isEmpty()) return;
        
        Order order = ecommerce.checkout(name.trim(), email.trim());
        if (order != null) {
            refreshAllTables();
            JOptionPane.showMessageDialog(this, "Order placed successfully! Order ID: " + order.getOrderId());
        } else {
            JOptionPane.showMessageDialog(this, "Checkout failed! Please check stock availability.");
        }
    }
    
    // Order Methods
    private void viewOrderDetails() {
        int row = orderTable.getSelectedRow();
        if (row >= 0) {
            String orderId = (String) orderTableModel.getValueAt(row, 0);
            Order order = ecommerce.getOrder(orderId);
            
            if (order != null) {
                StringBuilder details = new StringBuilder();
                details.append("Order ID: ").append(order.getOrderId()).append("\n");
                details.append("Customer: ").append(order.getCustomerName()).append("\n");
                details.append("Email: ").append(order.getCustomerEmail()).append("\n");
                details.append("Date: ").append(order.getFormattedDate()).append("\n");
                details.append("Status: ").append(order.getStatus()).append("\n\n");
                details.append("Items:\n");
                
                for (CartItem item : order.getItems()) {
                    details.append("- ").append(item.toString()).append("\n");
                }
                
                details.append("\nTotal: $").append(String.format("%.2f", order.getTotalAmount()));
                
                JOptionPane.showMessageDialog(this, details.toString(), "Order Details", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order!");
        }
    }
    
    private void updateOrderStatus() {
        int row = orderTable.getSelectedRow();
        if (row >= 0) {
            String orderId = (String) orderTableModel.getValueAt(row, 0);
            String[] statuses = {"Pending", "Processing", "Shipped", "Delivered", "Cancelled"};
            
            String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Status",
                    JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);
            
            if (newStatus != null) {
                ecommerce.updateOrderStatus(orderId, newStatus);
                refreshOrderTable();
                JOptionPane.showMessageDialog(this, "Status updated!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order!");
        }
    }
    
    // Refresh Methods
    private void refreshAllTables() {
        refreshProductTable();
        refreshShopTable();
        refreshCart();
        refreshOrderTable();
        updateCategoryFilter();
    }
    
    private void refreshProductTable() {
        productTableModel.setRowCount(0);
        for (Product product : ecommerce.getAllProducts()) {
            Object[] row = {
                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("$%.2f", product.getPrice()),
                product.getStock(),
                product.getDescription()
            };
            productTableModel.addRow(row);
        }
    }
    
    private void refreshShopTable() {
        updateShopTable(ecommerce.getAllProducts());
    }
    
    private void updateShopTable(List<Product> products) {
        shopTableModel.setRowCount(0);
        for (Product product : products) {
            Object[] row = {
                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("$%.2f", product.getPrice()),
                product.getStock()
            };
            shopTableModel.addRow(row);
        }
    }
    
    private void refreshCart() {
        cartListModel.clear();
        for (CartItem item : ecommerce.getCart()) {
            cartListModel.addElement(item);
        }
        cartTotalLabel.setText(String.format("Total: $%.2f", ecommerce.getCartTotal()));
    }
    
    private void refreshOrderTable() {
        orderTableModel.setRowCount(0);
        for (Order order : ecommerce.getAllOrders()) {
            Object[] row = {
                order.getOrderId(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getFormattedDate(),
                String.format("$%.2f", order.getTotalAmount()),
                order.getStatus()
            };
            orderTableModel.addRow(row);
        }
    }
    
    private void updateCategoryFilter() {
        categoryFilter.removeAllItems();
        categoryFilter.addItem("All Categories");
        for (String category : ecommerce.getCategories()) {
            categoryFilter.addItem(category);
        }
    }
    
    private void loadSampleData() {
        ecommerce.addProduct(new Product("P001", "iPhone 15", "Electronics", 999.99, 50, "Latest Apple smartphone"));
        ecommerce.addProduct(new Product("P002", "MacBook Pro", "Electronics", 1999.99, 25, "Professional laptop"));
        ecommerce.addProduct(new Product("P003", "Nike Air Max", "Shoes", 129.99, 100, "Comfortable running shoes"));
        ecommerce.addProduct(new Product("P004", "Levi's Jeans", "Clothing", 79.99, 75, "Classic denim jeans"));
        ecommerce.addProduct(new Product("P005", "Coffee Maker", "Appliances", 89.99, 30, "Automatic drip coffee maker"));
        ecommerce.addProduct(new Product("P006", "Wireless Mouse", "Electronics", 29.99, 200, "Bluetooth wireless mouse"));
        ecommerce.addProduct(new Product("P007", "Gaming Chair", "Furniture", 299.99, 15, "Ergonomic gaming chair"));
        ecommerce.addProduct(new Product("P008", "Yoga Mat", "Sports", 24.99, 80, "Non-slip exercise mat"));
    }
}