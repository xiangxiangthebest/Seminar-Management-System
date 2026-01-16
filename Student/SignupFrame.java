package Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SignupFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private static final String STUDENT_FILE = "data/students.txt";

    public SignupFrame() {
        setTitle("Student Sign Up");
        setSize(420, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton signupBtn = new JButton("Sign Up");
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(signupBtn, gbc);

        signupBtn.addActionListener(e -> signup());

        add(panel);
        setVisible(true);
    }

    private void signup() {
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }

        List<String[]> students = FileUtil.readCSV(STUDENT_FILE);

        for (String[] s : students) {
            if (s.length > 1 && s[1].equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists");
                return;
            }
        }

        String studentId = generateNextStudentId(students);

        String record = studentId + "," + username + "," + password + ",,,,";

        FileUtil.writeLine(STUDENT_FILE, record);

        JOptionPane.showMessageDialog(
            this,
            "Sign up successful!\nYour Student ID is " + studentId
        );

        new LoginFrame();
        dispose();
    }

    private String generateNextStudentId(List<String[]> students) {
        int max = 0;
        for (String[] s : students) {
            if (s.length > 0 && s[0].startsWith("STU")) {
                try {
                    int num = Integer.parseInt(s[0].substring(3));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("STU%03d", max + 1);
    }
}
