package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static Connection connection;

    public static void init() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load SQLite JDBC driver
                Class.forName("org.sqlite.JDBC");

                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Database connected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            init();
        }
        return connection;
    }

    public static void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create table
    public static void createStudentTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS student (
                student_id TEXT PRIMARY KEY
            );
        """;

        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Student table ready");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveStudent(String studentId) {
        String sql = "INSERT INTO student(student_id) VALUES(?)";

        try (PreparedStatement pstmt =
                     getConnection().prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
            System.out.println("Student saved: " + studentId);

        } catch (SQLException e) {
            System.out.println("Student already exists or error occurred");
        }
    }
}
