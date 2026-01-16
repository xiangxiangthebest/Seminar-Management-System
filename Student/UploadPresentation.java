package Student;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.awt.*;

public class UploadPresentation extends JFrame {

    private static final String STUDENT_FILE = "data/students.txt";

    public UploadPresentation(String studentId) {
        setTitle("Upload Presentation");
        setSize(450, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField path = new JTextField();
        path.setEditable(false);

        JButton browse = new JButton("Browse");
        JButton upload = new JButton("Upload");

        browse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                path.setText(file.getAbsolutePath());
            }
        });

        upload.addActionListener(e -> {
            if (path.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a file first");
                return;
            }

            List<String[]> students = FileUtil.readCSV(STUDENT_FILE);
            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).length > 0 && students.get(i)[0].equals(studentId)) {
                    if (students.get(i).length < 8) students.set(i, java.util.Arrays.copyOf(students.get(i), 8));
                    students.get(i)[7] = path.getText();
                    break;
                }
            }

            List<String> updated = new java.util.ArrayList<>();
            for (String[] s : students) updated.add(String.join(",", s));
            FileUtil.writeAll(STUDENT_FILE, updated);

            JOptionPane.showMessageDialog(this, "File uploaded");
            dispose();
        });

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; panel.add(path, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; panel.add(browse, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(upload, gbc);

        add(panel);
        setVisible(true);
    }
}
