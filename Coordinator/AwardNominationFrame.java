//read sessions.txt
//read evaluations.txt

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
           STEP 1: Read awards.txt
           --------------------------------- */
        Map<String, Integer> awardCount = new HashMap<>();
        Set<String> awardedStudents = new HashSet<>();

        List<String[]> awards = FileUtil.readCSV("data/awards.txt");
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
           STEP 2: Award dropdown (check quota)
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
           STEP 3: Load data files
           --------------------------------- */
        List<String[]> evaluations = FileUtil.readCSV("data/evaluations.txt");
        List<String[]> sessions = FileUtil.readCSV("data/sessions.txt");

        /* ---------------------------------
           STEP 4: Update student list
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
                    String studentName = eval[0];

                    double total =
                            Double.parseDouble(eval[2]) +
                            Double.parseDouble(eval[3]) +
                            Double.parseDouble(eval[4]) +
                            Double.parseDouble(eval[5]);

                    // Must be >90 and not already awarded
                    if (total <= 90 || awardedStudents.contains(studentName))
                        continue;

                    String presentationType = getPresentationType(studentName, sessions);

                    if (selectedAward.equals("People Choice")) {
                        eligible.add(studentName);
                    } else if (selectedAward.equals("Best Oral")
                            && "Oral".equalsIgnoreCase(presentationType)) {
                        eligible.add(studentName);
                    } else if (selectedAward.equals("Best Poster")
                            && "Poster".equalsIgnoreCase(presentationType)) {
                        eligible.add(studentName);
                    }

                } catch (Exception ex) {
                    // skip invalid rows
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
            award.setSelectedIndex(0); // trigger load
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
           STEP 5: Save nomination
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

    /* ---------------------------------
       Helper Methods
       --------------------------------- */
    private String getPresentationType(String student, List<String[]> sessions) {
        for (String[] s : sessions) {
            if (s.length >= 5 && s[4].equals(student)) {
                return s[3]; // Oral / Poster
            }
        }
        return "";
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
