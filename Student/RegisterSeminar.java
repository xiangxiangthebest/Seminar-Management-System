package Student;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class RegisterSeminar extends JFrame {

    private static final String STUDENT_FILE = "data/students.txt";

    public RegisterSeminar(String studentId) {
        setTitle("Seminar Registration");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel for form
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Components
        JLabel lblTitle = new JLabel("Research Title:");
        JTextField title = new JTextField();

        JLabel lblAbs = new JLabel("Abstract:");
        JTextArea abs = new JTextArea(4, 20);
        abs.setLineWrap(true);
        abs.setWrapStyleWord(true);
        JScrollPane absScroll = new JScrollPane(abs);

        JLabel lblSupervisor = new JLabel("Supervisor:");
        JTextField supervisor = new JTextField();

        JRadioButton oral = new JRadioButton("Oral");
        JRadioButton poster = new JRadioButton("Poster");
        ButtonGroup group = new ButtonGroup();
        group.add(oral);
        group.add(poster);

        JButton submit = new JButton("Submit");

        // Submit action
        submit.addActionListener(e -> {
            String type = oral.isSelected() ? "Oral" : poster.isSelected() ? "Poster" : "";

            if (title.getText().isEmpty() || abs.getText().isEmpty() || supervisor.getText().isEmpty() || type.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields and select type");
                return;
            }

            List<String[]> students = FileUtil.readCSV(STUDENT_FILE);
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i)[0].equals(studentId)) {
                    students.get(i)[3] = title.getText();
                    students.get(i)[4] = abs.getText();
                    students.get(i)[5] = supervisor.getText();
                    students.get(i)[6] = type;
                    break;
                }
            }

            List<String> updated = new java.util.ArrayList<>();
            for (String[] s : students) updated.add(String.join(",", s));
            FileUtil.writeAll(STUDENT_FILE, updated);

            JOptionPane.showMessageDialog(this, "Registration updated");
            dispose();
        });

        // Layout using GridBag
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblTitle, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(title, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblAbs, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(absScroll, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblSupervisor, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(supervisor, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(oral, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(poster, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panel.add(submit, gbc);

        add(panel);
        setVisible(true);
    }
}
