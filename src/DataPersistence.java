import java.io.*;
import java.util.*;

public class DataPersistence {
    private static final String DATA_DIR = "school_data";
    private static final String STUDENTS_FILE = DATA_DIR + "/students.txt";
    private static final String TEACHERS_FILE = DATA_DIR + "/teachers.txt";
    private static final String GRADES_FILE = DATA_DIR + "/grades.txt";
    private static final String ATTENDANCE_FILE = DATA_DIR + "/attendance.txt";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    
    public static void saveData(SchoolManagementSystem school, LoginSystem loginSystem) {
        try {
            // Create data directory if it doesn't exist
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Save students
            saveStudents(school.getAllStudents());
            
            // Save teachers  
            saveTeachers(school.getAllTeachers());
            
            // Save grades
            saveGrades(school.getAllGrades());
            
            // Save attendance
            saveAttendance(school);
            
            // Save users
            saveUsers(loginSystem.getAllUsers());
            
            System.out.println("Data saved successfully!");
            
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    public static boolean loadData(SchoolManagementSystem school, LoginSystem loginSystem) {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) {
                return false; // No saved data
            }
            
            // Load students
            loadStudents(school);
            
            // Load teachers
            loadTeachers(school);
            
            // Load grades  
            loadGrades(school);
            
            // Load attendance
            loadAttendance(school);
            
            // Load users
            loadUsers(loginSystem);
            
            System.out.println("Data loaded successfully!");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            return false;
        }
    }
    
    private static void saveStudents(List<Student> students) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student student : students) {
                writer.println(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%.2f|%.2f",
                    student.getStudentId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getFormattedDOB(),
                    student.getGender(),
                    student.getGrade(),
                    student.getParentName(),
                    student.getParentPhone(),
                    student.getParentEmail(),
                    student.getAddress(),
                    student.getStatus(),
                    student.getFeesTotal(),
                    student.getFeesPaid()
                ));
            }
        }
    }
    
    private static void loadStudents(SchoolManagementSystem school) throws IOException {
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 11) {
                    // Create student with existing ID
                    Student student = new Student(
                        parts[0], // id
                        parts[1], // firstName
                        parts[2], // lastName
                        java.time.LocalDate.parse(parts[3]), // dob
                        parts[4], // gender
                        parts[5], // grade
                        parts[6], // parentName
                        parts[7], // parentPhone
                        parts[8], // parentEmail
                        parts[9]  // address
                    );
                    student.setStatus(parts[10]);
                    if (parts.length > 11) student.setFeesTotal(Double.parseDouble(parts[11]));
                    if (parts.length > 12) student.addFeePayment(Double.parseDouble(parts[12]));
                    
                    // Add to school system
                    school.addExistingStudent(student);
                }
            }
        }
    }
    
    private static void saveTeachers(List<Teacher> teachers) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TEACHERS_FILE))) {
            for (Teacher teacher : teachers) {
                writer.println(String.format("%s|%s|%s|%s|%s|%s|%s|%.2f|%s|%s",
                    teacher.getTeacherId(),
                    teacher.getFirstName(),
                    teacher.getLastName(),
                    teacher.getEmail(),
                    teacher.getPhone(),
                    teacher.getSubject(),
                    teacher.getQualification(),
                    teacher.getSalary(),
                    teacher.getAddress(),
                    teacher.getStatus()
                ));
            }
        }
    }
    
    private static void loadTeachers(SchoolManagementSystem school) throws IOException {
        File file = new File(TEACHERS_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 9) {
                    Teacher teacher = new Teacher(
                        parts[0], // id
                        parts[1], // firstName
                        parts[2], // lastName
                        parts[3], // email
                        parts[4], // phone
                        parts[5], // subject
                        parts[6], // qualification
                        Double.parseDouble(parts[7]), // salary
                        parts[8]  // address
                    );
                    if (parts.length > 9) teacher.setStatus(parts[9]);
                    
                    school.addExistingTeacher(teacher);
                }
            }
        }
    }
    
    private static void saveGrades(List<Grade> grades) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(GRADES_FILE))) {
            for (Grade grade : grades) {
                writer.println(String.format("%s|%s|%s|%s|%.2f|%.2f|%s|%s",
                    grade.getGradeId(),
                    grade.getStudentId(),
                    grade.getSubject(),
                    grade.getExamType(),
                    grade.getMarks(),
                    grade.getTotalMarks(),
                    grade.getSemester(),
                    grade.getAcademicYear()
                ));
            }
        }
    }
    
    private static void loadGrades(SchoolManagementSystem school) throws IOException {
        File file = new File(GRADES_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 8) {
                    school.addExistingGrade(new Grade(
                        parts[0], // gradeId
                        parts[1], // studentId
                        parts[2], // subject
                        parts[3], // examType
                        Double.parseDouble(parts[4]), // marks
                        Double.parseDouble(parts[5]), // totalMarks
                        parts[6], // semester
                        parts[7]  // academicYear
                    ));
                }
            }
        }
    }
    
    private static void saveUsers(List<User> users) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.println(String.format("%s|%s|%s|%s|%s|%s|%s|%b",
                    user.getUserId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getAssociatedId(),
                    user.isActive()
                ));
            }
        }
    }
    
    private static void saveAttendance(SchoolManagementSystem school) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_FILE))) {
            for (Student student : school.getAllStudents()) {
                for (Attendance attendance : school.getStudentAttendance(student.getStudentId())) {
                    writer.println(String.format("%s|%s|%s|%s|%s",
                        attendance.getAttendanceId(),
                        attendance.getStudentId(),
                        attendance.getDate().toString(),
                        attendance.getStatus(),
                        attendance.getRemarks()
                    ));
                }
            }
        }
    }
    
    private static void loadAttendance(SchoolManagementSystem school) throws IOException {
        File file = new File(ATTENDANCE_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    school.addExistingAttendance(new Attendance(
                        parts[0], // attendanceId
                        parts[1], // studentId
                        java.time.LocalDate.parse(parts[2]), // date
                        parts[3], // status
                        parts[4]  // remarks
                    ));
                }
            }
        }
    }
    
    private static void loadUsers(LoginSystem loginSystem) throws IOException {
        File file = new File(USERS_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    User user = new User(
                        parts[0], // userId
                        parts[1], // username
                        parts[2], // password
                        parts[3], // role
                        parts[4], // fullName
                        parts[5], // email
                        parts[6]  // associatedId
                    );
                    if (parts.length > 7) user.setActive(Boolean.parseBoolean(parts[7]));
                    
                    loginSystem.addExistingUser(user);
                }
            }
        }
    }
}