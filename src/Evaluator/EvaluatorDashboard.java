package Evaluator;

import javax.swing.*;
import java.awt.*;

public class EvaluatorDashboard extends JFrame {

    String evaluatorName;

    public EvaluatorDashboard(String name) {

        this.evaluatorName = name;

        setTitle("Evaluator Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        JLabel lblWelcome = new JLabel(
                "Logged in as: " + evaluatorName,
                SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        add(lblWelcome, BorderLayout.NORTH);

        // MENU PANEL 
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton btnViewStudents = new JButton("View My Students");
        JButton btnViewSessions = new JButton("View My Sessions");
        JButton btnViewSubmissions = new JButton("View Submissions");
        JButton btnEvaluateStudents = new JButton("Evaluate Students");

        Dimension buttonSize = new Dimension(250, 40);

        JButton[] buttons = {
                btnViewStudents,
                btnViewSessions,
                btnViewSubmissions,
                btnEvaluateStudents
        };

        for (JButton btn : buttons) {
            btn.setMaximumSize(buttonSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuPanel.add(btn);
            menuPanel.add(Box.createVerticalStrut(15));
        }

        add(menuPanel, BorderLayout.CENTER);

       
        btnViewStudents.addActionListener(e ->
                new MyStudentsView(evaluatorName).setVisible(true));

        btnViewSessions.addActionListener(e ->
                new MySessionsView(evaluatorName).setVisible(true));

        btnViewSubmissions.addActionListener(e ->
                new ViewSubmissionView(evaluatorName).setVisible(true));

        btnEvaluateStudents.addActionListener(e ->
                new EvaluateSessionView(evaluatorName).setVisible(true));
    }
}