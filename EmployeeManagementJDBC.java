import java.sql.*;
import java.util.Scanner;

public class EmployeeManagementJDBC {
    private static final String URL = "jdbc:mysql://localhost:3306/codomax_db";
    private static final String USER = "root";
    private static final String PASSWORD = "your_mysql_password";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Add mysql-connector-j JAR to classpath.");
            return;
        }

        while (true) {
            System.out.println("\n=== Employee Management System (Java + JDBC) ===");
            System.out.println("1. Add Employee (INSERT)");
            System.out.println("2. View All Employees (SELECT)");
            System.out.println("3. Update Employee Salary (UPDATE)");
            System.out.println("4. Delete Employee (DELETE)");
            System.out.println("5. Batch Salary Bonus with Transaction (COMMIT/ROLLBACK)");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    insertEmployee();
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    updateSalary();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    applyDepartmentBonusTransaction();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Choose 1-6.");
            }
        }
    }

    private static void insertEmployee() {
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Department: ");
        String dept = scanner.nextLine().trim();
        System.out.print("Enter Salary: ");
        double salary = Double.parseDouble(scanner.nextLine().trim());

        String sql = "INSERT INTO employees (full_name, department, salary) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, dept);
            pstmt.setDouble(3, salary);

            int rows = pstmt.executeUpdate();
            System.out.println(rows + " employee record inserted.");
        } catch (SQLException e) {
            System.out.println("Database Error (INSERT): " + e.getMessage());
        }
    }

    private static void viewEmployees() {
        String sql = "SELECT emp_id, full_name, department, salary FROM employees ORDER BY emp_id";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- Employees Table ---");
            boolean hasRows = false;
            while (rs.next()) {
                hasRows = true;
                System.out.printf("ID: %d | Name: %-18s | Dept: %-14s | Salary: %.2f%n",
                        rs.getInt("emp_id"),
                        rs.getString("full_name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
            if (!hasRows) {
                System.out.println("No records found.");
            }
        } catch (SQLException e) {
            System.out.println("Database Error (SELECT): " + e.getMessage());
        }
    }

    private static void updateSalary() {
        System.out.print("Enter Employee ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter New Salary: ");
        double salary = Double.parseDouble(scanner.nextLine().trim());

        String sql = "UPDATE employees SET salary = ? WHERE emp_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, salary);
            pstmt.setInt(2, id);

            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Salary updated successfully." : "Employee ID not found.");
        } catch (SQLException e) {
            System.out.println("Database Error (UPDATE): " + e.getMessage());
        }
    }

    private static void deleteEmployee() {
        System.out.print("Enter Employee ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());

        String sql = "DELETE FROM employees WHERE emp_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0 ? "Employee deleted." : "Employee ID not found.");
        } catch (SQLException e) {
            System.out.println("Database Error (DELETE): " + e.getMessage());
        }
    }

    private static void applyDepartmentBonusTransaction() {
        System.out.print("Enter Department for bonus: ");
        String dept = scanner.nextLine().trim();
        System.out.print("Enter Bonus Amount to add: ");
        double bonus = Double.parseDouble(scanner.nextLine().trim());

        String sql = "UPDATE employees SET salary = salary + ? WHERE department = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, bonus);
                pstmt.setString(2, dept);
                int updated = pstmt.executeUpdate();
                conn.commit();
                System.out.println("Transaction committed! Updated " + updated + " record(s).");
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Transaction rolled back due to error: " + ex.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }
}
