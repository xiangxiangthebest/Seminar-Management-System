package Student;

import java.awt.*;
import javax.swing.*;

public class StudentDashboard extends JFrame {

    public StudentDashboard(String studentId, String studentName) {
        setTitle("Student Dashboard - " + studentName);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnRegister = new JButton("Register Seminar");
        btnRegister.addActionListener(e -> new RegisterSeminar(studentId));

        JButton btnUpload = new JButton("Upload Presentation Material");
        btnUpload.addActionListener(e -> new UploadPresentation(studentId));

        JButton btnStatus = new JButton("View Submission Status");
        btnStatus.addActionListener(e -> new ViewSubmissionStatus(studentId));

        JButton btnNotification = new JButton("Notification");
        btnNotification.addActionListener(e -> new Notification(studentName));

        panel.add(btnRegister);
        panel.add(btnUpload);
        panel.add(btnStatus);
        panel.add(btnNotification);

        add(panel);
        setVisible(true);
    }
}
