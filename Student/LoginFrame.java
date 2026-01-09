package Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {

    private JTextField username;
    private JPasswordField password;

    private static final String STUDENT_FILE = "data/students.txt";
    private static final String FIXED_PASSWORD = "1234";

    public LoginFrame() {
        setTitle("Seminar Management System - Login");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        username = new JTextField(15);
        password = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username"), gbc);
        gbc.gridx = 1;
        panel.add(username, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        panel.add(password, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> login());

        add(panel);
        setVisible(true);
    }

    private void login() {
        String nameInput = username.getText().trim();
        String pwdInput = String.valueOf(password.getPassword());

        if (nameInput.isEmpty() || pwdInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }

        if (!pwdInput.equals(FIXED_PASSWORD)) {
            JOptionPane.showMessageDialog(this, "Invalid password");
            return;
        }

        // ---------------- Find student in students.txt ----------------
        List<String[]> students = FileUtil.readCSV(STUDENT_FILE);
        String studentId = null;
        String studentName = null;

        for (String[] s : students) {
            if (s.length > 1 && s[1].trim().equalsIgnoreCase(nameInput)) {
                studentId = s[0].trim();
                studentName = s[1].trim();
                break;
            }
        }

        if (studentId != null && studentName != null) {
            // Pass both studentId and studentName to Dashboard
            new StudentDashboard(studentId, studentName);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Student name not found");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
