import database.Database;

public class main {
    public static void main(String[] args) {

        Database.init();          // connect
        Database.createTables();  // create tables
        Database.insertSampleData(); // insert data
        Database.close();         // close database

    }
}
