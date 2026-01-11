package evaluator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class EvaluatorLogin extends JFrame {

    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin;
    JLabel lblMessage;

    public EvaluatorLogin() {

        // Window title
        setTitle("Evaluator Login");

        // Window size
        setSize(350, 220);

        // Close program when window closes
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center window
        setLocationRelativeTo(null);

        // Layout
        setLayout(new GridLayout(5, 1));

        // Components
        JLabel lblUser = new JLabel("Evaluator Name:");
        txtUsername = new JTextField();

        JLabel lblPass = new JLabel("Password:");
        txtPassword = new JPasswordField();

        btnLogin = new JButton("Login");
        lblMessage = new JLabel("", SwingConstants.CENTER);

        // Add components to window
        add(lblUser);
        add(txtUsername);
        add(lblPass);
        add(txtPassword);
        add(btnLogin);
        add(lblMessage);

        // Button click event
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    // LOGIN LOGIC
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (password.equals("1234") && evaluatorExists(username)) {

            JOptionPane.showMessageDialog(this,
                    "Login Successful!\nWelcome " + username);

            // Open Dashboard and pass evaluator name
            new EvaluatorDashboard(username).setVisible(true);

            // Close login window
            this.dispose();

        } else {
            lblMessage.setText("Invalid username or password");
        }
    }

    // CHECK evaluator.txt
    private boolean evaluatorExists(String username) {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("data/evaluators.txt"));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Comparing [" + line + "] with [" + username + "]");
                if (line.trim().equalsIgnoreCase(username)) {
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // MAIN METHOD (Program starts here)
    public static void main(String[] args) {
        new EvaluatorLogin().setVisible(true);
    }
}
