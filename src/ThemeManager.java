import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private static boolean isDarkMode = false;
    
    // Dark theme colors
    public static final Color DARK_BG = new Color(45, 45, 45);
    public static final Color DARK_PANEL = new Color(60, 60, 60);
    public static final Color DARK_TEXT = Color.WHITE;
    public static final Color DARK_BORDER = new Color(80, 80, 80);
    public static final Color DARK_BUTTON = new Color(70, 70, 70);
    public static final Color DARK_SELECTED = new Color(100, 100, 100);
    
    // Light theme colors
    public static final Color LIGHT_BG = Color.WHITE;
    public static final Color LIGHT_PANEL = new Color(240, 240, 240);
    public static final Color LIGHT_TEXT = Color.BLACK;
    public static final Color LIGHT_BORDER = Color.GRAY;
    public static final Color LIGHT_BUTTON = new Color(230, 230, 230);
    public static final Color LIGHT_SELECTED = new Color(184, 207, 229);
    
    public static void setDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
    }
    
    public static boolean isDarkMode() {
        return isDarkMode;
    }
    
    public static void applyTheme(Component component) {
        if (component instanceof JFrame) {
            JFrame frame = (JFrame) component;
            frame.getContentPane().setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
        }
        
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            panel.setBackground(isDarkMode ? DARK_PANEL : LIGHT_PANEL);
            panel.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        }
        
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        }
        
        if (component instanceof JButton) {
            JButton button = (JButton) component;
            button.setBackground(isDarkMode ? DARK_BUTTON : LIGHT_BUTTON);
            button.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            button.setBorder(BorderFactory.createLineBorder(isDarkMode ? DARK_BORDER : LIGHT_BORDER));
        }
        
        if (component instanceof JTextField) {
            JTextField field = (JTextField) component;
            field.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
            field.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            field.setCaretColor(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        }
        
        if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
            table.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
            table.setSelectionBackground(isDarkMode ? DARK_SELECTED : LIGHT_SELECTED);
            table.setGridColor(isDarkMode ? DARK_BORDER : LIGHT_BORDER);
            table.getTableHeader().setBackground(isDarkMode ? DARK_PANEL : LIGHT_PANEL);
            table.getTableHeader().setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        }
        
        if (component instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) component;
            combo.setBackground(isDarkMode ? DARK_BUTTON : LIGHT_BUTTON);
            combo.setForeground(isDarkMode ? DARK_TEXT : LIGHT_TEXT);
        }
        
        if (component instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) component;
            scroll.getViewport().setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
            scroll.setBackground(isDarkMode ? DARK_BG : LIGHT_BG);
        }
        
        // Apply to all child components
        if (component instanceof Container) {
            Container container = (Container) component;
            for (Component child : container.getComponents()) {
                applyTheme(child);
            }
        }
    }
}