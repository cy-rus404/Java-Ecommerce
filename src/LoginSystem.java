import java.util.*;

public class LoginSystem {
    private Map<String, User> users;
    private User currentUser;
    
    public LoginSystem() {
        users = new HashMap<>();
        currentUser = null;
        createDefaultUsers();
    }
    
    private void createDefaultUsers() {
        // Default admin user only
        addUser("admin", "admin123", "Admin", "System Administrator", "admin@school.edu", "");
    }
    
    public String addUser(String username, String password, String role, String fullName, 
                         String email, String associatedId) {
        String userId = "USR" + (users.size() + 1000);
        User user = new User(userId, username, password, role, fullName, email, associatedId);
        users.put(username, user);
        return userId;
    }
    
    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean hasPermission(String permission) {
        if (currentUser == null) return false;
        
        String role = currentUser.getRole();
        
        switch (permission) {
            case "MANAGE_STUDENTS":
                return role.equals("Admin");
            case "MANAGE_TEACHERS":
                return role.equals("Admin");
            case "MANAGE_GRADES":
                return role.equals("Admin") || role.equals("Teacher");
            case "VIEW_GRADES":
                return true; // All roles can view grades (filtered by access)
            case "MANAGE_ATTENDANCE":
                return role.equals("Admin") || role.equals("Teacher");
            case "VIEW_ATTENDANCE":
                return true; // All roles can view attendance (filtered by access)
            case "MANAGE_FEES":
                return role.equals("Admin");
            case "VIEW_FEES":
                return role.equals("Admin") || role.equals("Parent");
            case "VIEW_OWN_DATA":
                return role.equals("Student") || role.equals("Parent");
            default:
                return false;
        }
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
    
    public boolean removeUser(String username) {
        return users.remove(username) != null;
    }
    
    public User getUser(String username) {
        return users.get(username);
    }
    
    public void addExistingUser(User user) {
        users.put(user.getUsername(), user);
    }
}