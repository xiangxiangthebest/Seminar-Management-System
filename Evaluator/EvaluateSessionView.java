package Evaluator;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class EvaluateSessionView extends JFrame {

    private String evaluatorName;
    private String selectedStudent;

    private JComboBox<String> sessionDropdown;

    private JLabel lblStudent, lblTopic, lblType, lblVenue, lblTime;

    private JTextField[] markFields = new JTextField[4];
    private JTextArea overallCommentArea;

   
    private Map<String, String[]> sessionData = new HashMap<>();

    public EvaluateSessionView(String evaluatorName) {
        this.evaluatorName = evaluatorName;

        setTitle("Evaluate Session - " + evaluatorName);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

       
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Session ID:"));

        sessionDropdown = new JComboBox<>();
        loadSessions();
        sessionDropdown.addActionListener(e -> updateStudentInfo());
        topPanel.add(sessionDropdown);

        add(topPanel, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        lblStudent = new JLabel("Student: ");
        lblTopic = new JLabel("Topic: ");
        lblType = new JLabel("Presentation Type: ");
        lblVenue = new JLabel("Venue: ");
        lblTime = new JLabel("Date & Time: ");

        centerPanel.add(lblStudent);
        centerPanel.add(lblTopic);
        centerPanel.add(lblType);
        centerPanel.add(lblVenue);
        centerPanel.add(lblTime);

        centerPanel.add(Box.createVerticalStrut(15));

        
        String[] rubrics = {
                "Rubric 1: Problem Clarity",
                "Rubric 2: Methodology",
                "Rubric 3: Results",
                "Rubric 4: Presentation"
        };

        for (int i = 0; i < rubrics.length; i++) {
            JPanel rubricPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rubricPanel.setBorder(BorderFactory.createTitledBorder(rubrics[i]));

            rubricPanel.add(new JLabel("Marks (0-20):"));
            markFields[i] = new JTextField(5);
            rubricPanel.add(markFields[i]);

            centerPanel.add(rubricPanel);
        }

       
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentPanel.setBorder(BorderFactory.createTitledBorder("Overall Comment"));
        overallCommentArea = new JTextArea(4, 50);
        commentPanel.add(new JScrollPane(overallCommentArea));
        centerPanel.add(commentPanel);

        centerPanel.add(Box.createVerticalStrut(20));

        
        JButton submitBtn = new JButton("Submit Evaluation");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(submitBtn);
        submitBtn.addActionListener(e -> submitEvaluation());
        centerPanel.add(btnPanel);

        add(new JScrollPane(centerPanel), BorderLayout.CENTER);
    }

    
    private void loadSessions() {
        File file = new File("data/sessions.txt");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "sessions.txt not found!", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 7) continue;

                String sessionId = data[0].trim();
                String datetime = data[2].trim();
                String venue = data[3].trim();
                String type = data[4].trim();
                String studentName = data[5].trim();
                String evaluator = data[6].trim();

                if (evaluator.equalsIgnoreCase(evaluatorName)) {
                    sessionDropdown.addItem(sessionId);
                    sessionData.put(sessionId, new String[]{studentName, type, venue, datetime});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading sessions.txt");
        }
    }

    
    private void updateStudentInfo() {
        String sessionId = (String) sessionDropdown.getSelectedItem();
        if (sessionId == null) return;

        String[] info = sessionData.get(sessionId);
        if (info == null) return;

        selectedStudent = info[0];

        lblStudent.setText("Student: " + selectedStudent);
        lblType.setText("Presentation Type: " + info[1]);
        lblVenue.setText("Venue: " + info[2]);
        lblTime.setText("Date & Time: " + info[3]);

        loadStudentTopic(selectedStudent);
    }

    
    private void loadStudentTopic(String studentName) {
        File file = new File("data/students.txt");
        if (!file.exists()) {
            lblTopic.setText("Topic: Not found");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = splitCSV(line);
                
                if (data.length > 3 && data[1].trim().equalsIgnoreCase(studentName.trim())) {
                    lblTopic.setText("Topic: " + data[3].trim());
                    return;
                }
            }
            lblTopic.setText("Topic: Not found");
        } catch (IOException e) {
            e.printStackTrace();
            lblTopic.setText("Topic: Not found");
        }
    }

    
    private void submitEvaluation() {
        try {
            // Validate that a student is selected
            if (selectedStudent == null || selectedStudent.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a session first.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(selectedStudent).append(",").append(evaluatorName);

            
            for (int i = 0; i < markFields.length; i++) {
                String markStr = markFields[i].getText().trim();
                int mark;
                try {
                    mark = Integer.parseInt(markStr);
                    if (mark < 0 || mark > 20) {
                        JOptionPane.showMessageDialog(this,
                                "Rubric " + (i + 1) + " marks must be between 0 and 20.",
                                "Invalid Input",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Rubric " + (i + 1) + " marks must be a number.",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                sb.append(",").append(mark);
            }

            sb.append(",").append(overallCommentArea.getText().trim());

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/evaluations.txt", true))) {
                bw.write(sb.toString());
                bw.newLine();
                bw.flush();
            }

            JOptionPane.showMessageDialog(this, 
                    "Evaluation submitted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Clear fields for next evaluation
            for (JTextField field : markFields) {
                field.setText("");
            }
            overallCommentArea.setText("");
            
            // Reload sessions dropdown
            sessionDropdown.removeAllItems();
            sessionData.clear();
            loadSessions();
            
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error saving evaluation: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private String[] splitCSV(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}