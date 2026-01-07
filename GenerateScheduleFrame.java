import java.util.*;
import javax.swing.*;

public class GenerateScheduleFrame extends JFrame {

    public GenerateScheduleFrame() {
        setTitle("Seminar Schedule");
        setSize(600, 300);
        setLocationRelativeTo(null);

        String[] columns = {"Session ID", "Date", "Venue", "Type"};
        List<String[]> data = FileUtil.readCSV("data/sessions.txt");

        String[][] rows = data.toArray(new String[0][]);
        JTable table = new JTable(rows, columns);

        add(new JScrollPane(table));
        setVisible(true);
    }
}
