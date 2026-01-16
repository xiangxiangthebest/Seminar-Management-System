package Student;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {

    private JTextField username;
    private JPasswordField password;

    private static final String STUDENT_FILE = "data/students.txt";

    public LoginFrame() {
        setTitle("Seminar Management System - Login");
        setSize(400, 260); // slightly taller for Sign Up button
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

        // 🔹 NEW: Sign Up button
        JButton signupBtn = new JButton("Sign Up");
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(signupBtn, gbc);

        loginBtn.addActionListener(e -> login());

        // 🔹 NEW: Sign Up action
        signupBtn.addActionListener(e -> {
            new SignupFrame();
            dispose();
        });

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

        List<String[]> students = FileUtil.readCSV(STUDENT_FILE);

        for (String[] s : students) {
            if (s.length >= 3) {
                String username = s[1].trim();
                String password = s[2].trim();

                if (username.equalsIgnoreCase(nameInput) && password.equals(pwdInput)) {
                    new StudentDashboard(s[0], s[1]);
                    dispose();
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Invalid username or password");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
