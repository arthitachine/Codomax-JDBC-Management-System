# Java + MySQL Employee Management System (JDBC)

Built for **Module 04: Java Database & JDBC** during the Codomax Digital Solutions Java Development Internship.

## Highlights
- **MySQL Schema & SQL:** Relational `employees` table defined in `schema.sql`.
- **JDBC & PreparedStatements:** Parameterized queries (`INSERT`, `SELECT`, `UPDATE`, `DELETE`) to prevent SQL injection.
- **Transaction Management:** Manual transaction control using `setAutoCommit(false)`, `commit()`, and `rollback()`.

## Setup & Run
1. Run `schema.sql` in MySQL to create `codomax_db` and the `employees` table.
2. Update `URL`, `USER`, and `PASSWORD` in `EmployeeManagementJDBC.java`.
3. Compile and run with the MySQL Connector/J JAR on the classpath:
   ```bash
   javac -cp ".:mysql-connector-j.jar" EmployeeManagementJDBC.java
   java -cp ".:mysql-connector-j.jar" EmployeeManagementJDBC
