//read students.txt
//read evaluators.txt
//read sessions.txt


package Coordinator;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;


public class ManageSessionFrame extends JFrame {

    public ManageSessionFrame() {
        setTitle("Manage Seminar Session");
        setSize(550, 450);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Session Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ------------------------------
        // Auto-generate Session ID based on sessions.txt
        JTextField id = new JTextField(15);
        id.setEditable(false);
        id.setText(generateSessionID());

        // ------------------------------
        // Date & time spinner (shows day of week)
        SpinnerDateModel dateTimeModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        JSpinner dateTimeSpinner = new JSpinner(dateTimeModel);
        dateTimeSpinner.setEditor(new JSpinner.DateEditor(dateTimeSpinner, "EEEE, yyyy-MM-dd HH:mm"));

        // ------------------------------
        // Venue dropdown
        JComboBox<String> venue = new JComboBox<>(new String[]{
                "FCI BR1012","FCI BR1013","FCI BR1014","FCI BR1015",
                "FCI BR1016","FCI BR1017","FCI BR1018"
        });

        // Session type dropdown
        JComboBox<String> type = new JComboBox<>(new String[]{"Oral","Poster"});

        // ------------------------------
        // Students dropdown (exclude already assigned)
        java.util.List<String[]> students = FileUtil.readCSV("data/students.txt");
        java.util.List<String[]> sessions = FileUtil.readCSV("data/sessions.txt");

        Set<String> assignedStudents = new HashSet<>();
        for (String[] s : sessions) {
            if (s.length >=  6) {
                assignedStudents.add(s[s.length - 2].trim()); // TRIM spaces!
            }
        }

        java.util.List<String> availableStudents = new java.util.ArrayList<>();
        for (String[] s : students) {
            String studentName = s[1].trim(); // Student name is at index 1
            if (!assignedStudents.contains(studentName)) {
                availableStudents.add(studentName);
            }
        }

        JComboBox<String> student = new JComboBox<>(availableStudents.toArray(new String[0]));

        // ------------------------------
        // Evaluators dropdown from file
        java.util.List<String[]> evaluators = FileUtil.readCSV("data/evaluators.txt");
        java.util.List<String> evaluatorNames = new java.util.ArrayList<>();
        for (String[] e : evaluators) evaluatorNames.add(e[0].trim());
        JComboBox<String> evaluator = new JComboBox<>(evaluatorNames.toArray(new String[0]));

        // ------------------------------
        // Add components to form
        addRow(form, gbc, 0, "Session ID", id);
        addRow(form, gbc, 1, "Date & Time", dateTimeSpinner);
        addRow(form, gbc, 2, "Venue", venue);
        addRow(form, gbc, 3, "Session Type", type);
        addRow(form, gbc, 4, "Student", student);
        addRow(form, gbc, 5, "Evaluator", evaluator);

        // ------------------------------
        // Save button
        JButton save = new JButton("Save Session");
        gbc.gridx = 1;
        gbc.gridy = 6;
        form.add(save, gbc);

        save.addActionListener(e -> {
            Date dateTime = (Date) dateTimeSpinner.getValue();
            String dateTimeStr = new SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm").format(dateTime);

            // Save to sessions.txt
            FileUtil.writeLine("data/sessions.txt",
                    id.getText() + "," + dateTimeStr + "," +
                            venue.getSelectedItem() + "," +
                            type.getSelectedItem() + "," +
                            student.getSelectedItem() + "," +
                            evaluator.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Session saved successfully");

            // Auto-update session ID for next entry
            id.setText(generateSessionID());

            // Remove selected student from dropdown to prevent double assignment
            student.removeItem(student.getSelectedItem());

            if (student.getItemCount() == 0) {
                save.setEnabled(false);
                JOptionPane.showMessageDialog(this, "All students have been assigned!");
            }
        });

        add(form);
        setVisible(true);
    }

    // ------------------------------
    // Auto-generate next session ID based on last in sessions.txt
    private String generateSessionID() {
        java.util.List<String[]> sessions = FileUtil.readCSV("data/sessions.txt");
        int maxNum = 0;

        for (String[] s : sessions) {
            try {
                if (s.length > 0 && s[0].startsWith("S")) {
                    int num = Integer.parseInt(s[0].substring(1));
                    if (num > maxNum) maxNum = num;
                }
            } catch (Exception ignored) {}
        }

        // Next ID
        return "S" + String.format("%03d", maxNum + 1);
    }

    // ------------------------------
    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManageSessionFrame::new);
    }
}
