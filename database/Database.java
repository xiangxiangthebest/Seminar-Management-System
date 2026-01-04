package database;

import java.sql.*;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:database.db";
    private static Connection connection;

    // ==============================
    // DATABASE CONNECTION
    // ==============================
    public static void init() {
        try {
            Class.forName("org.sqlite.JDBC"); // load driver
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
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

    // CREATE TABLES
    public static void createTables() {
        try (Statement stmt = getConnection().createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS student (
                    student_id TEXT PRIMARY KEY,
                    research_title TEXT,
                    abstract TEXT,
                    supervisor_name TEXT,
                    presentation_type TEXT,
                    file_path TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS evaluator (
                    evaluator_id TEXT PRIMARY KEY,
                    expertise_area TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS coordinator (
                    coordinator_id TEXT PRIMARY KEY
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS user (
                    user_id TEXT PRIMARY KEY,
                    username TEXT,
                    email TEXT,
                    password TEXT,
                    role TEXT
                );
            """);

            System.out.println("All tables created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // INSERT DATA
    public static void insertSampleData() {
        try {

            // STUDENTS
            PreparedStatement psStudent = getConnection().prepareStatement(
                "INSERT OR IGNORE INTO student VALUES (?, ?, ?, ?, ?, ?)"
            );

            String[][] students = {
                {"S001","AI Healthcare","AI diagnosis","Dr Lim","Poster","files/s1.pdf"},
                {"S002","Blockchain Voting","Secure voting","Dr Tan","Oral","files/s2.pdf"},
                {"S003","Smart Home IoT","Automation","Dr Wong","Poster","files/s3.pdf"},
                {"S004","Cyber Threats","Risk analysis","Dr Lee","Oral","files/s4.pdf"},
                {"S005","Big Data Mining","Pattern discovery","Dr Chan","Poster","files/s5.pdf"},
                {"S006","Cloud Systems","Scalability","Dr Ong","Oral","files/s6.pdf"}
            };

            for (String[] s : students) {
                for (int i = 0; i < s.length; i++) {
                    psStudent.setString(i + 1, s[i]);
                }
                psStudent.executeUpdate();
            }

            // EVALUATORS
            PreparedStatement psEval = getConnection().prepareStatement(
                "INSERT OR IGNORE INTO evaluator VALUES (?, ?)"
            );

            String[][] evaluators = {
                {"E001","Artificial Intelligence"},
                {"E002","Blockchain"},
                {"E003","Cybersecurity"},
                {"E004","Data Science"},
                {"E005","Cloud Computing"},
                {"E006","IoT"}
            };

            for (String[] e : evaluators) {
                psEval.setString(1, e[0]);
                psEval.setString(2, e[1]);
                psEval.executeUpdate();
            }

            // COORDINATORS
            PreparedStatement psCoord = getConnection().prepareStatement(
                "INSERT OR IGNORE INTO coordinator VALUES (?)"
            );

            String[] coords = {"C001","C002","C003"};
            for (String c : coords) {
                psCoord.setString(1, c);
                psCoord.executeUpdate();
            }

            // USERS
            PreparedStatement psUser = getConnection().prepareStatement(
                "INSERT OR IGNORE INTO user VALUES (?, ?, ?, ?, ?)"
            );

            String[][] users = {
                
                // STUDENTS
                {"S001","alice","alice@mail.com","1234","student"},
                {"S002","bob","bob@mail.com","1234","student"},
                {"S003","charlie","charlie@mail.com","1234","student"},
                {"S004","david","david@mail.com","1234","student"},
                {"S005","eva","eva@mail.com","1234","student"},
                {"S006","frank","frank@mail.com","1234","student"},

                // EVALUATORS
                {"E001","drtan","tan@mail.com","1234","evaluator"},
                {"E002","drlee","lee@mail.com","1234","evaluator"},
                {"E003","drlim","lim@mail.com","1234","evaluator"},
                {"E004","drliu","liu@mail.com","1234","evaluator"},
                {"E005","drgoh","goh@mail.com","1234","evaluator"},
                {"E006","drlam","lam@mail.com","1234","evaluator"},

                // COORDINATORS
                {"C001","admin1","admin@mail.com","admin","coordinator"},
                {"C002","admin2","admin2@mail.com","admin","coordinator"},
                {"C003","admin3","admin3@mail.com","admin","coordinator"}
            };


            for (String[] u : users) {
                for (int i = 0; i < u.length; i++) {
                    psUser.setString(i + 1, u[i]);
                }
                psUser.executeUpdate();
            }

            System.out.println("Sample data inserted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
