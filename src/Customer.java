import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private String address;
    private String status; // Lead, Prospect, Customer, Inactive
    private LocalDate dateAdded;
    private double totalValue;
    private String notes;
    
    public Customer(String customerId, String firstName, String lastName, String email, 
                   String phone, String company, String address, String status) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.address = address;
        this.status = status;
        this.dateAdded = LocalDate.now();
        this.totalValue = 0.0;
        this.notes = "";
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCompany() { return company; }
    public String getAddress() { return address; }
    public String getStatus() { return status; }
    public LocalDate getDateAdded() { return dateAdded; }
    public double getTotalValue() { return totalValue; }
    public String getNotes() { return notes; }
    
    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setCompany(String company) { this.company = company; }
    public void setAddress(String address) { this.address = address; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalValue(double totalValue) { this.totalValue = totalValue; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public void addValue(double amount) {
        this.totalValue += amount;
    }
    
    public String getFormattedDate() {
        return dateAdded.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", customerId, getFullName(), company);
    }
}