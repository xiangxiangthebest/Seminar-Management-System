package Coordinator;
import javax.swing.*;

public class FinalReportFrame extends JFrame {

    public FinalReportFrame() {
        setTitle("Final Evaluation Report");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        for (String[] row : FileUtil.readCSV("data/evaluations.txt")) {
            area.append("Student: " + row[0]
                + " | Score: " + row[1] + "\n");
        }

        add(new JScrollPane(area));
        setVisible(true);
    }
}
