import java.time.LocalDate;
import java.util.*;

public class SchoolManagementSystem {
    private Map<String, Student> students;
    private Map<String, Teacher> teachers;
    private List<Grade> grades;
    private List<Attendance> attendanceRecords;
    private int studentCounter;
    private int teacherCounter;
    private int gradeCounter;
    private int attendanceCounter;
    
    public SchoolManagementSystem() {
        students = new HashMap<>();
        teachers = new HashMap<>();
        grades = new ArrayList<>();
        attendanceRecords = new ArrayList<>();
        studentCounter = 1000;
        teacherCounter = 100;
        gradeCounter = 10000;
        attendanceCounter = 50000;
    }
    
    // Student Management
    public String addStudent(String firstName, String lastName, LocalDate dateOfBirth,
                           String gender, String grade, String parentName, String parentPhone,
                           String parentEmail, String address) {
        String studentId = "STU" + (++studentCounter);
        Student student = new Student(studentId, firstName, lastName, dateOfBirth, gender,
                                    grade, parentName, parentPhone, parentEmail, address);
        students.put(studentId, student);
        return studentId;
    }
    
    public boolean updateStudent(String studentId, String firstName, String lastName,
                               String grade, String parentName, String parentPhone,
                               String parentEmail, String address) {
        Student student = students.get(studentId);
        if (student != null) {
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setGrade(grade);
            student.setParentName(parentName);
            student.setParentPhone(parentPhone);
            student.setParentEmail(parentEmail);
            student.setAddress(address);
            return true;
        }
        return false;
    }
    
    public boolean removeStudent(String studentId) {
        if (students.remove(studentId) != null) {
            // Cascading delete - remove all related records
            removeStudentGrades(studentId);
            removeStudentAttendance(studentId);
            return true;
        }
        return false;
    }
    
    private void removeStudentGrades(String studentId) {
        grades.removeIf(grade -> grade.getStudentId().equals(studentId));
    }
    
    private void removeStudentAttendance(String studentId) {
        attendanceRecords.removeIf(attendance -> attendance.getStudentId().equals(studentId));
    }
    
