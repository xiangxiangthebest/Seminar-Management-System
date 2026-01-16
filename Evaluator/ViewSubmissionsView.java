package Evaluator;

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
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // TABLE
        model = new DefaultTableModel(
                new String[]{"Session ID", "Student Name", "Type", "Submission File"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        
        loadSubmissions();

       
        JButton viewBtn = new JButton("View Submission Info");
        viewBtn.addActionListener(e -> viewSubmission());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(viewBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //LOAD SUBMISSIONS
    private void loadSubmissions() {

        
        Map<String, String> sessionStudentMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(
                new FileReader("data/sessions.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 7) continue;

                String sessionId = data[0].trim();
                String studentName = data[5].trim();
                String evaluator = data[6].trim();

                if (evaluator.equalsIgnoreCase(evaluatorName)) {
                    sessionStudentMap.put(studentName, sessionId);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading sessions.txt");
            return;
        }

        
        try (BufferedReader br = new BufferedReader(
                new FileReader("data/students.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] data = splitCSV(line);
                if (data.length < 7) continue;

             

                String studentName = data[1].trim();
                String type = data[5].trim();
                String file = data[6].trim();

                if (sessionStudentMap.containsKey(studentName)) {

                    String sessionId = sessionStudentMap.get(studentName);
                    String key = sessionId + "|" + studentName;

                    model.addRow(new Object[]{
                            sessionId,
                            studentName,
                            type,
                            file
                    });

                    submissionMap.put(key, file);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading students.txt");
        }
    }

    // VIEW SUBMISSION 
    private void viewSubmission() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a submission first.");
            return;
        }

        String sessionId = model.getValueAt(selectedRow, 0).toString();
        String student = model.getValueAt(selectedRow, 1).toString();

        String key = sessionId + "|" + student;
        String file = submissionMap.get(key);

        JOptionPane.showMessageDialog(this,
                "Student: " + student +
                        "\nSession ID: " + sessionId +
                        "\nSubmission File: " + file,
                "Submission Info",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // CSV HANDLER 
    private String[] splitCSV(String line) {

        List<String> result = new ArrayList<>();
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