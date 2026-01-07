package Coordinator;
import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private JTextField username;
    private JPasswordField password;

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
        if (username.getText().equals("coordinator") &&
            String.valueOf(password.getPassword()).equals("1234")) {
            new CoordinatorDashboard();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid login");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                "javax.swing.plaf.nimbus.NimbusLookAndFeel"
            );
        } catch (Exception ignored) {}

        new LoginFrame();
    }
}
