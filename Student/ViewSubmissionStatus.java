package Student;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class ViewSubmissionStatus extends JFrame {

    private static final String STUDENT_FILE = "data/students.txt";

    public ViewSubmissionStatus(String studentId) {
        setTitle("Submission Status");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        List<String[]> students = FileUtil.readCSV(STUDENT_FILE);
        String[] studentData = null;

        for (String[] s : students) {
            if (s.length > 0 && s[0].equals(studentId)) {
                studentData = s;
                break;
            }
        }

        // ------------------ Submission Details Box ------------------
        JPanel submissionPanel = new JPanel();
        submissionPanel.setLayout(new BoxLayout(submissionPanel, BoxLayout.Y_AXIS));
        submissionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Submission Details",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                submissionPanel.getFont().deriveFont(14f)
        ));

        if (studentData == null) {
            submissionPanel.add(createLabel("No submission found"));
        } else {
            submissionPanel.add(createFieldLabel("Title:", studentData[3]));
            submissionPanel.add(createFieldLabel("Abstract:", studentData[4]));
            submissionPanel.add(createFieldLabel("Supervisor:", studentData[5]));
            submissionPanel.add(createFieldLabel("Type:", studentData[6]));
            submissionPanel.add(createFieldLabel("File:", studentData.length > 7 && !studentData[7].isEmpty() ? studentData[7] : "Not uploaded"));
        }

        mainPanel.add(submissionPanel);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll);

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel("<html>" + text + "</html>");
        label.setFont(label.getFont().deriveFont(14f));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }

    private JLabel createFieldLabel(String field, String value) {
        JLabel label = new JLabel("<html><b>" + field + "</b> " + value + "</html>");
        label.setFont(label.getFont().deriveFont(14f));
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return label;
    }
}
