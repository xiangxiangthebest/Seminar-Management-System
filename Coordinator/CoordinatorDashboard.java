package Coordinator;

import java.awt.*;
import javax.swing.*;

public class CoordinatorDashboard extends JFrame {

    public CoordinatorDashboard() {
        setTitle("Coordinator Dashboard");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

        JButton btnSession = new JButton("Manage Sessions");
        btnSession.addActionListener(e -> new ManageSessionFrame());

        JButton btnSchedule = new JButton("Generate Schedule");
        btnSchedule.addActionListener(e -> new GenerateScheduleFrame());

        JButton btnReport = new JButton("Generate Final Report");
        btnReport.addActionListener(e -> new FinalReportFrame());

        JButton btnAward = new JButton("Award Nomination");
        btnAward.addActionListener(e -> new AwardNominationFrame());

        JButton btnStats = new JButton("View Award Statistics");
        btnStats.addActionListener(e -> new AwardStatisticsFrame());

        JButton btnCeremony = new JButton("Award Ceremony Agenda");
        btnCeremony.addActionListener(e -> new AwardCeremonyFrame());

        panel.add(btnSession);
        panel.add(btnSchedule);
        panel.add(btnReport);
        panel.add(btnAward);
        panel.add(btnStats);
        panel.add(btnCeremony);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CoordinatorDashboard::new);
    }
}
