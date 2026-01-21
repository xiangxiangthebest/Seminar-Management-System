// Main.java
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Main extends JFrame {

    public Main() {
        setTitle("Seminar Management System - Login");
        setSize(400, 200);
        setLocationRelativeTo(null); // center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Select your role to login:");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // span all buttons
        panel.add(label, gbc);

        gbc.gridwidth = 1; // reset

        // Coordinator Button
        JButton coordinatorBtn = new JButton("Coordinator");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(coordinatorBtn, gbc);

        // Student Button
        JButton studentBtn = new JButton("Student");
        gbc.gridx = 1;
        panel.add(studentBtn, gbc);

        // Evaluator Button
        JButton evaluatorBtn = new JButton("Evaluator");
        gbc.gridx = 2;
        panel.add(evaluatorBtn, gbc);

        // Action for Coordinator Button
        coordinatorBtn.addActionListener((ActionEvent e) -> {
            // Open Coordinator.LoginFrame
            new Coordinator.LoginFrame(); // make sure LoginFrame constructor shows the login screen
            dispose(); // close main login frame
        });

        // Actions for Student and Evaluator
        studentBtn.addActionListener(e -> {
            new Student.LoginFrame();
            dispose();
        });

        evaluatorBtn.addActionListener(e -> {
            new Evaluator.EvaluatorLogin().setVisible(true);
             dispose();
        });


        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
