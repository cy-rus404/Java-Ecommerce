import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SchoolManagementGUI extends JFrame {
    private SchoolManagementSystem school;
    private LoginSystem loginSystem;
    private JTabbedPane tabbedPane;
    private JLabel userInfoLabel;
    private String johnDoeId;
    private String janeSmithId;
    
    // Student Management
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JTextField stuFirstNameField, stuLastNameField, stuGradeField, stuParentNameField;
    private JTextField stuParentPhoneField, stuParentEmailField, stuAddressField, stuDOBField;
    private JTextField stuUsernameField, stuPasswordField, stuParentUsernameField, stuParentPasswordField;
    private JComboBox<String> stuGenderCombo;
    
    // Teacher Management
    private JTable teacherTable;
    private DefaultTableModel teacherTableModel;
    private JTextField teaFirstNameField, teaLastNameField, teaEmailField, teaPhoneField;
    private JTextField teaSubjectField, teaQualificationField, teaSalaryField, teaAddressField;
    private JTextField teaUsernameField, teaPasswordField;
    
    // Grades Management
    private JTable gradeTable;
    private DefaultTableModel gradeTableModel;
    private JComboBox<String> gradeStudentCombo, gradeSubjectCombo, gradeExamTypeCombo;
    private JTextField gradeMarksField, gradeTotalMarksField, gradeSemesterField, gradeYearField;
    
    // Attendance Management
    private JTable attendanceTable;
    private DefaultTableModel attendanceTableModel;
    private JComboBox<String> attStudentCombo, attStatusCombo;
    private JTextField attDateField, attRemarksField;
    
    // Fee Management
    private JTable feeTable;
    private DefaultTableModel feeTableModel;
    private JComboBox<String> feeStudentCombo;
    private JTextField feeAmountField, feeTotalField;
    
    public SchoolManagementGUI() {
        school = new SchoolManagementSystem();
        loginSystem = new LoginSystem();
        
        // Try to load existing data, if none exists start empty
        if (!DataPersistence.loadData(school, loginSystem)) {
            loadSampleData();
        }
        
        // Show login dialog
        LoginGUI loginDialog = new LoginGUI(this, loginSystem);
        loginDialog.setVisible(true);
        
        if (!loginDialog.isLoginSuccessful()) {
            System.exit(0);
        }
        
        initializeGUI();
        
        // Only refresh tables if user has permission to see them
        SwingUtilities.invokeLater(() -> {
            refreshAllTables();
        });
    }
    
    private void initializeGUI() {
        User currentUser = loginSystem.getCurrentUser();
        setTitle("School Management System - " + currentUser.getRole() + ": " + currentUser.getFullName());
        
        // Save data when window is closed
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                DataPersistence.saveData(school, loginSystem);
                System.exit(0);
            }
        });
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        JMenu userMenu = new JMenu("User");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem changePasswordItem = new JMenuItem("Change Password");
        
        logoutItem.addActionListener(e -> logout());
        changePasswordItem.addActionListener(e -> changePassword());
        
        userMenu.add(changePasswordItem);
        userMenu.addSeparator();
        userMenu.add(logoutItem);
        
        JMenu themeMenu = new JMenu("Theme");
        JMenuItem darkModeItem = new JMenuItem("Toggle Dark Mode");
        darkModeItem.addActionListener(e -> toggleDarkMode());
        themeMenu.add(darkModeItem);
        
        menuBar.add(userMenu);
        menuBar.add(themeMenu);
        setJMenuBar(menuBar);
        
        // User info panel
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoLabel = new JLabel("Logged in as: " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        userInfoLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        
        JButton logoutButton = new JButton("Logout");
        JButton themeButton = new JButton("🌙 Dark Mode");
        
        logoutButton.addActionListener(e -> logout());
        themeButton.addActionListener(e -> toggleDarkMode());
        
        userInfoPanel.add(userInfoLabel);
        userInfoPanel.add(Box.createHorizontalStrut(10));
        userInfoPanel.add(themeButton);
        userInfoPanel.add(Box.createHorizontalStrut(10));
        userInfoPanel.add(logoutButton);
        
        tabbedPane = new JTabbedPane();
        
        // Add tabs based on user permissions
        if (loginSystem.hasPermission("MANAGE_STUDENTS")) {
            tabbedPane.addTab("Students", createStudentTab());
        }
        if (loginSystem.hasPermission("MANAGE_TEACHERS")) {
            tabbedPane.addTab("Teachers", createTeacherTab());
        }
        if (loginSystem.hasPermission("VIEW_GRADES")) {
            tabbedPane.addTab("Grades", createGradeTab());
        }
        if (loginSystem.hasPermission("VIEW_ATTENDANCE")) {
            tabbedPane.addTab("Attendance", createAttendanceTab());
        }
        if (loginSystem.hasPermission("VIEW_FEES")) {
            tabbedPane.addTab("Fees", createFeeTab());
        }
        
        // If student or parent, add personal dashboard
        if (loginSystem.getCurrentUser().getRole().equals("Student") || 
            loginSystem.getCurrentUser().getRole().equals("Parent")) {
            tabbedPane.addTab("My Dashboard", createDashboardTab());
        }
        
        add(tabbedPane, BorderLayout.CENTER);
        add(userInfoPanel, BorderLayout.SOUTH);
        setSize(1400, 800);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        // Apply initial theme
        ThemeManager.applyTheme(this);
    }
    
    private JPanel createStudentTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Input form
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        stuFirstNameField = new JTextField(15);
        inputPanel.add(stuFirstNameField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        stuLastNameField = new JTextField(15);
        inputPanel.add(stuLastNameField, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        stuDOBField = new JTextField(15);
        inputPanel.add(stuDOBField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 3;
        stuGenderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        inputPanel.add(stuGenderCombo, gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Grade:"), gbc);
        gbc.gridx = 1;
        stuGradeField = new JTextField(15);
        inputPanel.add(stuGradeField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Parent Name:"), gbc);
        gbc.gridx = 3;
        stuParentNameField = new JTextField(15);
        inputPanel.add(stuParentNameField, gbc);
        
        // Row 4
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Parent Phone:"), gbc);
        gbc.gridx = 1;
        stuParentPhoneField = new JTextField(15);
        inputPanel.add(stuParentPhoneField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Parent Email:"), gbc);
        gbc.gridx = 3;
        stuParentEmailField = new JTextField(15);
        inputPanel.add(stuParentEmailField, gbc);
        
        // Row 5
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        stuAddressField = new JTextField(40);
        inputPanel.add(stuAddressField, gbc);
        
        // Login account creation fields
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        inputPanel.add(new JLabel("Student Username:"), gbc);
        gbc.gridx = 1;
        stuUsernameField = new JTextField(15);
        inputPanel.add(stuUsernameField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Student Password:"), gbc);
        gbc.gridx = 3;
        stuPasswordField = new JTextField(15);
        inputPanel.add(stuPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        inputPanel.add(new JLabel("Parent Username:"), gbc);
        gbc.gridx = 1;
        stuParentUsernameField = new JTextField(15);
        inputPanel.add(stuParentUsernameField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Parent Password:"), gbc);
        gbc.gridx = 3;
        stuParentPasswordField = new JTextField(15);
        inputPanel.add(stuParentPasswordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addStudentBtn = new JButton("Add Student");
        JButton updateStudentBtn = new JButton("Update Student");
        JButton deleteStudentBtn = new JButton("Delete Student");
        JButton clearStudentBtn = new JButton("Clear Fields");
        JButton searchStudentBtn = new JButton("Search");
        
        addStudentBtn.addActionListener(e -> addStudent());
        updateStudentBtn.addActionListener(e -> updateStudent());
        deleteStudentBtn.addActionListener(e -> deleteStudent());
        clearStudentBtn.addActionListener(e -> clearStudentFields());
        searchStudentBtn.addActionListener(e -> searchStudents());
        
        buttonPanel.add(addStudentBtn);
        buttonPanel.add(updateStudentBtn);
        buttonPanel.add(deleteStudentBtn);
        buttonPanel.add(clearStudentBtn);
        buttonPanel.add(searchStudentBtn);
        
        // Table
        String[] columns = {"ID", "Name", "Grade", "Gender", "Parent", "Phone", "Email", "Status"};
        studentTableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(studentTableModel);
        
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    loadStudentToFields(row);
                }
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTeacherTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Input form
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Teacher Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        teaFirstNameField = new JTextField(15);
        inputPanel.add(teaFirstNameField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        teaLastNameField = new JTextField(15);
        inputPanel.add(teaLastNameField, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        teaEmailField = new JTextField(15);
        inputPanel.add(teaEmailField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3;
        teaPhoneField = new JTextField(15);
        inputPanel.add(teaPhoneField, gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        teaSubjectField = new JTextField(15);
        inputPanel.add(teaSubjectField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Qualification:"), gbc);
        gbc.gridx = 3;
        teaQualificationField = new JTextField(15);
        inputPanel.add(teaQualificationField, gbc);
        
        // Row 4
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1;
        teaSalaryField = new JTextField(15);
        inputPanel.add(teaSalaryField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 3;
        teaAddressField = new JTextField(15);
        inputPanel.add(teaAddressField, gbc);
        
        // Login account creation fields
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        teaUsernameField = new JTextField(15);
        inputPanel.add(teaUsernameField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 3;
        teaPasswordField = new JTextField(15);
        inputPanel.add(teaPasswordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addTeacherBtn = new JButton("Add Teacher");
        JButton updateTeacherBtn = new JButton("Update Teacher");
        JButton deleteTeacherBtn = new JButton("Delete Teacher");
        JButton clearTeacherBtn = new JButton("Clear Fields");
        
        addTeacherBtn.addActionListener(e -> addTeacher());
        updateTeacherBtn.addActionListener(e -> updateTeacher());
        deleteTeacherBtn.addActionListener(e -> deleteTeacher());
        clearTeacherBtn.addActionListener(e -> clearTeacherFields());
        
        buttonPanel.add(addTeacherBtn);
        buttonPanel.add(updateTeacherBtn);
        buttonPanel.add(deleteTeacherBtn);
        buttonPanel.add(clearTeacherBtn);
        
        // Table
        String[] columns = {"ID", "Name", "Subject", "Email", "Phone", "Qualification", "Salary", "Status"};
        teacherTableModel = new DefaultTableModel(columns, 0);
        teacherTable = new JTable(teacherTableModel);
        
        teacherTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = teacherTable.getSelectedRow();
                if (row >= 0) {
                    loadTeacherToFields(row);
                }
            }
        });
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(teacherTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createGradeTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Input form
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Grade Entry"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        gradeStudentCombo = new JComboBox<>();
        inputPanel.add(gradeStudentCombo, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 3;
        gradeSubjectCombo = new JComboBox<>(new String[]{"Mathematics", "English", "Science", "History", "Geography", "Physics", "Chemistry", "Biology"});
        gradeSubjectCombo.setEditable(true);
        inputPanel.add(gradeSubjectCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Exam Type:"), gbc);
        gbc.gridx = 1;
        gradeExamTypeCombo = new JComboBox<>(new String[]{"Quiz", "Assignment", "Midterm", "Final", "Project"});
        inputPanel.add(gradeExamTypeCombo, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Marks Obtained:"), gbc);
        gbc.gridx = 3;
        gradeMarksField = new JTextField(10);
        inputPanel.add(gradeMarksField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Total Marks:"), gbc);
        gbc.gridx = 1;
        gradeTotalMarksField = new JTextField(10);
        inputPanel.add(gradeTotalMarksField, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 3;
        gradeSemesterField = new JTextField(10);
        inputPanel.add(gradeSemesterField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Academic Year:"), gbc);
        gbc.gridx = 1;
        gradeYearField = new JTextField(10);
        inputPanel.add(gradeYearField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addGradeBtn = new JButton("Add Grade");
        JButton calculateGPABtn = new JButton("Calculate GPA");
        JButton clearGradeBtn = new JButton("Clear Fields");
        
        addGradeBtn.addActionListener(e -> addGrade());
        calculateGPABtn.addActionListener(e -> calculateGPA());
        clearGradeBtn.addActionListener(e -> clearGradeFields());
        
        buttonPanel.add(addGradeBtn);
        buttonPanel.add(calculateGPABtn);
        buttonPanel.add(clearGradeBtn);
        
        // Table
        String[] columns = {"Student ID", "Student Name", "Subject", "Exam Type", "Marks", "Total", "Grade", "Percentage", "Semester", "Year"};
        gradeTableModel = new DefaultTableModel(columns, 0);
        gradeTable = new JTable(gradeTableModel);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(gradeTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAttendanceTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Input form
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Mark Attendance"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        attStudentCombo = new JComboBox<>();
        inputPanel.add(attStudentCombo, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        attDateField = new JTextField(15);
        attDateField.setText(LocalDate.now().toString());
        inputPanel.add(attDateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        attStatusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Late", "Excused"});
        inputPanel.add(attStatusCombo, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Remarks:"), gbc);
        gbc.gridx = 3;
        attRemarksField = new JTextField(15);
        inputPanel.add(attRemarksField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton markAttendanceBtn = new JButton("Mark Attendance");
        JButton viewAttendanceBtn = new JButton("View Student Attendance");
        JButton calculateAttendanceBtn = new JButton("Calculate Attendance %");
        JButton clearAttendanceBtn = new JButton("Clear Fields");
        
        markAttendanceBtn.addActionListener(e -> markAttendance());
        viewAttendanceBtn.addActionListener(e -> viewStudentAttendance());
        calculateAttendanceBtn.addActionListener(e -> calculateAttendancePercentage());
        clearAttendanceBtn.addActionListener(e -> clearAttendanceFields());
        
        buttonPanel.add(markAttendanceBtn);
        buttonPanel.add(viewAttendanceBtn);
        buttonPanel.add(calculateAttendanceBtn);
        buttonPanel.add(clearAttendanceBtn);
        
        // Table
        String[] columns = {"Student ID", "Student Name", "Date", "Status", "Remarks"};
        attendanceTableModel = new DefaultTableModel(columns, 0);
        attendanceTable = new JTable(attendanceTableModel);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(attendanceTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFeeTab() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Input form
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Fee Management"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        feeStudentCombo = new JComboBox<>();
        inputPanel.add(feeStudentCombo, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Payment Amount:"), gbc);
        gbc.gridx = 3;
        feeAmountField = new JTextField(15);
        inputPanel.add(feeAmountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Set Total Fees:"), gbc);
        gbc.gridx = 1;
        feeTotalField = new JTextField(15);
        inputPanel.add(feeTotalField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addPaymentBtn = new JButton("Add Payment");
        JButton setTotalFeesBtn = new JButton("Set Total Fees");
        JButton viewOutstandingBtn = new JButton("View Outstanding Fees");
        JButton clearFeeBtn = new JButton("Clear Fields");
        
        addPaymentBtn.addActionListener(e -> addFeePayment());
        setTotalFeesBtn.addActionListener(e -> setTotalFees());
        viewOutstandingBtn.addActionListener(e -> viewOutstandingFees());
        clearFeeBtn.addActionListener(e -> clearFeeFields());
        
        buttonPanel.add(addPaymentBtn);
        buttonPanel.add(setTotalFeesBtn);
        buttonPanel.add(viewOutstandingBtn);
        buttonPanel.add(clearFeeBtn);
        
        // Table
        String[] columns = {"Student ID", "Student Name", "Grade", "Total Fees", "Fees Paid", "Outstanding", "Status"};
        feeTableModel = new DefaultTableModel(columns, 0);
        feeTable = new JTable(feeTableModel);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(feeTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    // Permission check helper
    private boolean checkPermission(String permission) {
        if (!loginSystem.hasPermission(permission)) {
            JOptionPane.showMessageDialog(this, "You don't have permission to perform this action!", 
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    // Filter data based on user role
    private List<Student> getFilteredStudents() {
        User currentUser = loginSystem.getCurrentUser();
        String role = currentUser.getRole();
        
        if (role.equals("Admin") || role.equals("Teacher")) {
            return school.getAllStudents();
        } else if (role.equals("Student")) {
            // Student can only see their own record
            Student student = school.getStudent(currentUser.getAssociatedId());
            List<Student> result = new ArrayList<>();
            if (student != null) result.add(student);
            return result;
        } else if (role.equals("Parent")) {
            // Parent can only see their child's record
            Student student = school.getStudent(currentUser.getAssociatedId());
            List<Student> result = new ArrayList<>();
            if (student != null) result.add(student);
            return result;
        }
        return new ArrayList<>();
    }
    
    private List<Grade> getFilteredGrades() {
        User currentUser = loginSystem.getCurrentUser();
        String role = currentUser.getRole();
        
        if (role.equals("Admin")) {
            return school.getAllGrades();
        } else if (role.equals("Teacher")) {
            // Teachers can see all grades (in real system, filter by their subjects)
            return school.getAllGrades();
        } else if (role.equals("Student") || role.equals("Parent")) {
            return school.getStudentGrades(currentUser.getAssociatedId());
        }
        return new ArrayList<>();
    }
    
    // Student Management Methods
    private void addStudent() {
        if (!checkPermission("MANAGE_STUDENTS")) return;
        try {
            String firstName = stuFirstNameField.getText().trim();
            String lastName = stuLastNameField.getText().trim();
            LocalDate dob = LocalDate.parse(stuDOBField.getText().trim());
            String gender = (String) stuGenderCombo.getSelectedItem();
            String grade = stuGradeField.getText().trim();
            String parentName = stuParentNameField.getText().trim();
            String parentPhone = stuParentPhoneField.getText().trim();
            String parentEmail = stuParentEmailField.getText().trim();
            String address = stuAddressField.getText().trim();
            
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First Name and Last Name are required!");
                return;
            }
            
            String studentId = school.addStudent(firstName, lastName, dob, gender, grade, 
                                               parentName, parentPhone, parentEmail, address);
            
            // Create login accounts if usernames/passwords provided
            String studentUsername = stuUsernameField.getText().trim();
            String studentPassword = stuPasswordField.getText().trim();
            String parentUsername = stuParentUsernameField.getText().trim();
            String parentPassword = stuParentPasswordField.getText().trim();
            
            StringBuilder message = new StringBuilder("Student added successfully! ID: " + studentId);
            
            if (!studentUsername.isEmpty() && !studentPassword.isEmpty()) {
                String studentUserId = loginSystem.addUser(studentUsername, studentPassword, "Student", 
                                                         firstName + " " + lastName, "", studentId);
                message.append("\nStudent login created: ").append(studentUsername);
            }
            
            if (!parentUsername.isEmpty() && !parentPassword.isEmpty()) {
                String parentUserId = loginSystem.addUser(parentUsername, parentPassword, "Parent", 
                                                         parentName, parentEmail, studentId);
                message.append("\nParent login created: ").append(parentUsername);
            }
            
            refreshAllTables();
            clearStudentFields();
            JOptionPane.showMessageDialog(this, message.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void updateStudent() {
        if (!checkPermission("MANAGE_STUDENTS")) return;
        int row = studentTable.getSelectedRow();
        if (row >= 0) {
            try {
                String studentId = (String) studentTableModel.getValueAt(row, 0);
                String firstName = stuFirstNameField.getText().trim();
                String lastName = stuLastNameField.getText().trim();
                String grade = stuGradeField.getText().trim();
                String parentName = stuParentNameField.getText().trim();
                String parentPhone = stuParentPhoneField.getText().trim();
                String parentEmail = stuParentEmailField.getText().trim();
                String address = stuAddressField.getText().trim();
                
                if (school.updateStudent(studentId, firstName, lastName, grade, parentName, 
                                       parentPhone, parentEmail, address)) {
                    refreshAllTables();
                    clearStudentFields();
                    JOptionPane.showMessageDialog(this, "Student updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update student!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to update!");
        }
    }
    
    private void deleteStudent() {
        if (!checkPermission("MANAGE_STUDENTS")) return;
        int row = studentTable.getSelectedRow();
        if (row >= 0) {
            String studentId = (String) studentTableModel.getValueAt(row, 0);
            String studentName = (String) studentTableModel.getValueAt(row, 1);
            
            // Count related records
            int gradeCount = school.getStudentGrades(studentId).size();
            int attendanceCount = school.getStudentAttendance(studentId).size();
            
            String message = String.format(
                "Delete student %s (%s)?\n\n" +
                "This will also delete:\n" +
                "• %d grade records\n" +
                "• %d attendance records\n" +
                "• Fee payment history\n\n" +
                "This action cannot be undone!",
                studentId, studentName, gradeCount, attendanceCount
            );
            
            int confirm = JOptionPane.showConfirmDialog(this, message, 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (school.removeStudent(studentId)) {
                    refreshAllTables();
                    clearStudentFields();
                    JOptionPane.showMessageDialog(this, "Student and all related records deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete student!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!");
        }
    }
    
    private void searchStudents() {
        String keyword = JOptionPane.showInputDialog(this, "Enter search keyword:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Student> results = school.searchStudents(keyword.trim());
            updateStudentTable(results);
        }
    }
    
    private void loadStudentToFields(int row) {
        String studentId = (String) studentTableModel.getValueAt(row, 0);
        Student student = school.getStudent(studentId);
        if (student != null) {
            stuFirstNameField.setText(student.getFirstName());
            stuLastNameField.setText(student.getLastName());
            stuDOBField.setText(student.getFormattedDOB());
            stuGenderCombo.setSelectedItem(student.getGender());
            stuGradeField.setText(student.getGrade());
            stuParentNameField.setText(student.getParentName());
            stuParentPhoneField.setText(student.getParentPhone());
            stuParentEmailField.setText(student.getParentEmail());
            stuAddressField.setText(student.getAddress());
        }
    }
    
    private void clearStudentFields() {
        stuFirstNameField.setText("");
        stuLastNameField.setText("");
        stuDOBField.setText("");
        stuGenderCombo.setSelectedIndex(0);
        stuGradeField.setText("");
        stuParentNameField.setText("");
        stuParentPhoneField.setText("");
        stuParentEmailField.setText("");
        stuAddressField.setText("");
        stuUsernameField.setText("");
        stuPasswordField.setText("");
        stuParentUsernameField.setText("");
        stuParentPasswordField.setText("");
    }
    
    // Teacher Management Methods
    private void addTeacher() {
        if (!checkPermission("MANAGE_TEACHERS")) return;
        try {
            String firstName = teaFirstNameField.getText().trim();
            String lastName = teaLastNameField.getText().trim();
            String email = teaEmailField.getText().trim();
            String phone = teaPhoneField.getText().trim();
            String subject = teaSubjectField.getText().trim();
            String qualification = teaQualificationField.getText().trim();
            double salary = Double.parseDouble(teaSalaryField.getText().trim());
            String address = teaAddressField.getText().trim();
            
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First Name and Last Name are required!");
                return;
            }
            
            String teacherId = school.addTeacher(firstName, lastName, email, phone, subject, 
                                               qualification, salary, address);
            
            // Create login account if username/password provided
            String username = teaUsernameField.getText().trim();
            String password = teaPasswordField.getText().trim();
            
            StringBuilder message = new StringBuilder("Teacher added successfully! ID: " + teacherId);
            
            if (!username.isEmpty() && !password.isEmpty()) {
                String userId = loginSystem.addUser(username, password, "Teacher", 
                                                   firstName + " " + lastName, email, teacherId);
                message.append("\nLogin account created: ").append(username);
            }
            
            refreshAllTables();
            clearTeacherFields();
            JOptionPane.showMessageDialog(this, message.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void updateTeacher() {
        if (!checkPermission("MANAGE_TEACHERS")) return;
        int row = teacherTable.getSelectedRow();
        if (row >= 0) {
            try {
                String teacherId = (String) teacherTableModel.getValueAt(row, 0);
                String firstName = teaFirstNameField.getText().trim();
                String lastName = teaLastNameField.getText().trim();
                String email = teaEmailField.getText().trim();
                String phone = teaPhoneField.getText().trim();
                String subject = teaSubjectField.getText().trim();
                String qualification = teaQualificationField.getText().trim();
                double salary = Double.parseDouble(teaSalaryField.getText().trim());
                String address = teaAddressField.getText().trim();
                
                if (school.updateTeacher(teacherId, firstName, lastName, email, phone, subject, 
                                       qualification, salary, address)) {
                    refreshAllTables();
                    clearTeacherFields();
                    JOptionPane.showMessageDialog(this, "Teacher updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update teacher!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a teacher to update!");
        }
    }
    
    private void deleteTeacher() {
        if (!checkPermission("MANAGE_TEACHERS")) return;
        int row = teacherTable.getSelectedRow();
        if (row >= 0) {
            String teacherId = (String) teacherTableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete teacher " + teacherId + "?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (school.removeTeacher(teacherId)) {
                    refreshAllTables();
                    clearTeacherFields();
                    JOptionPane.showMessageDialog(this, "Teacher deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete teacher!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a teacher to delete!");
        }
    }
    
    private void loadTeacherToFields(int row) {
        String teacherId = (String) teacherTableModel.getValueAt(row, 0);
        Teacher teacher = school.getTeacher(teacherId);
        if (teacher != null) {
            teaFirstNameField.setText(teacher.getFirstName());
            teaLastNameField.setText(teacher.getLastName());
            teaEmailField.setText(teacher.getEmail());
            teaPhoneField.setText(teacher.getPhone());
            teaSubjectField.setText(teacher.getSubject());
            teaQualificationField.setText(teacher.getQualification());
            teaSalaryField.setText(String.valueOf(teacher.getSalary()));
            teaAddressField.setText(teacher.getAddress());
        }
    }
    
    private void clearTeacherFields() {
        teaFirstNameField.setText("");
        teaLastNameField.setText("");
        teaEmailField.setText("");
        teaPhoneField.setText("");
        teaSubjectField.setText("");
        teaQualificationField.setText("");
        teaSalaryField.setText("");
        teaAddressField.setText("");
        teaUsernameField.setText("");
        teaPasswordField.setText("");
    }
    
    // Grade Management Methods
    private void addGrade() {
        if (!checkPermission("MANAGE_GRADES")) return;
        try {
            String studentInfo = (String) gradeStudentCombo.getSelectedItem();
            if (studentInfo == null) {
                JOptionPane.showMessageDialog(this, "Please select a student!");
                return;
            }
            
            String studentId = studentInfo.split(" - ")[0];
            String subject = (String) gradeSubjectCombo.getSelectedItem();
            String examType = (String) gradeExamTypeCombo.getSelectedItem();
            double marks = Double.parseDouble(gradeMarksField.getText().trim());
            double totalMarks = Double.parseDouble(gradeTotalMarksField.getText().trim());
            String semester = gradeSemesterField.getText().trim();
            String academicYear = gradeYearField.getText().trim();
            
            String gradeId = school.addGrade(studentId, subject, examType, marks, totalMarks, 
                                           semester, academicYear);
            
            // Calculate and show updated GPA
            double overallGPA = school.calculateOverallGPA(studentId);
            double subjectGPA = school.calculateSubjectGPA(studentId, subject);
            
            refreshGradeTable();
            clearGradeFields();
            
            String message = String.format(
                "Grade added successfully! ID: %s\n\n" +
                "Updated GPAs for %s:\n" +
                "Overall GPA: %.2f%%\n" +
                "%s GPA: %.2f%%",
                gradeId, studentInfo.split(" - ")[1], overallGPA, subject, subjectGPA
            );
            
            JOptionPane.showMessageDialog(this, message, "Grade Added - GPA Updated", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void calculateGPA() {
        String studentInfo = (String) gradeStudentCombo.getSelectedItem();
        if (studentInfo == null) {
            JOptionPane.showMessageDialog(this, "Please select a student!");
            return;
        }
        
        String studentId = studentInfo.split(" - ")[0];
        String semester = gradeSemesterField.getText().trim();
        String academicYear = gradeYearField.getText().trim();
        
        if (semester.isEmpty() || academicYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter semester and academic year!");
            return;
        }
        
        double gpa = school.calculateStudentGPA(studentId, semester, academicYear);
        JOptionPane.showMessageDialog(this, String.format("GPA for %s in %s %s: %.2f%%", 
                studentInfo, semester, academicYear, gpa));
    }
    
    private void clearGradeFields() {
        gradeMarksField.setText("");
        gradeTotalMarksField.setText("");
        gradeSemesterField.setText("");
        gradeYearField.setText("");
    }
    
    // Attendance Management Methods
    private void markAttendance() {
        if (!checkPermission("MANAGE_ATTENDANCE")) return;
        try {
            String studentInfo = (String) attStudentCombo.getSelectedItem();
            if (studentInfo == null) {
                JOptionPane.showMessageDialog(this, "Please select a student!");
                return;
            }
            
            String studentId = studentInfo.split(" - ")[0];
            LocalDate date = LocalDate.parse(attDateField.getText().trim());
            String status = (String) attStatusCombo.getSelectedItem();
            String remarks = attRemarksField.getText().trim();
            
            String attendanceId = school.markAttendance(studentId, date, status, remarks);
            refreshAttendanceTable();
            clearAttendanceFields();
            JOptionPane.showMessageDialog(this, "Attendance marked successfully! ID: " + attendanceId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void viewStudentAttendance() {
        String studentInfo = (String) attStudentCombo.getSelectedItem();
        if (studentInfo == null) {
            JOptionPane.showMessageDialog(this, "Please select a student!");
            return;
        }
        
        String studentId = studentInfo.split(" - ")[0];
        List<Attendance> attendance = school.getStudentAttendance(studentId);
        
        StringBuilder report = new StringBuilder();
        report.append("Attendance Report for ").append(studentInfo).append("\n\n");
        
        for (Attendance att : attendance) {
            report.append(att.toString()).append("\n");
        }
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Attendance Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void calculateAttendancePercentage() {
        String studentInfo = (String) attStudentCombo.getSelectedItem();
        if (studentInfo == null) {
            JOptionPane.showMessageDialog(this, "Please select a student!");
            return;
        }
        
        String startDateStr = JOptionPane.showInputDialog(this, "Enter start date (YYYY-MM-DD):");
        String endDateStr = JOptionPane.showInputDialog(this, "Enter end date (YYYY-MM-DD):");
        
        try {
            String studentId = studentInfo.split(" - ")[0];
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            
            double percentage = school.calculateAttendancePercentage(studentId, startDate, endDate);
            JOptionPane.showMessageDialog(this, String.format("Attendance percentage for %s from %s to %s: %.2f%%", 
                    studentInfo, startDateStr, endDateStr, percentage));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void clearAttendanceFields() {
        attDateField.setText(LocalDate.now().toString());
        attStatusCombo.setSelectedIndex(0);
        attRemarksField.setText("");
    }
    
    // Fee Management Methods
    private void addFeePayment() {
        if (!checkPermission("MANAGE_FEES")) return;
        try {
            String studentInfo = (String) feeStudentCombo.getSelectedItem();
            if (studentInfo == null) {
                JOptionPane.showMessageDialog(this, "Please select a student!");
                return;
            }
            
            String studentId = studentInfo.split(" - ")[0];
            double amount = Double.parseDouble(feeAmountField.getText().trim());
            
            if (school.addFeePayment(studentId, amount)) {
                refreshFeeTable();
                clearFeeFields();
                JOptionPane.showMessageDialog(this, "Fee payment added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add fee payment!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void setTotalFees() {
        if (!checkPermission("MANAGE_FEES")) return;
        try {
            String studentInfo = (String) feeStudentCombo.getSelectedItem();
            if (studentInfo == null) {
                JOptionPane.showMessageDialog(this, "Please select a student!");
                return;
            }
            
            String studentId = studentInfo.split(" - ")[0];
            double totalFees = Double.parseDouble(feeTotalField.getText().trim());
            
            if (school.setStudentFees(studentId, totalFees)) {
                refreshFeeTable();
                clearFeeFields();
                JOptionPane.showMessageDialog(this, "Total fees set successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to set total fees!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void viewOutstandingFees() {
        List<Student> studentsWithOutstanding = school.getStudentsWithOutstandingFees();
        
        StringBuilder report = new StringBuilder();
        report.append("Students with Outstanding Fees:\n\n");
        
        double totalOutstanding = 0;
        for (Student student : studentsWithOutstanding) {
            double outstanding = student.getOutstandingFees();
            report.append(String.format("%s - %s: $%.2f\n", 
                    student.getStudentId(), student.getFullName(), outstanding));
            totalOutstanding += outstanding;
        }
        
        report.append(String.format("\nTotal Outstanding: $%.2f", totalOutstanding));
        
        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Outstanding Fees Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearFeeFields() {
        feeAmountField.setText("");
        feeTotalField.setText("");
    }
    
    // Refresh Methods
    private void refreshAllTables() {
        if (studentTableModel != null) refreshStudentTable();
        if (teacherTableModel != null) refreshTeacherTable();
        if (gradeTableModel != null) refreshGradeTable();
        if (attendanceTableModel != null) refreshAttendanceTable();
        if (feeTableModel != null) refreshFeeTable();
        updateComboBoxes();
        
        // Reapply theme after refresh
        SwingUtilities.invokeLater(() -> ThemeManager.applyTheme(this));
    }
    
    private void refreshStudentTable() {
        updateStudentTable(getFilteredStudents());
    }
    
    private void updateStudentTable(List<Student> students) {
        studentTableModel.setRowCount(0);
        // Use the provided students list, but filter if needed
        List<Student> studentsToShow = students;
        User currentUser = loginSystem.getCurrentUser();
        String role = currentUser.getRole();
        
        // Apply role-based filtering
        if (role.equals("Student") || role.equals("Parent")) {
            studentsToShow = getFilteredStudents();
        }
        
        for (Student student : studentsToShow) {
            Object[] row = {
                student.getStudentId(),
                student.getFullName(),
                student.getGrade(),
                student.getGender(),
                student.getParentName(),
                student.getParentPhone(),
                student.getParentEmail(),
                student.getStatus()
            };
            studentTableModel.addRow(row);
        }
    }
    
    private void refreshTeacherTable() {
        teacherTableModel.setRowCount(0);
        for (Teacher teacher : school.getAllTeachers()) {
            Object[] row = {
                teacher.getTeacherId(),
                teacher.getFullName(),
                teacher.getSubject(),
                teacher.getEmail(),
                teacher.getPhone(),
                teacher.getQualification(),
                String.format("$%.2f", teacher.getSalary()),
                teacher.getStatus()
            };
            teacherTableModel.addRow(row);
        }
    }
    
    private void refreshGradeTable() {
        gradeTableModel.setRowCount(0);
        for (Grade grade : getFilteredGrades()) {
            Student student = school.getStudent(grade.getStudentId());
            String studentName = student != null ? student.getFullName() : "Unknown";
            
            Object[] row = {
                grade.getStudentId(),
                studentName,
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
    
    private void refreshAttendanceTable() {
        attendanceTableModel.setRowCount(0);
        for (Attendance attendance : school.getAttendanceByDate(LocalDate.now())) {
            Student student = school.getStudent(attendance.getStudentId());
            String studentName = student != null ? student.getFullName() : "Unknown";
            
            Object[] row = {
                attendance.getStudentId(),
                studentName,
                attendance.getFormattedDate(),
                attendance.getStatus(),
                attendance.getRemarks()
            };
            attendanceTableModel.addRow(row);
        }
    }
    
    private void refreshFeeTable() {
        feeTableModel.setRowCount(0);
        for (Student student : school.getAllStudents()) {
            String status = student.getOutstandingFees() > 0 ? "Outstanding" : "Paid";
            
            Object[] row = {
                student.getStudentId(),
                student.getFullName(),
                student.getGrade(),
                String.format("$%.2f", student.getFeesTotal()),
                String.format("$%.2f", student.getFeesPaid()),
                String.format("$%.2f", student.getOutstandingFees()),
                status
            };
            feeTableModel.addRow(row);
        }
    }
    
    private void updateComboBoxes() {
        // Update student combo boxes only if they exist
        if (gradeStudentCombo != null) {
            gradeStudentCombo.removeAllItems();
            for (Student student : school.getAllStudents()) {
                String item = student.getStudentId() + " - " + student.getFullName();
                gradeStudentCombo.addItem(item);
            }
        }
        
        if (attStudentCombo != null) {
            attStudentCombo.removeAllItems();
            for (Student student : school.getAllStudents()) {
                String item = student.getStudentId() + " - " + student.getFullName();
                attStudentCombo.addItem(item);
            }
        }
        
        if (feeStudentCombo != null) {
            feeStudentCombo.removeAllItems();
            for (Student student : school.getAllStudents()) {
                String item = student.getStudentId() + " - " + student.getFullName();
                feeStudentCombo.addItem(item);
            }
        }
    }
    
    private JPanel createDashboardTab() {
        return new DashboardPanel(school, loginSystem);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", 
                "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Save data before logout
            DataPersistence.saveData(school, loginSystem);
            
            loginSystem.logout();
            dispose();
            
            // Restart application
            SwingUtilities.invokeLater(() -> {
                new SchoolManagementGUI().setVisible(true);
            });
        }
    }
    
    private void changePassword() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();
        
        panel.add(new JLabel("Current Password:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match!");
                return;
            }
            
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password must be at least 6 characters!");
                return;
            }
            
            if (loginSystem.changePassword(loginSystem.getCurrentUser().getUsername(), 
                    oldPassword, newPassword)) {
                JOptionPane.showMessageDialog(this, "Password changed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Current password is incorrect!");
            }
        }
    }
    
    private void toggleDarkMode() {
        ThemeManager.setDarkMode(!ThemeManager.isDarkMode());
        ThemeManager.applyTheme(this);
        
        // Update button text
        Component[] components = ((JPanel) getContentPane().getComponent(1)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().contains("Dark") || btn.getText().contains("🌙")) {
                    btn.setText(ThemeManager.isDarkMode() ? "☀️ Light Mode" : "🌙 Dark Mode");
                    break;
                }
            }
        }
        
        repaint();
    }
    
    private void updateLoginSystemWithActualIds() {
        // No dummy users to update
    }
    
    private void loadSampleData() {
        // No sample data - start with empty system
        System.out.println("Starting with empty system - add your own data!");
    }

    // Small main entry so this GUI can be launched directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SchoolManagementGUI().setVisible(true);
        });
    }
}