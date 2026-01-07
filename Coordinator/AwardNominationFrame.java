package Coordinator;
import java.awt.*;
import javax.swing.*;

public class AwardNominationFrame extends JFrame {

    public AwardNominationFrame() {
        setTitle("Award Nomination");
        setSize(400, 250);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nominate Award"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);

        JComboBox<String> award =
            new JComboBox<>(new String[]{
                "Best Oral", "Best Poster", "Most People Choice"
            });

        JTextField student = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Award"), gbc);
        gbc.gridx = 1;
        panel.add(award, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Student Name"), gbc);
        gbc.gridx = 1;
        panel.add(student, gbc);

        JButton save = new JButton("Nominate");
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(save, gbc);

        save.addActionListener(e -> {
            FileUtil.writeLine("data/awards.txt",
                award.getSelectedItem()+","+student.getText());
            JOptionPane.showMessageDialog(this, "Nomination saved");
        });

        add(panel);
        setVisible(true);
    }
}
