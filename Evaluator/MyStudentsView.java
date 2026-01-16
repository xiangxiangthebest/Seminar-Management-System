package Evaluator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyStudentsView extends JFrame {

    private String evaluatorName;
    private JTable table;
    private DefaultTableModel tableModel;

    public MyStudentsView(String evaluatorName) {
        this.evaluatorName = evaluatorName;

        setTitle("My Students - " + evaluatorName);
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Table Columns 
        String[] columns = {
                "Student ID",
                "Student Name",
                "Project Title",
                "Presentation Type",
                "Submission File"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadStudents();
    }

    // Load students 
    private void loadStudents() {

        try (BufferedReader br = new BufferedReader(
                new FileReader("data/students.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {

                // Split CSV 
                String[] data = line.split(",");

                

                if (data.length < 7) continue;

                String studentId = data[0].trim();
                String name = data[1].trim();
                String title = data[2].trim();
                String supervisor = data[4].trim();
                String type = data[5].trim();
                String file = data[6].trim();

                
                if (supervisor.equalsIgnoreCase(evaluatorName)) {
                    tableModel.addRow(new Object[]{
                            studentId,
                            name,
                            title,
                            type,
                            file
                    });
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading students file",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}