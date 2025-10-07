import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Attendance {
    private String attendanceId;
    private String studentId;
    private LocalDate date;
    private String status; // Present, Absent, Late, Excused
    private String remarks;
    
    public Attendance(String attendanceId, String studentId, LocalDate date, String status, String remarks) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
        this.remarks = remarks;
    }
    
    // Getters
    public String getAttendanceId() { return attendanceId; }
    public String getStudentId() { return studentId; }
    public LocalDate getDate() { return date; }
    public String getStatus() { return status; }
    public String getRemarks() { return remarks; }
    
    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    
    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", getFormattedDate(), status, remarks);
    }
}