public class User {
    private String userId;
    private String username;
    private String password;
    private String role; // Admin, Teacher, Student, Parent
    private String fullName;
    private String email;
    private boolean isActive;
    private String associatedId; // Teacher ID, Student ID, or Parent's Student ID
    
    public User(String userId, String username, String password, String role, 
               String fullName, String email, String associatedId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.email = email;
        this.associatedId = associatedId;
        this.isActive = true;
    }
    
    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public boolean isActive() { return isActive; }
    public String getAssociatedId() { return associatedId; }
    
    // Setters
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setAssociatedId(String associatedId) { this.associatedId = associatedId; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", userId, fullName, role);
    }
}