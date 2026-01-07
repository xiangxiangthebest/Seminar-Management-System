import java.awt.*;
import javax.swing.*;

public class CoordinatorDashboard extends JFrame {

    public CoordinatorDashboard() {
        setTitle("Coordinator Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel menu = new JPanel(new GridLayout(3, 2, 15, 15));
        menu.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        addButton(menu, "Manage Seminar Session", () -> new ManageSessionFrame());
        addButton(menu, "Generate Schedule", () -> new GenerateScheduleFrame());
        addButton(menu, "Final Evaluation Report", () -> new FinalReportFrame());
        addButton(menu, "Award Nomination", () -> new AwardNominationFrame());
        addButton(menu, "Award Statistics", () -> new AwardStatisticsFrame());

        add(menu);
        setVisible(true);
    }

    private void addButton(JPanel panel, String title, Runnable action) {
        JButton btn = new JButton(title);
        btn.addActionListener(e -> action.run());
        panel.add(btn);
    }
}
