import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JDialog {
    private LoginSystem loginSystem;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private boolean loginSuccessful = false;
    
    public LoginGUI(JFrame parent, LoginSystem loginSystem) {
        super(parent, "School Management System - Login", true);
        this.loginSystem = loginSystem;
        initializeGUI();
    }
    
    private void initializeGUI() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        JLabel titleLabel = new JLabel("School Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        // Login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        
        loginButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setPreferredSize(new Dimension(100, 30));
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });
        
        // Enter key support
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        
        // Demo credentials panel
        JPanel demoPanel = new JPanel();
        demoPanel.setBorder(BorderFactory.createTitledBorder("Login Credentials"));
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));
        
        demoPanel.add(new JLabel("Admin: admin / admin123"));
        demoPanel.add(new JLabel("Add students/teachers to create more users"));
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(demoPanel, BorderLayout.SOUTH);
        
        setSize(450, 400);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Focus on username field
        SwingUtilities.invokeLater(() -> usernameField.requestFocus());
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (loginSystem.authenticate(username, password)) {
            loginSuccessful = true;
            JOptionPane.showMessageDialog(this, "Login successful! Welcome " + 
                    loginSystem.getCurrentUser().getFullName(), 
                    "Login Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }
    
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}