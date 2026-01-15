package evaluator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.io.*;
import java.util.*;

public class ViewSubmissionsView extends JFrame {

    private String evaluatorName;
    private JTable table;
    private DefaultTableModel model;

   
    private Map<String, String> submissionMap = new HashMap<>();

    public ViewSubmissionsView(String evaluatorName) {

        this.evaluatorName = evaluatorName;

        setTitle("View Submissions - " + evaluatorName);
        setSize(850, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

      
        model = new DefaultTableModel(
                new String[]{"Student", "Topic", "Type", "Submission File"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

       
        loadSubmissions();

        
        JButton viewBtn = new JButton("View Submission Info");
        viewBtn.addActionListener(e -> viewSubmission());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(viewBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    
    private void loadSubmissions() {

        
        Set<String> evaluatorStudents = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/sessions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 7) continue;
                String student = data[5].trim();
                String evaluator = data[6].trim();
                if (evaluator.equalsIgnoreCase(evaluatorName)) {
                    evaluatorStudents.add(student);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading sessions.txt");
            return;
        }

        
        try (BufferedReader br = new BufferedReader(new FileReader("data/students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = splitCSV(line);
                if (data.length < 6) continue;

                String student = data[0].trim();
                String topic = data[1].trim();
                String type = data[4].trim();
                String submissionFile = data[5].trim();

                if (evaluatorStudents.contains(student)) {
                    model.addRow(new Object[]{student, topic, type, submissionFile});
                    submissionMap.put(student, submissionFile);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading students.txt");
        }
    }

    
    private void viewSubmission() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student first.");
            return;
        }

        String student = model.getValueAt(selectedRow, 0).toString();
        String submissionFile = submissionMap.get(student);

        JOptionPane.showMessageDialog(this,
                "Student: " + student +
                        "\nSubmission File: " + submissionFile,
                "Submission Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    //CSV HANDLER
    private String[] splitCSV(String line) {
        java.util.List<String> result = new ArrayList<>();
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
