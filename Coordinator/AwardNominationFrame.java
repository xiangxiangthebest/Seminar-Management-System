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
           STEP 2: Track awarded students
           --------------------------------- */
        Set<String> awardedStudents = new HashSet<>();

        for (String[] row : awards) {
            if (row.length >= 2) {
                String studentName = row[1].trim();
                awardedStudents.add(studentName);
            }
        }

        System.out.println("Already awarded students: " + awardedStudents);

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

        System.out.println("Student session types: " + studentSessionType);

        /* ---------------------------------
           STEP 4: Count current awards per category
           --------------------------------- */
        Map<String, Integer> awardCount = new HashMap<>();
        awardCount.put("Best Oral", 0);
        awardCount.put("Best Poster", 0);
        awardCount.put("People Choice", 0);

        for (String[] row : awards) {
            if (row.length >= 1) {
                String awardType = row[0].trim();
                awardCount.put(awardType, awardCount.getOrDefault(awardType, 0) + 1);
            }
        }

        System.out.println("Current award counts: " + awardCount);

        /* ---------------------------------
           STEP 5: Define award limits
           --------------------------------- */
        Map<String, Integer> awardLimits = new HashMap<>();
        awardLimits.put("Best Oral", 2);
        awardLimits.put("Best Poster", 2);
        awardLimits.put("People Choice", 5);

        /* ---------------------------------
           STEP 6: Award dropdown (with limits)
           --------------------------------- */
        DefaultComboBoxModel<String> awardModel = new DefaultComboBoxModel<>();
        
        for (String awardType : awardLimits.keySet()) {
            int current = awardCount.getOrDefault(awardType, 0);
            int limit = awardLimits.get(awardType);
            if (current < limit) {
                awardModel.addElement(awardType);
            }
        }

        JComboBox<String> award = new JComboBox<>(awardModel);
        JComboBox<String> student = new JComboBox<>();

        /* ---------------------------------
           STEP 7: Update student list by award
           --------------------------------- */
        award.addActionListener(e -> {
            student.removeAllItems();

            String selectedAward = award.getSelectedItem().toString();
            List<String> eligible = new ArrayList<>();

            System.out.println("\n=== Processing Award: " + selectedAward + " ===");
            System.out.println("Total evaluations: " + evaluations.size());

            for (String[] eval : evaluations) {
                try {
                    if (eval.length < 6) {
                        System.out.println("Skipped: Row has insufficient columns (" + eval.length + ")");
                        continue;
                    }

                    String studentName = eval[0].trim();

                    double score1 = Double.parseDouble(eval[2].trim());
                    double score2 = Double.parseDouble(eval[3].trim());
                    double score3 = Double.parseDouble(eval[4].trim());
                    double score4 = Double.parseDouble(eval[5].trim());
                    double total = score1 + score2 + score3 + score4;

                    System.out.println("\nStudent: " + studentName + 
                                     " | Scores: " + score1 + "," + score2 + "," + score3 + "," + score4 + 
                                     " | Total: " + total);

                    // Score check (minimum 70)
                    if (total < 70) {
                        System.out.println("  ✗ Rejected: Score too low (" + total + " < 70)");
                        continue;
                    }

                    // Already-awarded check
                    if (awardedStudents.contains(studentName)) {
                        System.out.println("  ✗ Rejected: Already awarded");
                        continue;
                    }

                    // Must exist in sessions
                    String sessionType = studentSessionType.get(studentName);
                    if (sessionType == null) {
                        System.out.println("  ✗ Rejected: Not found in sessions");
                        continue;
                    }

                    System.out.println("  → Session Type: " + sessionType);

                    // Award-specific restrictions
                    if ("Best Oral".equals(selectedAward)
                            && !"Oral".equalsIgnoreCase(sessionType)) {
                        System.out.println("  ✗ Rejected: Not an Oral session");
                        continue;
                    }

                    if ("Best Poster".equals(selectedAward)
                            && !"Poster".equalsIgnoreCase(sessionType)) {
                        System.out.println("  ✗ Rejected: Not a Poster session");
                        continue;
                    }

                    System.out.println("  ✓ ELIGIBLE!");
                    eligible.add(studentName);

                } catch (NumberFormatException ex) {
                    System.out.println("Error parsing scores: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("Error processing row: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }

            System.out.println("\nTotal eligible students: " + eligible.size());

            if (eligible.isEmpty()) {
                student.addItem("No eligible student");
                System.out.println("WARNING: No eligible students found for " + selectedAward);
            } else {
                for (String s : eligible) {
                    student.addItem(s);
                }
            }
        });

        if (awardModel.getSize() > 0) {
            award.setSelectedIndex(0); // auto-load students
        } else {
            student.addItem("All awards have reached their limits");
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
           STEP 8: Save nomination
           --------------------------------- */
        save.addActionListener(e -> {

            if ("No eligible student".equals(student.getSelectedItem()) 
                    || "All awards have reached their limits".equals(student.getSelectedItem())) {
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