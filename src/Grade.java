public class Grade {
    private String gradeId;
    private String studentId;
    private String subject;
    private String examType; // Quiz, Midterm, Final, Assignment
    private double marks;
    private double totalMarks;
    private String grade;
    private String semester;
    private String academicYear;
    
    public Grade(String gradeId, String studentId, String subject, String examType,
                double marks, double totalMarks, String semester, String academicYear) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.subject = subject;
        this.examType = examType;
        this.marks = marks;
        this.totalMarks = totalMarks;
        this.semester = semester;
        this.academicYear = academicYear;
        this.grade = calculateGrade();
    }
    
    private String calculateGrade() {
        double percentage = (marks / totalMarks) * 100;
        if (percentage >= 90) return "A+";
        else if (percentage >= 80) return "A";
        else if (percentage >= 70) return "B";
        else if (percentage >= 60) return "C";
        else if (percentage >= 50) return "D";
        else return "F";
    }
    
    // Getters
    public String getGradeId() { return gradeId; }
    public String getStudentId() { return studentId; }
    public String getSubject() { return subject; }
    public String getExamType() { return examType; }
    public double getMarks() { return marks; }
    public double getTotalMarks() { return totalMarks; }
    public String getGrade() { return grade; }
    public String getSemester() { return semester; }
    public String getAcademicYear() { return academicYear; }
    
    // Setters
    public void setMarks(double marks) { 
        this.marks = marks; 
        this.grade = calculateGrade();
    }
    public void setTotalMarks(double totalMarks) { 
        this.totalMarks = totalMarks;
        this.grade = calculateGrade();
    }
    
    public double getPercentage() {
        return (marks / totalMarks) * 100;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s: %.1f/%.1f (%s)", subject, examType, marks, totalMarks, grade);
    }
}