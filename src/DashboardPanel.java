import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private SchoolManagementSystem school;
    private LoginSystem loginSystem;
    private JTable gradeTable, attendanceTable, feeTable;
    private DefaultTableModel gradeTableModel, attendanceTableModel, feeTableModel;
    private JLabel welcomeLabel, gpaLabel, attendanceLabel, feeStatusLabel;
    
    public DashboardPanel(SchoolManagementSystem school, LoginSystem loginSystem) {
        this.school = school;
        this.loginSystem = loginSystem;
        initializeDashboard();
        loadUserData();
    }
    
    private void initializeDashboard() {
        setLayout(new BorderLayout());
        
        User currentUser = loginSystem.getCurrentUser();
        String role = currentUser.getRole();
        
        // Welcome panel
        JPanel welcomePanel = new JPanel(new GridLayout(4, 1, 10, 10));
        welcomePanel.setBorder(BorderFactory.createTitledBorder("Welcome"));
        
        welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        
        gpaLabel = new JLabel("Current GPA: Loading...");
        attendanceLabel = new JLabel("Attendance: Loading...");
        feeStatusLabel = new JLabel("Fee Status: Loading...");
        
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(gpaLabel);
        welcomePanel.add(attendanceLabel);
        welcomePanel.add(feeStatusLabel);
        
        // Create tabbed pane for different sections
        JTabbedPane dashboardTabs = new JTabbedPane();
        
        if (role.equals("Student") || role.equals("Parent")) {
            dashboardTabs.addTab("My Grades", createGradesPanel());
            dashboardTabs.addTab("My Attendance", createAttendancePanel());
            
            if (role.equals("Parent")) {
                dashboardTabs.addTab("Fee Information", createFeePanel());
            }
        }
        
        add(welcomePanel, BorderLayout.NORTH);
        add(dashboardTabs, BorderLayout.CENTER);
    }
    
    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Subject", "Exam Type", "Marks", "Total", "Grade", "Percentage", "Semester", "Year"};
        gradeTableModel = new DefaultTableModel(columns, 0);
        gradeTable = new JTable(gradeTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        panel.add(new JScrollPane(gradeTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Date", "Status", "Remarks"};
        attendanceTableModel = new DefaultTableModel(columns, 0);
        attendanceTable = new JTable(attendanceTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        panel.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFeePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Description", "Total Fees", "Paid Amount", "Outstanding", "Status"};
        feeTableModel = new DefaultTableModel(columns, 0);
        feeTable = new JTable(feeTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        panel.add(new JScrollPane(feeTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadUserData() {
        User currentUser = loginSystem.getCurrentUser();
        String studentId = getStudentId(currentUser);
        
        if (studentId != null) {
            loadGrades(studentId);
            loadAttendance(studentId);
            loadFeeInfo(studentId);
            calculateStats(studentId);
        }
    }
    
    private String getStudentId(User user) {
        if (user.getRole().equals("Student")) {
            return user.getAssociatedId();
        } else if (user.getRole().equals("Parent")) {
            return user.getAssociatedId();
        }
        return null;
    }
    
    private void loadGrades(String studentId) {
        if (gradeTableModel != null) {
            gradeTableModel.setRowCount(0);
            List<Grade> grades = school.getStudentGrades(studentId);
            
            for (Grade grade : grades) {
                Object[] row = {
                    grade.getSubject(),
                    grade.getExamType(),
                    grade.getMarks(),
                    grade.getTotalMarks(),
                    grade.getGrade(),
                    String.format("%.1f%%", grade.getPercentage()),
                    grade.getSemester(),
                    grade.getAcademicYear()
                };
                gradeTableModel.addRow(row);
            }
        }
    }
    
    private void loadAttendance(String studentId) {
        if (attendanceTableModel != null) {
            attendanceTableModel.setRowCount(0);
            List<Attendance> attendanceList = school.getStudentAttendance(studentId);
            
            for (Attendance attendance : attendanceList) {
                Object[] row = {
                    attendance.getFormattedDate(),
                    attendance.getStatus(),
                    attendance.getRemarks()
                };
                attendanceTableModel.addRow(row);
            }
        }
    }
    
    private void loadFeeInfo(String studentId) {
        if (feeTableModel != null) {
            feeTableModel.setRowCount(0);
            Student student = school.getStudent(studentId);
            
            if (student != null) {
                String status = student.getOutstandingFees() > 0 ? "Outstanding" : "Paid";
                Object[] row = {
                    "Academic Year 2024",
                    String.format("$%.2f", student.getFeesTotal()),
                    String.format("$%.2f", student.getFeesPaid()),
                    String.format("$%.2f", student.getOutstandingFees()),
                    status
                };
                feeTableModel.addRow(row);
            }
        }
    }
    
    private void calculateStats(String studentId) {
        // Calculate GPA
        double gpa = school.calculateStudentGPA(studentId, "Fall", "2024");
        if (gpa > 0) {
            gpaLabel.setText(String.format("Current GPA: %.2f%%", gpa));
        } else {
            gpaLabel.setText("Current GPA: No grades available");
        }
        
        // Calculate attendance percentage
        java.time.LocalDate endDate = java.time.LocalDate.now();
        java.time.LocalDate startDate = endDate.minusDays(30);
        double attendancePercentage = school.calculateAttendancePercentage(studentId, startDate, endDate);
        
        if (attendancePercentage > 0) {
            attendanceLabel.setText(String.format("Attendance (Last 30 days): %.1f%%", attendancePercentage));
        } else {
            attendanceLabel.setText("Attendance: No records available");
        }
        
        // Fee status
        Student student = school.getStudent(studentId);
        if (student != null) {
            if (student.getOutstandingFees() > 0) {
                feeStatusLabel.setText(String.format("Outstanding Fees: $%.2f", student.getOutstandingFees()));
                feeStatusLabel.setForeground(Color.RED);
            } else {
                feeStatusLabel.setText("Fee Status: All fees paid");
                feeStatusLabel.setForeground(new Color(0, 128, 0));
            }
        }
    }
    
    public void refreshData() {
        loadUserData();
    }
}