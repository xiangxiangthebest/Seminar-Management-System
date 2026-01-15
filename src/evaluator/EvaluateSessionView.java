package evaluator;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class EvaluateSessionView extends JFrame {

    String evaluatorName;
    String selectedStudent;

    JComboBox<String> sessionDropdown;

    JLabel lblStudent, lblTopic, lblType, lblVenue, lblTime;

    JTextField[] markFields = new JTextField[4];
    JTextArea overallCommentArea;

    Map<String, String[]> sessionData = new HashMap<>();

    public EvaluateSessionView(String evaluatorName) {

        this.evaluatorName = evaluatorName;

        setTitle("Evaluate Session - " + evaluatorName);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

       
        JPanel topPanel = new JPanel(new FlowLayout());

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

        centerPanel.add(Box.createVerticalStrut(10));

        // RUBRICS 
        String[] rubrics = {
                "Rubric 1: Problem Clarity",
                "Rubric 2: Methodology",
                "Rubric 3: Results",
                "Rubric 4: Presentation"
        };

        for (int i = 0; i < 4; i++) {
            JPanel rubricPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rubricPanel.setBorder(BorderFactory.createTitledBorder(rubrics[i]));

            rubricPanel.add(new JLabel("Marks:"));
            markFields[i] = new JTextField(5);
            rubricPanel.add(markFields[i]);

            centerPanel.add(rubricPanel);
        }

        //  COMMENT 
        JPanel commentPanel = new JPanel(new BorderLayout());
        commentPanel.setBorder(BorderFactory.createTitledBorder("Overall Comment"));

        overallCommentArea = new JTextArea(4, 50);
        commentPanel.add(new JScrollPane(overallCommentArea));

        centerPanel.add(commentPanel);

        
        JButton submitBtn = new JButton("Submit Evaluation");
        submitBtn.addActionListener(e -> submitEvaluation());

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(submitBtn);

        add(new JScrollPane(centerPanel), BorderLayout.CENTER);
    }

    
    private void loadSessions() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/sessions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String sessionId = data[0].trim();
                String datetime = data[2].trim();
                String venue = data[3].trim();
                String type = data[4].trim();
                String student = data[5].trim();
                String evaluator = data[6].trim();

                if (evaluator.equalsIgnoreCase(evaluatorName)) {
                    sessionDropdown.addItem(sessionId);
                    sessionData.put(sessionId,
                            new String[]{student, type, venue, datetime});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading sessions.");
        }
    }

   
    private void updateStudentInfo() {
        String sessionId = (String) sessionDropdown.getSelectedItem();
        if (sessionId == null) return;

        String[] info = sessionData.get(sessionId);

        selectedStudent = info[0];
        lblStudent.setText("Student: " + selectedStudent);
        lblType.setText("Presentation Type: " + info[1]);
        lblVenue.setText("Venue: " + info[2]);
        lblTime.setText("Date & Time: " + info[3]);

        loadStudentTopic(selectedStudent);
    }

    private void loadStudentTopic(String studentName) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equalsIgnoreCase(studentName)) {
                    lblTopic.setText("Topic: " + data[1].trim());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void submitEvaluation() {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("data/evaluations.txt", true))) {

            bw.write(selectedStudent + "," + evaluatorName);

            for (int i = 0; i < 4; i++) {
                bw.write("," + markFields[i].getText().trim());
            }

            bw.write("," + overallCommentArea.getText().trim());
            bw.newLine();

            JOptionPane.showMessageDialog(this, "Evaluation submitted successfully!");
            dispose();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving evaluation.");
        }
    }
}
