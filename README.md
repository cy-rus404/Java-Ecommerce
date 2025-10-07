# School Management System

A Java Swing-based school management application with role-based access control and data persistence.

## Features

- **Multi-User System**: Admin, Teacher, Student, and Parent roles
- **Student Management**: Add, edit, remove students with cascading deletes
- **Grade Tracking**: Automatic GPA calculations and grade management
- **Attendance System**: Track and calculate attendance percentages
- **Fee Management**: Monitor fee payments and outstanding balances
- **Data Persistence**: File-based storage maintains data between sessions
- **Dark Mode**: Toggle between light and dark themes

## Getting Started

### Prerequisites
- Java 8 or higher
- VS Code with Java Extension Pack (recommended)

### Running the Application
```bash
cd HelloWorld
javac -d bin src/*.java
java -cp bin App
```

### Default Login
- **Username**: admin
- **Password**: admin123

## Usage

1. Login with admin credentials
2. Add students and teachers through respective tabs
3. Create user accounts when adding students/teachers
4. Mark attendance and add grades
5. View personalized dashboards for students/parents

## Project Structure
```
src/
├── App.java                    # Main entry point
├── SchoolManagementGUI.java    # Main interface
├── SchoolManagementSystem.java # Core business logic
├── LoginSystem.java           # Authentication
├── DashboardPanel.java        # Student/parent dashboard
├── DataPersistence.java       # File storage
├── ThemeManager.java          # Dark/light themes
└── Models/
    ├── Student.java
    ├── Teacher.java
    ├── Grade.java
    ├── Attendance.java
    └── User.java
```

## Data Storage
Application data is stored in `school_data/` directory with automatic backup on each session.
