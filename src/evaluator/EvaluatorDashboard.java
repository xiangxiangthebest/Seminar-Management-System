package evaluator;

import javax.swing.*;

public class EvaluatorDashboard extends JFrame {

    String evaluatorName;

    public EvaluatorDashboard(String name) {

        this.evaluatorName = name;

        setTitle("Evaluator Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== MENU BAR =====
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem viewStudents = new JMenuItem("View My Students");
        JMenuItem viewSessions = new JMenuItem("View My Sessions");
        JMenuItem viewSubmissions = new JMenuItem("View Submissions");
        JMenuItem evaluateStudents = new JMenuItem("Evaluate Students");

        viewStudents.addActionListener(e ->
                new MyStudentsView(evaluatorName).setVisible(true));

        viewSessions.addActionListener(e ->
                new MySessionsView(evaluatorName).setVisible(true));

        viewSubmissions.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "View Submissions - Coming Soon"));

               evaluateStudents.addActionListener(e ->
        new EvaluateSessionView(evaluatorName).setVisible(true));


        menu.add(viewStudents);
        menu.add(viewSessions);
        menu.add(viewSubmissions);
        menu.add(evaluateStudents);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        JLabel lblWelcome = new JLabel(
                "Logged in as: " + evaluatorName,
                SwingConstants.CENTER);

        add(lblWelcome);
    }
}
