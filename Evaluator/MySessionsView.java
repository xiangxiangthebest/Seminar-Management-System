package Evaluator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class MySessionsView extends JFrame {

    String evaluatorName;
    JTable table;
    DefaultTableModel model;

    public MySessionsView(String evaluatorName) {

        this.evaluatorName = evaluatorName;

        setTitle("My Sessions - " + evaluatorName);
        setSize(900, 300);
        setLocationRelativeTo(null);

        
        String[] columns = {
                "Session ID",
                "Day",
                "Date & Time",
                "Venue",
                "Type",
                "Student Name"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        loadSessions();

        add(new JScrollPane(table));
    }

    private void loadSessions() {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("data/sessions.txt"));

            String line;
            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                String sessionId = data[0].trim();
                String day = data[1].trim();
                String datetime = data[2].trim();
                String venue = data[3].trim();
                String type = data[4].trim();
                String studentName = data[5].trim();
                String evaluator = data[6].trim();

                
                if (evaluator.equalsIgnoreCase(evaluatorName)) {

                    model.addRow(new Object[]{
                            sessionId,
                            day,
                            datetime,
                            venue,
                            type,
                            studentName
                    });
                }
            }

            br.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading sessions.txt");
            e.printStackTrace();
        }
    }
}