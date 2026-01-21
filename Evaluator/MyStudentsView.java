package Evaluator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class MyStudentsView extends JFrame {

    private String lecturerName;
    private JTable table;
    private DefaultTableModel model;

    public MyStudentsView(String lecturerName) {
        this.lecturerName = lecturerName;

        setTitle("My Students - " + lecturerName);
        setSize(900, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{"Student ID", "Name", "Topic", "Description", "Presentation Type", "Submission File"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadStudents();
    }

    private void loadStudents() {
        File file = new File("data/students.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "students.txt not found!", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split by commas
                String[] data = splitCSV(line);
                if (data.length < 8) continue;

                String studentID = data[0].trim();
                String name = data[1].trim();
                String supervisor = data[5].trim();
                String topic = data[3].trim();
                String description = data[4].trim();
                String type = data[6].trim();
                String submissionFile = data[7].trim();

                
                if (supervisor.equalsIgnoreCase(lecturerName)) {
                    model.addRow(new Object[]{studentID, name, topic, description, type, submissionFile});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading students.txt");
        }
    }

    
    private String[] splitCSV(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString());
        return result.toArray(new String[0]);
    }
}
