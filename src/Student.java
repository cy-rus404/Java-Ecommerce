import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Student {
    private String studentId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String grade;
    private String parentName;
    private String parentPhone;
    private String parentEmail;
    private String address;
    private LocalDate enrollmentDate;
    private String status; // Active, Inactive, Graduated
    private double feesPaid;
    private double feesTotal;
    
    public Student(String studentId, String firstName, String lastName, LocalDate dateOfBirth,
                  String gender, String grade, String parentName, String parentPhone, 
                  String parentEmail, String address) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.grade = grade;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.parentEmail = parentEmail;
        this.address = address;
        this.enrollmentDate = LocalDate.now();
        this.status = "Active";
        this.feesPaid = 0.0;
        this.feesTotal = 0.0;
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getGrade() { return grade; }
    public String getParentName() { return parentName; }
    public String getParentPhone() { return parentPhone; }
    public String getParentEmail() { return parentEmail; }
    public String getAddress() { return address; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public String getStatus() { return status; }
    public double getFeesPaid() { return feesPaid; }
    public double getFeesTotal() { return feesTotal; }
    
    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setParentName(String parentName) { this.parentName = parentName; }
    public void setParentPhone(String parentPhone) { this.parentPhone = parentPhone; }
    public void setParentEmail(String parentEmail) { this.parentEmail = parentEmail; }
    public void setAddress(String address) { this.address = address; }
    public void setStatus(String status) { this.status = status; }
    public void setFeesTotal(double feesTotal) { this.feesTotal = feesTotal; }
    
    public void addFeePayment(double amount) {
        this.feesPaid += amount;
    }
    
    public double getOutstandingFees() {
        return feesTotal - feesPaid;
    }
    
    public String getFormattedDOB() {
        return dateOfBirth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    public String getFormattedEnrollmentDate() {
        return enrollmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (Grade %s)", studentId, getFullName(), grade);
    }
}