package evaluator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class MyStudentsView extends JFrame {

    String evaluatorName;
    JTable table;
    DefaultTableModel model;

    public MyStudentsView(String evaluatorName) {

        this.evaluatorName = evaluatorName;

        setTitle("My Students - " + evaluatorName);
        setSize(900, 300);
        setLocationRelativeTo(null);

        // Table columns
        String[] columns = {
                "Student Name",
                "Project Title",
                "Description",
                "Presentation Type",
                "File"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        loadStudents();

        add(new JScrollPane(table));
    }

    private void loadStudents() {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("data/students.txt"));

            String line;
            while ((line = br.readLine()) != null) {

                
                String[] data = line.split(
                        ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String studentName = data[0].trim();
                String title = data[1].trim();
                String description = data[2].replace("\"", "").trim();
                String supervisor = data[3].replace("\"", "").trim();
                String type = data[4].trim();
                String file = data[5].trim();

                
                if (supervisor.equalsIgnoreCase(evaluatorName)) {

                    model.addRow(new Object[]{
                            studentName,
                            title,
                            description,
                            type,
                            file
                    });
                }
            }

            br.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading students.txt");
            e.printStackTrace();
        }
    }
}