    public Student getStudent(String studentId) {
        return students.get(studentId);
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    public List<Student> getStudentsByGrade(String grade) {
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getGrade().equals(grade)) {
                result.add(student);
            }
        }
        return result;
    }
    
    // Teacher Management
    public String addTeacher(String firstName, String lastName, String email, String phone,
                           String subject, String qualification, double salary, String address) {
        String teacherId = "TEA" + (++teacherCounter);
        Teacher teacher = new Teacher(teacherId, firstName, lastName, email, phone,
                                    subject, qualification, salary, address);
        teachers.put(teacherId, teacher);
        return teacherId;
    }
    
    public boolean updateTeacher(String teacherId, String firstName, String lastName,
                               String email, String phone, String subject, String qualification,
                               double salary, String address) {
        Teacher teacher = teachers.get(teacherId);
        if (teacher != null) {
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacher.setEmail(email);
            teacher.setPhone(phone);
            teacher.setSubject(subject);
            teacher.setQualification(qualification);
            teacher.setSalary(salary);
            teacher.setAddress(address);
            return true;
        }
        return false;
    }
    
    public boolean removeTeacher(String teacherId) {
        if (teachers.remove(teacherId) != null) {
            // Note: In a real system, you might want to reassign grades to another teacher
            // For now, we'll keep the grades but the teacher reference will be invalid
            return true;
        }
        return false;
    }
    
    public Teacher getTeacher(String teacherId) {
        return teachers.get(teacherId);
    }
    
    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers.values());
    }
    
    // Grade Management
    public String addGrade(String studentId, String subject, String examType,
                         double marks, double totalMarks, String semester, String academicYear) {
        String gradeId = "GRD" + (++gradeCounter);
        Grade grade = new Grade(gradeId, studentId, subject, examType, marks, totalMarks,
                              semester, academicYear);
        grades.add(grade);
        return gradeId;
    }
    
    public List<Grade> getStudentGrades(String studentId) {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId)) {
                result.add(grade);
            }
        }
        return result;
    }
    
    public List<Grade> getAllGrades() {
        return new ArrayList<>(grades);
    }
    
    public double calculateStudentGPA(String studentId, String semester, String academicYear) {
        List<Grade> studentGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId) && 
                grade.getSemester().equals(semester) &&
                grade.getAcademicYear().equals(academicYear)) {
                studentGrades.add(grade);
            }
        }
        
        if (studentGrades.isEmpty()) return 0.0;
        
        double totalPercentage = 0;
        for (Grade grade : studentGrades) {
            totalPercentage += grade.getPercentage();
        }
        return totalPercentage / studentGrades.size();
    }
    
    // Calculate overall GPA for all grades of a student
    public double calculateOverallGPA(String studentId) {
        List<Grade> studentGrades = getStudentGrades(studentId);
        
        if (studentGrades.isEmpty()) return 0.0;
        
        double totalPercentage = 0;
        for (Grade grade : studentGrades) {
            totalPercentage += grade.getPercentage();
        }
        return totalPercentage / studentGrades.size();
    }
    
    // Get GPA by subject for a student
    public double calculateSubjectGPA(String studentId, String subject) {
        List<Grade> subjectGrades = new ArrayList<>();
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId) && grade.getSubject().equals(subject)) {
                subjectGrades.add(grade);
            }
        }
        
        if (subjectGrades.isEmpty()) return 0.0;
        
        double totalPercentage = 0;
        for (Grade grade : subjectGrades) {
            totalPercentage += grade.getPercentage();
        }
        return totalPercentage / subjectGrades.size();
    }
    
    // Attendance Management
    public String markAttendance(String studentId, LocalDate date, String status, String remarks) {
        String attendanceId = "ATT" + (++attendanceCounter);
        Attendance attendance = new Attendance(attendanceId, studentId, date, status, remarks);
        attendanceRecords.add(attendance);
        return attendanceId;
    }
    
    public List<Attendance> getStudentAttendance(String studentId) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance attendance : attendanceRecords) {
            if (attendance.getStudentId().equals(studentId)) {
                result.add(attendance);
            }
        }
        return result;
    }
    
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        List<Attendance> result = new ArrayList<>();
        for (Attendance attendance : attendanceRecords) {
            if (attendance.getDate().equals(date)) {
                result.add(attendance);
            }
        }
        return result;
    }
    
    public double calculateAttendancePercentage(String studentId, LocalDate startDate, LocalDate endDate) {
        int totalDays = 0;
        int presentDays = 0;
        
        for (Attendance attendance : attendanceRecords) {
            if (attendance.getStudentId().equals(studentId) &&
                !attendance.getDate().isBefore(startDate) &&
                !attendance.getDate().isAfter(endDate)) {
                totalDays++;
                if (attendance.getStatus().equals("Present") || attendance.getStatus().equals("Late")) {
                    presentDays++;
                }
            }
        }
        
        return totalDays > 0 ? (double) presentDays / totalDays * 100 : 0.0;
    }
    
    // Fee Management
    public boolean addFeePayment(String studentId, double amount) {
        Student student = students.get(studentId);
        if (student != null) {
            student.addFeePayment(amount);
            return true;
        }
        return false;
    }
    
    public boolean setStudentFees(String studentId, double totalFees) {
        Student student = students.get(studentId);
        if (student != null) {
            student.setFeesTotal(totalFees);
            return true;
        }
        return false;
    }
    
    public List<Student> getStudentsWithOutstandingFees() {
        List<Student> result = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getOutstandingFees() > 0) {
                result.add(student);
            }
        }
        return result;
    }
    
    // Search functionality
    public List<Student> searchStudents(String keyword) {
        List<Student> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Student student : students.values()) {
            if (student.getFullName().toLowerCase().contains(lowerKeyword) ||
                student.getStudentId().toLowerCase().contains(lowerKeyword) ||
                student.getGrade().toLowerCase().contains(lowerKeyword)) {
                results.add(student);
            }
        }
        return results;
    }
    
    public List<Teacher> searchTeachers(String keyword) {
        List<Teacher> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        
        for (Teacher teacher : teachers.values()) {
            if (teacher.getFullName().toLowerCase().contains(lowerKeyword) ||
                teacher.getTeacherId().toLowerCase().contains(lowerKeyword) ||
                teacher.getSubject().toLowerCase().contains(lowerKeyword)) {
                results.add(teacher);
            }
        }
        return results;
    }
    
    public Set<String> getGrades() {
        Set<String> gradeSet = new HashSet<>();
        for (Student student : students.values()) {
            gradeSet.add(student.getGrade());
        }
        return gradeSet;
    }
    
    public Set<String> getSubjects() {
        Set<String> subjectSet = new HashSet<>();
        for (Teacher teacher : teachers.values()) {
            subjectSet.add(teacher.getSubject());
        }
        for (Grade grade : grades) {
            subjectSet.add(grade.getSubject());
        }
        return subjectSet;
    }
    
    // Methods for loading existing data
    public void addExistingStudent(Student student) {
        students.put(student.getStudentId(), student);
    }
    
    public void addExistingTeacher(Teacher teacher) {
        teachers.put(teacher.getTeacherId(), teacher);
    }
    
    public void addExistingGrade(Grade grade) {
        grades.add(grade);
    }
    
    public void addExistingAttendance(Attendance attendance) {
        attendanceRecords.add(attendance);
    }
    
    // Data integrity methods
    public void cleanupOrphanedRecords() {
            // Remove grades for non-existent students
        grades.removeIf(grade -> !students.containsKey(grade.getStudentId()));
        
        // Remove attendance for non-existent students
        attendanceRecords.removeIf(attendance -> !students.containsKey(attendance.getStudentId()));
    }
    
    public List<String> getOrphanedGradeStudents() {
        List<String> orphaned = new ArrayList<>();
        for (Grade grade : grades) {
            if (!students.containsKey(grade.getStudentId()) && !orphaned.contains(grade.getStudentId())) {
                orphaned.add(grade.getStudentId());
            }
        }
        return orphaned;
    }
    
    public List<String> getOrphanedAttendanceStudents() {
        List<String> orphaned = new ArrayList<>();
        for (Attendance attendance : attendanceRecords) {
            if (!students.containsKey(attendance.getStudentId()) && !orphaned.contains(attendance.getStudentId())) {
                orphaned.add(attendance.getStudentId());
            }
        }
        return orphaned;
    }
}