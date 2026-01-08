//read evaluations.txt
//generate final_report.txt

package Coordinator;

import java.awt.*;
import java.util.List;
import javax.swing.*;   

public class FinalReportFrame extends JFrame {

    public FinalReportFrame() {
        setTitle("Final Evaluation Report");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        List<String[]> evaluations = FileUtil.readCSV("data/evaluations.txt");
        double totalMarksAll = 0;
        int studentCount = evaluations.size();

        for (String[] row : evaluations) {
            try {
                double total = Double.parseDouble(row[2]) + Double.parseDouble(row[3])
                        + Double.parseDouble(row[4]) + Double.parseDouble(row[5]);
                totalMarksAll += total;
                area.append("Student: " + row[0]
                        + " | Evaluator: " + row[1]
                        + " | Marks: " + row[2] + "," + row[3] + "," + row[4] + "," + row[5]
                        + " | Comments: " + row[6]
                        + " | Total: " + total + "\n");
            } catch (Exception e) {
            }
        }

        double averageMarks = studentCount > 0 ? totalMarksAll / studentCount : 0;
        area.append("\n--- Analytics ---\n");
        area.append("Total Students: " + studentCount + "\n");
        area.append("Average Total Marks: " + String.format("%.2f", averageMarks) + "\n");

        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        JButton exportBtn = new JButton("Export Report");
        exportBtn.addActionListener(e -> {
            FileUtil.writeLine("data/final_report.txt", area.getText(), false);
            JOptionPane.showMessageDialog(this, "Report exported to data/final_report.txt");
        });

        panel.add(exportBtn, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
}
