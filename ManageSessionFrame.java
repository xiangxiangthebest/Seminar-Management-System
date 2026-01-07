import java.awt.*;
import java.util.Date;
import javax.swing.*;

public class ManageSessionFrame extends JFrame {

    public ManageSessionFrame() {
        setTitle("Manage Seminar Session");
        setSize(450, 420);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Session Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField id = new JTextField(15);

        // ✅ Date & Time Picker
        SpinnerDateModel dateModel =
                new SpinnerDateModel(new Date(), null, null, java.util.Calendar.MINUTE);
        JSpinner dateTimeSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor editor =
                new JSpinner.DateEditor(dateTimeSpinner, "yyyy-MM-dd HH:mm");
        dateTimeSpinner.setEditor(editor);

        // ✅ Venue Dropdown (7 choices)
        JComboBox<String> venue = new JComboBox<>(new String[]{
                "FCI BR1012",
                "FCI BR1013",
                "FCI BR1014",
                "FCI BR1015",
                "FCI BR1016",
                "FCI BR1017",
                "FCI BR1018"
        });

        JComboBox<String> type =
                new JComboBox<>(new String[]{"Oral", "Poster"});

        // ✅ Dummy Student List
        JComboBox<String> student = new JComboBox<>(new String[]{
                "Student A",
                "Student B",
                "Student C",
                "Student D",
                "Student E"
        });

        // ✅ Dummy Evaluator List
        JComboBox<String> evaluator = new JComboBox<>(new String[]{
                "Dr. Ali",
                "Dr. Siti",
                "Dr. Kumar",
                "Dr. Tan",
                "Dr. Lim"
        });

        addRow(form, gbc, 0, "Session ID", id);
        addRow(form, gbc, 1, "Date & Time", dateTimeSpinner);
        addRow(form, gbc, 2, "Venue", venue);
        addRow(form, gbc, 3, "Session Type", type);
        addRow(form, gbc, 4, "Student", student);
        addRow(form, gbc, 5, "Evaluator", evaluator);

        JButton save = new JButton("Save Session");
        gbc.gridx = 1;
        gbc.gridy = 6;
        form.add(save, gbc);

        save.addActionListener(e -> {
            Date selectedDate = (Date) dateTimeSpinner.getValue();
            String dateTimeStr =
                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                            .format(selectedDate);

            FileUtil.writeLine("data/sessions.txt",
                    id.getText() + "," +
                    dateTimeStr + "," +
                    venue.getSelectedItem() + "," +
                    type.getSelectedItem() + "," +
                    student.getSelectedItem() + "," +
                    evaluator.getSelectedItem()
            );

            JOptionPane.showMessageDialog(this, "Session saved successfully");
        });

        add(form);
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
