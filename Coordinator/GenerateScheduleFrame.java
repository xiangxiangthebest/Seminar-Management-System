package Coordinator;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GenerateScheduleFrame extends JFrame {

    public GenerateScheduleFrame() {
        setTitle("Seminar Schedule");
        setSize(800, 400);
        setLocationRelativeTo(null);

        // Columns for the table
        String[] columns = {"Session ID", "Day", "Date & Time", "Venue", "Type", "Student", "Evaluator"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        // Read sessions.txt
        List<String[]> sessions = FileUtil.readCSV("data/sessions.txt");
        for (String[] row : sessions) {
            if (row.length >= 6) {
                // Split first column (Sxxx) and date column
                String sessionId = row[0];
                String dateTime = row[1];

                String day = ""; // Default empty
                String dateTimeOnly = dateTime;

                // If dateTime includes day, split it
                if (dateTime.contains(",")) {
                    String[] parts = dateTime.split(",", 2);
                    day = parts[0].trim();
                    dateTimeOnly = parts[1].trim();
                }

                model.addRow(new Object[]{
                        sessionId,
                        day,
                        dateTimeOnly,
                        row[2], // Venue
                        row[3], // Type
                        row[4], // Student
                        row[5]  // Evaluator
                });
            }
        }

        // JTable
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Export button
        JButton exportBtn = new JButton("Export Schedule");
        exportBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    sb.append(model.getValueAt(i, j));
                    if (j < model.getColumnCount() - 1) sb.append(",");
                }
                sb.append("\n");
            }
            FileUtil.writeLine("data/final_schedule.txt", sb.toString(), true);
            JOptionPane.showMessageDialog(this, "Schedule exported to data/final_schedule.txt");
        });
        add(exportBtn, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GenerateScheduleFrame::new);
    }
}
