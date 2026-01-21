package Evaluator;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ViewSubmissionView extends JFrame {

    private String evaluatorName;
    private JComboBox<String> sessionDropdown;
    private JLabel lblStudent, lblTopic, lblType, lblVenue, lblTime, lblSubmission;

    
    private Map<String, String[]> sessionData = new HashMap<>();

    public ViewSubmissionView(String evaluatorName) {
        this.evaluatorName = evaluatorName;

        setTitle("View Submissions - " + evaluatorName);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Session ID:"));

        sessionDropdown = new JComboBox<>();
        loadSessions();
        sessionDropdown.addActionListener(e -> updateSubmissionInfo());
        topPanel.add(sessionDropdown);

        add(topPanel, BorderLayout.NORTH);

        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        lblStudent = new JLabel("Student: ");
        lblTopic = new JLabel("Topic: ");
        lblType = new JLabel("Presentation Type: ");
        lblVenue = new JLabel("Venue: ");
        lblTime = new JLabel("Date & Time: ");
        lblSubmission = new JLabel("Submission: ");

        centerPanel.add(lblStudent);
        centerPanel.add(lblTopic);
        centerPanel.add(lblType);
        centerPanel.add(lblVenue);
        centerPanel.add(lblTime);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblSubmission);

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

    
    private void updateSubmissionInfo() {
        String sessionId = (String) sessionDropdown.getSelectedItem();
        if (sessionId == null) return;

        String[] info = sessionData.get(sessionId);
        if (info == null) return;

        String studentName = info[0];

        lblStudent.setText("Student: " + studentName);
        lblType.setText("Presentation Type: " + info[1]);
        lblVenue.setText("Venue: " + info[2]);
        lblTime.setText("Date & Time: " + info[3]);

        
        loadStudentSubmission(studentName);
    }

    private void loadStudentSubmission(String studentName) {
        File file = new File("data/students.txt");
        if (!file.exists()) {
            lblTopic.setText("Topic: Not found");
            lblSubmission.setText("Submission: Not found");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = splitCSV(line);
                if (data.length > 7 && data[1].trim().equalsIgnoreCase(studentName.trim())) {
                    lblTopic.setText("Topic: " + data[3].trim());
                    lblSubmission.setText("Submission: " + data[7].trim());
                    return;
                }
            }

            lblTopic.setText("Topic: Not found");
            lblSubmission.setText("Submission: Not found");

        } catch (IOException e) {
            e.printStackTrace();
            lblTopic.setText("Topic: Not found");
            lblSubmission.setText("Submission: Not found");
        }
    }

    
    private String[] splitCSV(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
