import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InventoryGUI extends JFrame {
    private InventoryManager inventory;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, categoryField, quantityField, priceField, warehouseField;
    private JLabel totalValueLabel;
    
    public InventoryGUI() {
        inventory = new InventoryManager();
        loadSampleData();
        initializeGUI();
        refreshTable();
    }
    
    private void initializeGUI() {
        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel topPanel = createInputPanel();
        JPanel centerPanel = createTablePanel();
        JPanel bottomPanel = createButtonPanel();
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Product Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(10);
        panel.add(idField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3;
        nameField = new JTextField(15);
        panel.add(nameField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 0;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 5;
        categoryField = new JTextField(10);
        panel.add(categoryField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(10);
        panel.add(quantityField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 3;
        priceField = new JTextField(15);
        panel.add(priceField, gbc);
        
        gbc.gridx = 4; gbc.gridy = 1;
        panel.add(new JLabel("Warehouse:"), gbc);
        gbc.gridx = 5;
        warehouseField = new JTextField(10);
        panel.add(warehouseField, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Inventory"));
        
        String[] columns = {"ID", "Name", "Category", "Quantity", "Price", "Warehouse", "Total Value"};
        tableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    loadProductToFields(row);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        
        JButton addBtn = new JButton("Add Product");
        JButton updateBtn = new JButton("Update Stock");
        JButton removeBtn = new JButton("Remove Product");
        JButton lowStockBtn = new JButton("Low Stock Alert");
        JButton clearBtn = new JButton("Clear Fields");
        
        totalValueLabel = new JLabel("Total Value: $0.00");
        totalValueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        addBtn.addActionListener(e -> addProduct());
        updateBtn.addActionListener(e -> updateStock());
        removeBtn.addActionListener(e -> removeProduct());
        lowStockBtn.addActionListener(e -> showLowStock());
        clearBtn.addActionListener(e -> clearFields());
        
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(removeBtn);
        panel.add(lowStockBtn);
        panel.add(clearBtn);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(totalValueLabel);
        
        return panel;
    }
    
    private void addProduct() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String warehouse = warehouseField.getText().trim();
            
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID and Name are required!");
                return;
            }
            
            Product product = new Product(id, name, category, quantity, price, warehouse);
            if (inventory.addProduct(product)) {
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "Product added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Product ID already exists!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format!");
        }
    }
    
    private void updateStock() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product ID!");
            return;
        }
        
        String[] options = {"Add Stock", "Remove Stock", "Set Stock"};
        int choice = JOptionPane.showOptionDialog(this, "Choose action:", "Update Stock",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (choice >= 0) {
            String amountStr = JOptionPane.showInputDialog(this, "Enter amount:");
            try {
                int amount = Integer.parseInt(amountStr);
                boolean success = false;
                
                switch (choice) {
                    case 0: success = inventory.addStock(id, amount); break;
                    case 1: success = inventory.removeStock(id, amount); break;
                    case 2: success = inventory.updateStock(id, amount); break;
                }
                
                if (success) {
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Stock updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update stock!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            }
        }
    }
    
    private void removeProduct() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product ID!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Remove product " + id + "?", 
                "Confirm Removal", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (inventory.removeProduct(id)) {
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this, "Product removed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!");
            }
        }
    }
    
    private void showLowStock() {
        String thresholdStr = JOptionPane.showInputDialog(this, "Enter low stock threshold:", "10");
        try {
            int threshold = Integer.parseInt(thresholdStr);
            List<Product> lowStock = inventory.getLowStockProducts(threshold);
            
            if (lowStock.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No low stock items found!");
            } else {
                StringBuilder message = new StringBuilder("Low Stock Items:\n\n");
                for (Product product : lowStock) {
                    message.append(String.format("%s - %s: %d units\n", 
                            product.getId(), product.getName(), product.getQuantity()));
                }
                JOptionPane.showMessageDialog(this, message.toString());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid threshold!");
        }
    }
    
    private void loadProductToFields(int row) {
        idField.setText((String) tableModel.getValueAt(row, 0));
        nameField.setText((String) tableModel.getValueAt(row, 1));
        categoryField.setText((String) tableModel.getValueAt(row, 2));
        quantityField.setText(tableModel.getValueAt(row, 3).toString());
        priceField.setText(tableModel.getValueAt(row, 4).toString().replace("$", ""));
        warehouseField.setText((String) tableModel.getValueAt(row, 5));
    }
    
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        priceField.setText("");
        warehouseField.setText("");
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Product> products = inventory.getAllProducts();
        
        for (Product product : products) {
            Object[] row = {
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getQuantity(),
                String.format("$%.2f", product.getPrice()),
                product.getWarehouse(),
                String.format("$%.2f", product.getTotalValue())
            };
            tableModel.addRow(row);
        }
        
        totalValueLabel.setText(String.format("Total Value: $%.2f", inventory.getTotalInventoryValue()));
    }
    
    private void loadSampleData() {
        inventory.addProduct(new Product("P001", "Laptop", "Electronics", 50, 999.99, "Main"));
        inventory.addProduct(new Product("P002", "Mouse", "Electronics", 200, 25.50, "Main"));
        inventory.addProduct(new Product("P003", "Desk Chair", "Furniture", 30, 150.00, "Warehouse-A"));
        inventory.addProduct(new Product("P004", "Notebook", "Stationery", 5, 3.99, "Main"));
        inventory.addProduct(new Product("P005", "Monitor", "Electronics", 25, 299.99, "Warehouse-B"));
    }
}