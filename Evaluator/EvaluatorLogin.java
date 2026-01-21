package Evaluator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class EvaluatorLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMessage;

    public EvaluatorLogin() {

        setTitle("Evaluator Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); 

        
        JPanel userPanel = new JPanel(new BorderLayout(5, 5));
        JLabel lblUser = new JLabel("Username:");
        txtUsername = new JTextField();
        userPanel.add(lblUser, BorderLayout.WEST);
        userPanel.add(txtUsername, BorderLayout.CENTER);
        mainPanel.add(userPanel);
        mainPanel.add(Box.createVerticalStrut(15)); 

        
        JPanel passPanel = new JPanel(new BorderLayout(5, 5));
        JLabel lblPass = new JLabel("Password:");
        txtPassword = new JPasswordField();
        passPanel.add(lblPass, BorderLayout.WEST);
        passPanel.add(txtPassword, BorderLayout.CENTER);
        mainPanel.add(passPanel);
        mainPanel.add(Box.createVerticalStrut(20));

       
        btnLogin = new JButton("Login");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(btnLogin);

        mainPanel.add(Box.createVerticalStrut(15)); 

        
        lblMessage = new JLabel("", SwingConstants.CENTER);
        lblMessage.setForeground(Color.RED);
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblMessage);

        add(mainPanel);

        
        btnLogin.addActionListener(e -> login());
    }

    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (password.equals("1234") && evaluatorExists(username)) {

            JOptionPane.showMessageDialog(this,
                    "Login Successful!\nWelcome " + username);

           
            new EvaluatorDashboard(username).setVisible(true);
            this.dispose();

        } else {
            lblMessage.setText("Invalid username or password");
        }
    }

    
    private boolean evaluatorExists(String username) {
    File file = new File("data/evaluators.txt");

    if (!file.exists()) {
        JOptionPane.showMessageDialog(this,
                "evaluators.txt not found!\nExpected at: " + file.getAbsolutePath(),
                "File Error",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().equalsIgnoreCase(username.trim())) {
                return true;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return false;
}


    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EvaluatorLogin().setVisible(true));
    }
}