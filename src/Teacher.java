import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Teacher {
    private String teacherId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String subject;
    private String qualification;
    private double salary;
    private LocalDate hireDate;
    private String status; // Active, Inactive, On Leave
    private String address;
    
    public Teacher(String teacherId, String firstName, String lastName, String email,
                  String phone, String subject, String qualification, double salary, String address) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.qualification = qualification;
        this.salary = salary;
        this.address = address;
        this.hireDate = LocalDate.now();
        this.status = "Active";
    }
    
    // Getters
    public String getTeacherId() { return teacherId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getSubject() { return subject; }
    public String getQualification() { return qualification; }
    public double getSalary() { return salary; }
    public LocalDate getHireDate() { return hireDate; }
    public String getStatus() { return status; }
    public String getAddress() { return address; }
    
    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setStatus(String status) { this.status = status; }
    public void setAddress(String address) { this.address = address; }
    
    public String getFormattedHireDate() {
        return hireDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", teacherId, getFullName(), subject);
    }
}