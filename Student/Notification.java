package Student;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class Notification extends JFrame {

    private static final String AWARDS_FILE = "data/awards.txt";
    private static final String SESSIONS_FILE = "data/sessions.txt";

    public Notification(String studentName) {
        setTitle("Notifications for " + studentName);
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ------------------ Awards Box ------------------
        JPanel awardsPanel = new JPanel();
        awardsPanel.setLayout(new BoxLayout(awardsPanel, BoxLayout.Y_AXIS));
        awardsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Awards",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                awardsPanel.getFont().deriveFont(14f)
        ));

        List<String[]> awards = FileUtil.readCSV(AWARDS_FILE);
        boolean hasAward = false;
        for (String[] a : awards) {
            if (a.length >= 2 && a[1].equalsIgnoreCase(studentName)) {
                JLabel lblAward = createLabel("Award: " + a[0]);
                awardsPanel.add(lblAward);
                hasAward = true;
            }
        }
        if (!hasAward) {
            awardsPanel.add(createLabel("No award notifications"));
        }

        mainPanel.add(awardsPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // ------------------ Session Details Box ------------------
        JPanel sessionPanel = new JPanel();
        sessionPanel.setLayout(new BoxLayout(sessionPanel, BoxLayout.Y_AXIS));
        sessionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Session Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                sessionPanel.getFont().deriveFont(14f)
        ));

        List<String[]> sessions = FileUtil.readCSV(SESSIONS_FILE);
        boolean hasSession = false;
        for (String[] s : sessions) {
            if (s.length >= 6 && s[5].equalsIgnoreCase(studentName)) {
                String sessionInfo = String.format(
                        "<b>Session %s (%s)</b><br>" +
                        "<b>Time:</b> %s<br>" +
                        "<b>Venue:</b> %s<br>" +
                        "<b>Type:</b> %s<br>" +
                        "<b>Evaluator:</b> %s",
                        s[0], s[1], s[2], s[3], s[4], s[6]
                );
                JLabel lblSession = createLabel(sessionInfo);
                sessionPanel.add(lblSession);
                sessionPanel.add(Box.createVerticalStrut(5));
                hasSession = true;
            }
        }
        if (!hasSession) {
            sessionPanel.add(createLabel("No session assignment notifications"));
        }

        mainPanel.add(sessionPanel);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll);

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel("<html>" + text + "</html>");
        label.setFont(label.getFont().deriveFont(14f));
        return label;
    }
}
