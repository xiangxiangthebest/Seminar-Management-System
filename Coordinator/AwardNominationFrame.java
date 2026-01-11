package Coordinator;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AwardNominationFrame extends JFrame {

    public AwardNominationFrame() {
        setTitle("Award Nomination");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nominate Award"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ---------------------------------
           STEP 1: Read files
           --------------------------------- */
        List<String[]> awards = FileUtil.readCSV("data/awards.txt");
        List<String[]> sessions = FileUtil.readCSV("data/sessions.txt");
        List<String[]> evaluations = FileUtil.readCSV("data/evaluations.txt");

        /* ---------------------------------
           STEP 2: Count awards + awarded students
           --------------------------------- */
        Map<String, Integer> awardCount = new HashMap<>();
        Set<String> awardedStudents = new HashSet<>();

        for (String[] row : awards) {
            if (row.length >= 2) {
                String awardName = row[0].trim();
                String studentName = row[1].trim();

                awardCount.put(
                        awardName,
                        awardCount.getOrDefault(awardName, 0) + 1
                );
                awardedStudents.add(studentName);
            }
        }

        /* ---------------------------------
           STEP 3: Map student → session type
           --------------------------------- */
        Map<String, String> studentSessionType = new HashMap<>();

        for (String[] row : sessions) {
            if (row.length >= 6) {
                String sessionType = row[4].trim(); // Oral / Poster
                String studentName = row[5].trim();
                studentSessionType.put(studentName, sessionType);
            }
        }

        /* ---------------------------------
           STEP 4: Award dropdown (with limits)
           --------------------------------- */
        DefaultComboBoxModel<String> awardModel = new DefaultComboBoxModel<>();

        if (awardCount.getOrDefault("Best Oral", 0) < 2)
            awardModel.addElement("Best Oral");

        if (awardCount.getOrDefault("Best Poster", 0) < 2)
            awardModel.addElement("Best Poster");

        if (awardCount.getOrDefault("People Choice", 0) < 3)
            awardModel.addElement("People Choice");

        JComboBox<String> award = new JComboBox<>(awardModel);
        JComboBox<String> student = new JComboBox<>();

        if (awardModel.getSize() == 0) {
            award.addItem("All awards have been given");
            award.setEnabled(false);
            student.addItem("No eligible student");
        }

        /* ---------------------------------
           STEP 5: Update student list by award
           --------------------------------- */
        award.addActionListener(e -> {
            student.removeAllItems();

            if (!award.isEnabled()) {
                student.addItem("No eligible student");
                return;
            }

            String selectedAward = award.getSelectedItem().toString();
            List<String> eligible = new ArrayList<>();

            for (String[] eval : evaluations) {
                try {
                    String studentName = eval[0].trim();

                    double total =
                        Double.parseDouble(eval[2].trim()) +
                        Double.parseDouble(eval[3].trim()) +
                        Double.parseDouble(eval[4].trim()) +
                        Double.parseDouble(eval[5].trim());

                    // Score + already-awarded check
                    if (total <= 90 || awardedStudents.contains(studentName))
                        continue;

                    // Must exist in sessions
                    String sessionType = studentSessionType.get(studentName);
                    if (sessionType == null)
                        continue;

                    // Award-specific restrictions
                    if ("Best Oral".equals(selectedAward)
                            && !"Oral".equalsIgnoreCase(sessionType))
                        continue;

                    if ("Best Poster".equals(selectedAward)
                            && !"Poster".equalsIgnoreCase(sessionType))
                        continue;

                    eligible.add(studentName);

                } catch (Exception ex) {
                    // Skip invalid row
                }
            }

            if (eligible.isEmpty()) {
                student.addItem("No eligible student");
            } else {
                for (String s : eligible) {
                    student.addItem(s);
                }
            }
        });

        if (award.isEnabled()) {
            award.setSelectedIndex(0); // auto-load students
        }

        /* ---------------------------------
           UI Layout
           --------------------------------- */
        addRow(panel, gbc, 0, "Award", award);
        addRow(panel, gbc, 1, "Student", student);

        JButton save = new JButton("Nominate");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(save, gbc);

        /* ---------------------------------
           STEP 6: Save nomination
           --------------------------------- */
        save.addActionListener(e -> {

            if (!award.isEnabled()
                    || "No eligible student".equals(student.getSelectedItem())) {
                JOptionPane.showMessageDialog(this,
                        "No valid nomination available",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            FileUtil.writeLine(
                    "data/awards.txt",
                    award.getSelectedItem() + "," + student.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this,
                    "Nomination saved successfully");

            dispose();
        });

        add(panel);
        setVisible(true);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc,
                        int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
}
