package com.skilltwin.cli;

import com.skilltwin.model.Student;
import com.skilltwin.model.Skill;
import com.skilltwin.service.StudentService;
import com.skilltwin.service.SkillService;
import com.skilltwin.model.LearningGoal;
import com.skilltwin.service.LearningGoalService;
import com.skilltwin.service.SimulationService;
import com.skilltwin.model.LearningScenario;
import com.skilltwin.service.ScenarioComparisonService;
import com.skilltwin.model.ProgressRecord;
import com.skilltwin.service.ProgressService;
import com.skilltwin.service.LearningSummaryService;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final StudentService studentService = new StudentService();
    private final SkillService skillService = new SkillService();
    private final LearningGoalService goalService = new LearningGoalService();
    private final SimulationService simulationService = new SimulationService();
    private final ScenarioComparisonService comparisonService = new ScenarioComparisonService();
    private final ProgressService progressService = new ProgressService();
    private final LearningSummaryService learningSummaryService = new LearningSummaryService();
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n================================");
            System.out.println("       SKILLTWIN AI");
            System.out.println("================================");
            System.out.println("1. Register Student");
            System.out.println("2. View All Students");
            System.out.println("3. Add Student Skill");
            System.out.println("4. View Student Skills");
            System.out.println("5. View Skill Progress");
            System.out.println("6. Create Learning Goal");
            System.out.println("7. View Learning Goals");
            System.out.println("8. Run Digital Twin Simulation");
            System.out.println("9. Compare Learning Scenarios");
            System.out.println("10. Create Learning Scenario");
            System.out.println("11. Record Actual Learning Progress");
            System.out.println("12. View Progress History");
            System.out.println("13. View Learning Progress Summary");
            System.out.println("14. Update Learning Goal Status");
            System.out.println("0. Exit");
            System.out.println("--------------------------------");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        registerStudent();
                        break;

                    case "2":
                        viewAllStudents();
                        break;

                    case "3":
                        addStudentSkill();
                        break;

                    case "4":
                        viewStudentSkills();
                        break;

                    case "5":
                        viewSkillProgress();
                        break;

                    case "6":
                        createLearningGoal();
                        break;

                    case "7":
                        viewLearningGoals();
                        break;

                    case "8":
                        runDigitalTwinSimulation();
                        break;

                    case "9":
                        compareLearningScenarios();
                        break;

                    case "10":
                        createLearningScenario();
                        break;

                
                    case "11":
                        recordActualProgress();
                        break;

                    case "12":
                        viewProgressHistory();
                        break;

                    case "13":
                        viewLearningSummary();
                        break;

                    case "14":
                        updateLearningGoalStatus();
                        break;

                    case "0":
                        running = false;
                        System.out.println("Thank you for using SkillTwin AI!");
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Please select 0 to 7.");
                }
            } 
            catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid numeric value.");
            } 
            catch (IllegalArgumentException e) {
                System.out.println("Validation error: " + e.getMessage());
            } 
            catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
    }
    private void registerStudent() throws SQLException {
        System.out.println("\n--- Register Student ---");
        System.out.print("Enter full name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter semester (1-12): ");
        int semester = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter email (optional): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            email = null;
        }
        System.out.print("Available study hours per day (0-24): ");
        String hoursInput = scanner.nextLine().trim();
        BigDecimal hours = hoursInput.isEmpty()
                ? new BigDecimal("2.00")
                : new BigDecimal(hoursInput);
        Student student = new Student();
        student.setFullName(name);
        student.setSemester(semester);
        student.setEmail(email);
        student.setAvailableHoursPerDay(hours);
        boolean success = studentService.registerStudent(student);
            if (success) {
                System.out.println("Student registered successfully!");
            } 
            else {
                System.out.println("Student registration failed.");
            }
        }
    private void viewAllStudents() throws SQLException {
        System.out.println("\n--- All Registered Students ---");
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) 
        {
            System.out.println("No students registered yet.");
            return;
        }
        for (Student student : students) {
            System.out.println(
                    "ID: " + student.getStudentId()
                    + " | Name: " + student.getFullName()
                    + " | Semester: " + student.getSemester()
                    + " | Email: "
                    + (student.getEmail() == null
                       ? "Not provided" : student.getEmail())
                    + " | Study Hours/Day: "
                    + student.getAvailableHoursPerDay()
            );
        }
    }
    private void addStudentSkill() throws SQLException {
        System.out.println("\n--- Add Student Skill ---");
        System.out.print("Enter Student ID: ");
        long studentId = Long.parseLong(scanner.nextLine().trim());
        System.out.print("Enter skill name (e.g., Java, Python): ");
        String skillName = scanner.nextLine().trim();
        System.out.print("Enter current level (0-100): ");
        int currentLevel = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter target level (0-100): ");
        int targetLevel =Integer.parseInt(scanner.nextLine().trim());
        long skillId = skillService.addSkill(studentId,skillName,currentLevel,targetLevel);
        System.out.println("Skill added successfully! Skill ID: " + skillId);
    }
    private void viewStudentSkills() throws SQLException {
        System.out.println("\n--- View Student Skills ---");
        System.out.print("Enter Student ID: ");
        long studentId = Long.parseLong(scanner.nextLine().trim());
        List<Skill> skills = skillService.getSkillsByStudentId(studentId);
        if (skills.isEmpty()) {
            System.out.println("No skills found for Student ID " + studentId);
            return;
        }
        for (Skill skill : skills) {
            System.out.println(skill);
        }
    }
    private void viewSkillProgress() throws SQLException {
    System.out.println("\n--- Skill Progress Tracking ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    List<Skill> skills = skillService.getSkillsByStudentId(studentId);
    if (skills.isEmpty()) {
        System.out.println("No skills found for Student ID " + studentId);
        return;
    }
    for (Skill skill : skills) {
        int currentLevel = skill.getCurrentLevel();
        int targetLevel = skill.getTargetLevel();
        int remainingGap = Math.max(0, targetLevel - currentLevel);
        double progress;
        if (targetLevel == 0 || currentLevel >= targetLevel) {
            progress = 100.0;
        } 
        else {
            progress = (currentLevel * 100.0) / targetLevel;
        }
        System.out.println("\n------------------------------");
        System.out.println("Skill: " + skill.getSkillName());
        System.out.println("Skill ID: " + skill.getSkillId());
        System.out.println("Current Level: " + currentLevel);
        System.out.println("Target Level: " + targetLevel);
        System.out.printf("Progress: %.2f%%%n", progress);
        System.out.println("Remaining Gap: " + remainingGap);
        if (currentLevel >= targetLevel) {
            System.out.println("Status: TARGET ACHIEVED");
        } else {
            System.out.println("Status: IN PROGRESS");
        }
    }
        System.out.println("------------------------------");
    }
private void createLearningGoal() throws SQLException {
    System.out.println("\n--- Create Learning Goal ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    System.out.print("Enter goal title: ");
    String title = scanner.nextLine().trim();
    System.out.print("Enter description (optional): ");
    String description = scanner.nextLine().trim();
    if (description.isEmpty()) {
        description = null;
    }
    System.out.print("Enter target date (YYYY-MM-DD, optional): ");
    String dateInput = scanner.nextLine().trim();
    LocalDate targetDate = null;
    if (!dateInput.isEmpty()) {
        try {
            targetDate = LocalDate.parse(dateInput);
        } 
        catch (DateTimeParseException e) {
            System.out.println("Invalid date. Use YYYY-MM-DD, for example 2026-12-31.");
            return;
        }
    }
    long goalId = goalService.addGoal( studentId, title, description, targetDate);
    System.out.println( "Learning goal created successfully! Goal ID: " + goalId);
}
private void viewLearningGoals() throws SQLException {
    System.out.println("\n--- View Learning Goals ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    List<LearningGoal> goals =goalService.getGoalsByStudentId(studentId);
    if (goals.isEmpty()) {
        System.out.println("No learning goals found for Student ID " + studentId);
        return;
    }
    for (LearningGoal goal : goals) {
        System.out.println("\n" + goal);
    }
}
private void runDigitalTwinSimulation() throws SQLException {
    System.out.println("\n--- Digital Twin Simulation ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    System.out.print("Enter scenario name: ");
    String scenarioName = scanner.nextLine().trim();
    System.out.print("Enter duration in weeks (1-104): ");
    int durationWeeks = Integer.parseInt(scanner.nextLine().trim());
    simulationService.simulateLearning(studentId, scenarioName, durationWeeks);
}
private void compareLearningScenarios() throws SQLException {
    System.out.println("\n--- Compare Learning Scenarios ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    List<LearningScenario> scenarios = comparisonService.compareScenarios(studentId);
    if (scenarios.isEmpty()) {
        System.out.println("No saved scenarios found for Student ID " + studentId);
        return;
    }
    System.out.println("\nScenario comparison for Student ID: " + studentId);
    System.out.printf("%-12s %-25s %-12s %-18s%n", "Scenario ID", "Scenario Name", "Weeks", "Estimated Score");
    System.out.println("---------------------------------------------------------------------");
    for (LearningScenario scenario : scenarios) {
        String score = scenario.getEstimatedScore() == null
                ? "Not available"
                : scenario.getEstimatedScore().toPlainString();
        System.out.printf("%-12d %-25s %-12d %-18s%n",
                scenario.getScenarioId(),
                scenario.getScenarioName(),
                scenario.getDurationWeeks(),
                score);
    }
System.out.println("\nNote: Scores are rule-based estimates, not guaranteed outcomes.");
BigDecimal highestScore = null;
int shortestDuration = Integer.MAX_VALUE;
for (LearningScenario scenario : scenarios) {
    BigDecimal score = scenario.getEstimatedScore();
    if (score != null && (highestScore == null || score.compareTo(highestScore) > 0)) {
        highestScore = score;
    }
    if (score != null && score.compareTo(BigDecimal.ZERO) >= 0) {
        shortestDuration = Math.min(shortestDuration, scenario.getDurationWeeks());
    }
}
if (highestScore == null) {
    System.out.println("No estimated scores are available.");
    return;
}
System.out.println("\n--- Comparison Summary ---");
System.out.println("Highest estimated score: " + highestScore);
System.out.println("Scenario(s) with the highest score:");
for (LearningScenario scenario : scenarios) {
    BigDecimal score = scenario.getEstimatedScore();
    if (score != null && score.compareTo(highestScore) == 0) {
        System.out.println("- " + scenario.getScenarioName() + " (" + scenario.getDurationWeeks() + " weeks)");
    }
}
if (shortestDuration != Integer.MAX_VALUE) {
    System.out.println("Shortest duration among scenarios with valid scores: " + shortestDuration + " weeks");
}
}
private void createLearningScenario() throws SQLException {
    System.out.println("\n--- Create Learning Scenario ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    System.out.println("\nChoose a scenario duration:");
    System.out.println("1. Fast Learning (2 weeks)");
    System.out.println("2. Balanced Learning (4 weeks)");
    System.out.println("3. Extended Learning (8 weeks)");
    System.out.println("4. Custom Duration");
    System.out.print("Enter your choice (1-4): ");
    String choice = scanner.nextLine().trim();
    int durationWeeks;
    String scenarioName;
    switch (choice) {
        case "1":
            durationWeeks = 2;
            scenarioName = "Fast Learning";
            break;
        case "2":
            durationWeeks = 4;
            scenarioName = "Balanced Learning";
            break;
        case "3":
            durationWeeks = 8;
            scenarioName = "Extended Learning";
            break;
        case "4":
            System.out.print("Enter duration in weeks (1-104): ");
            durationWeeks = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter scenario name: ");
            scenarioName = scanner.nextLine().trim();
            break;
        default:
            System.out.println("Invalid scenario choice. Select 1-4.");
            return;
    }
    simulationService.simulateLearning(studentId, scenarioName, durationWeeks);
}
private void recordActualProgress() throws SQLException {
    System.out.println("\n--- Record Actual Learning Progress ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    System.out.print("Enter Skill ID (leave blank for general study): ");
    String skillInput = scanner.nextLine().trim();
    Long skillId = skillInput.isEmpty() ? null : Long.parseLong(skillInput);
    System.out.print("Enter completed study hours (0-24): ");
    BigDecimal hours = new BigDecimal(scanner.nextLine().trim());
    System.out.print("Enter skill level after learning (0-100, blank if unknown): ");
    String levelInput = scanner.nextLine().trim();
    Integer level = levelInput.isEmpty() ? null : Integer.parseInt(levelInput);
    System.out.print("Enter notes (optional): ");
    String notes = scanner.nextLine().trim();
    if (notes.isEmpty()) {
        notes = null;
    }
    ProgressRecord record = new ProgressRecord();
    record.setStudentId(studentId);
    record.setSkillId(skillId);
    record.setHoursCompleted(hours);
    record.setSkillLevelAfter(level);
    record.setNotes(notes);
    long progressId = progressService.recordProgress(record);
    System.out.println("Progress recorded successfully!");
    System.out.println("Progress Record ID: " + progressId);
}
private void viewProgressHistory() throws SQLException {
    System.out.println("\n--- View Progress History ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    List<ProgressRecord> records = progressService.getProgressHistory(studentId);
    if (records.isEmpty()) {
        System.out.println("No progress history found for Student ID " + studentId);
        return;
    }
    System.out.println("\nProgress history for Student ID: " + studentId);
    System.out.printf("%-6s %-9s %-12s %-12s %-20s %s%n", "ID", "Skill ID", "Hours", "Level After", "Recorded At", "Notes");
    System.out.println("--------------------------------------------------------------------------");
    for (ProgressRecord record : records) {
        String skill = record.getSkillId() == null ? "General" : record.getSkillId().toString();
        String level = record.getSkillLevelAfter() == null ? "Unknown" : record.getSkillLevelAfter().toString();
        String notes = record.getNotes() == null ? "" : record.getNotes();
        System.out.printf("%-6d %-9s %-12s %-12s %-20s %s%n", record.getProgressId(), skill, record.getHoursCompleted(),level,record.getRecordedAt(),notes);
    }
}
private void viewLearningSummary() {
    System.out.println("\n--- Learning Progress Summary ---");
    try {
        System.out.print("Enter Student ID: ");
        long studentId = Long.parseLong(scanner.nextLine().trim());

        learningSummaryService.displaySummary(studentId);

    } 
    catch (NumberFormatException e) {
        System.out.println("Invalid Student ID. Please enter a number.");
    } 
    catch (java.sql.SQLException e) {
        System.out.println("Could not load learning summary: " + e.getMessage());
    }
}
private void updateLearningGoalStatus() throws SQLException {
    System.out.println("\n--- Update Learning Goal Status ---");
    System.out.print("Enter Student ID: ");
    long studentId = Long.parseLong(scanner.nextLine().trim());
    List<LearningGoal> goals = goalService.getGoalsByStudentId(studentId);
    if (goals.isEmpty()) {
        System.out.println("No learning goals found for Student ID " + studentId);
        return;
    }
    System.out.println("\n--- Available Learning Goals ---");
    for (LearningGoal goal : goals) {
        System.out.println( "Goal ID: " + goal.getGoalId() + " | Title: " + goal.getGoalTitle() + " | Status: " + goal.getStatus());
    }
    System.out.print("\nEnter Goal ID to update: ");
    long goalId = Long.parseLong(scanner.nextLine().trim());
    System.out.println("\nChoose new status:");
    System.out.println("1. PENDING");
    System.out.println("2. IN_PROGRESS");
    System.out.println("3. COMPLETED");
    System.out.print("Enter choice (1-3): ");
    String choice = scanner.nextLine().trim();
    String status;
    switch (choice) {
        case "1":
            status = "PENDING";
            break;
        case "2":
            status = "IN_PROGRESS";
            break;
        case "3":
            status = "COMPLETED";
            break;
        default:
            System.out.println("Invalid status choice.");
            return;
    }
    boolean updated = goalService.updateGoalStatus(goalId, studentId, status);
    if (updated){
        System.out.println("Learning goal status updated to " + status + " successfully!");
    } 
    else {
        System.out.println("Goal not found for this student. Please check the IDs.");
    }
}
}