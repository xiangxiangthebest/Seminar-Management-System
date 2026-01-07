package Coordinator;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class AwardStatisticsFrame extends JFrame {

    public AwardStatisticsFrame() {
        setTitle("Award Statistics (Bar Chart)");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Map<String, Integer> stats = loadStatistics();
        add(new ChartPanel(stats));

        setVisible(true);
    }

    private Map<String, Integer> loadStatistics() {
        Map<String, Integer> stats = new LinkedHashMap<>();

        // Initialize categories so bars always appear
        stats.put("Best Oral", 0);
        stats.put("Best Poster", 0);
        stats.put("Most People Choice", 0);

        for (String[] row : FileUtil.readCSV("data/awards.txt")) {
            stats.put(row[0], stats.getOrDefault(row[0], 0) + 1);
        }
        return stats;
    }

    // ================= CHART PANEL =================
    class ChartPanel extends JPanel {

        Map<String, Integer> data;

        ChartPanel(Map<String, Integer> data) {
            this.data = data;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int width = getWidth();
            int height = getHeight();

            int padding = 50;
            int barWidth = 80;
            int maxBarHeight = height - 2 * padding;

            int maxValue = Collections.max(data.values());
            if (maxValue == 0) maxValue = 1; // avoid divide by zero

            int x = padding;

            g.setFont(new Font("Segoe UI", Font.BOLD, 14));

            // Draw title
            g.drawString("Award Nomination Statistics", width / 2 - 120, 30);

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int value = entry.getValue();
                int barHeight = (int) ((double) value / maxValue * maxBarHeight);

                int y = height - padding - barHeight;

                // Draw bar
                g.setColor(new Color(100, 149, 237));
                g.fillRect(x, y, barWidth, barHeight);

                // Draw border
                g.setColor(Color.BLACK);
                g.drawRect(x, y, barWidth, barHeight);

                // Draw value
                g.drawString(String.valueOf(value), x + 30, y - 5);

                // Draw label
                g.drawString(entry.getKey(), x - 10, height - padding + 20);

                x += barWidth + 40;
            }
        }
    }
}
