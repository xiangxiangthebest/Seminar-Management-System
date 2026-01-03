import database.Database;

public class main {
    public static void main(String[] args) {
        Database.init();               // connect to DB
        Database.createStudentTable(); // create table
        Database.saveStudent("242UC244GC"); // insert sample student
        Database.close();              // close connection
    }
}
