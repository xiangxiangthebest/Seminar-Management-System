//read sessions.txt
//generate final_schedule.txt

package Coordinator;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GenerateScheduleFrame extends JFrame {

    public GenerateScheduleFrame() {
        setTitle("Seminar Schedule");
        setSize(900, 450);   // slightly bigger
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table columns
        String[] columns = {
                "Session ID",
                "Day",
                "Date & Time",
                "Venue",
                "Type",
                "Student",
                "Evaluator"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };

        // Read sessions.txt
        List<String[]> sessions = FileUtil.readCSV("data/sessions.txt");

        for (String[] row : sessions) {
            if (row.length >= 7) {
                model.addRow(new Object[]{
                        row[0].trim(), // Session ID
                        row[1].trim(), // Day
                        row[2].trim(), // Date & Time
                        row[3].trim(), // Venue
                        row[4].trim(), // Type
                        row[5].trim(), // Student
                        row[6].trim()  // Evaluator
                });
            }
        }

        // JTable
        JTable table = new JTable(model);
        table.setRowHeight(22);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set column widths (prevents layout overflow)
        int[] widths = {90, 90, 160, 140, 80, 120, 120};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
            FileUtil.writeLine("data/final_schedule.txt", sb.toString(), false);
            JOptionPane.showMessageDialog(this,
                    "Schedule exported successfully!",
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        bottomPanel.add(exportBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GenerateScheduleFrame::new);
    }
}
