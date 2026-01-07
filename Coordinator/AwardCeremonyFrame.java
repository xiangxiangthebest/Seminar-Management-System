package Coordinator;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
public class AwardCeremonyFrame extends JFrame {

    public AwardCeremonyFrame() {
        setTitle("Award Ceremony Agenda");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Columns for the table
        String[] columns = {"Award", "Winner", "Presentation Title", "Advisor", "Type", "File"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        // Read student info from CSV
        java.util.List<String[]> students = FileUtil.readCSV("data/students.txt");
        Map<String, String[]> studentDataMap = new HashMap<>();
        for (String[] s : students) {
            if (s.length >= 6) {
                studentDataMap.put(s[0].trim(), s); // key = student name
            }
        }

        // Read awarded students
        java.util.List<String[]> awards = FileUtil.readCSV("data/awards.txt");

        // Map award -> list of winners
        Map<String, java.util.List<String>> awardWinners = new LinkedHashMap<>();
        for (String[] row : awards) {
            if (row.length < 2) continue;
            String award = row[0].trim();
            String student = row[1].trim();
            awardWinners.computeIfAbsent(award, k -> new ArrayList<>()).add(student);
        }

        // Populate table with full info
        for (Map.Entry<String, java.util.List<String>> entry : awardWinners.entrySet()) {
            String award = entry.getKey();
            for (String winner : entry.getValue()) {
                String[] studentInfo = studentDataMap.get(winner);
                if (studentInfo != null) {
                    model.addRow(new Object[]{
                            award,
                            studentInfo[0],       // Name
                            studentInfo[2],       // Presentation title
                            studentInfo[3],       // Advisor
                            studentInfo[4],       // Type (Oral/Poster)
                            studentInfo[5]        // File
                    });
                } else {
                    model.addRow(new Object[]{award, winner, "", "", "", ""});
                }
            }
        }

        // If no awards
        if (model.getRowCount() == 0) {
            model.addRow(new Object[]{"No awards", "No winners", "", "", "", ""});
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        setVisible(true);
    }
}
