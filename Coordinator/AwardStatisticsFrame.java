//read awards.txt

package Coordinator;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class AwardStatisticsFrame extends JFrame {

    public AwardStatisticsFrame(){
        setTitle("Award Statistics");
        setSize(500,350);
        setLocationRelativeTo(null);

        Map<String,Integer> stats = new LinkedHashMap<>();
        stats.put("Best Oral",0);
        stats.put("Best Poster",0);
        stats.put("People Choice",0);

        for(String[] row: FileUtil.readCSV("data/awards.txt")){
            stats.put(row[0],stats.getOrDefault(row[0],0)+1);
        }

        JPanel panel = new JPanel(new BorderLayout());
        ChartPanel chart = new ChartPanel(stats);
        panel.add(chart, BorderLayout.CENTER);

        JButton exportBtn = new JButton("Export Chart Data");
        exportBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String,Integer> entry: stats.entrySet()){
                sb.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
            }
            FileUtil.writeLine("data/award_stats.txt", sb.toString(), false);
            JOptionPane.showMessageDialog(this,"Chart data exported to data/award_stats.txt");
        });

        panel.add(exportBtn, BorderLayout.SOUTH);
        add(panel);
        setVisible(true);
    }

    class ChartPanel extends JPanel{
        Map<String,Integer> data;
        ChartPanel(Map<String,Integer> data){ this.data=data; }

        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            int width=getWidth(), height=getHeight();
            int padding=50, barWidth=80, maxBarHeight=height-2*padding;
            int maxVal=Collections.max(data.values());
            if(maxVal==0) maxVal=1;
            int x=padding;

            g.setFont(new Font("Segoe UI",Font.BOLD,14));
            g.drawString("Award Statistics", width/2-70,30);

            for(Map.Entry<String,Integer> e:data.entrySet()){
                int val=e.getValue();
                int barHeight=(int)((double)val/maxVal*maxBarHeight);
                int y=height-padding-barHeight;

                g.setColor(new Color(100,149,237));
                g.fillRect(x,y,barWidth,barHeight);
                g.setColor(Color.BLACK);
                g.drawRect(x,y,barWidth,barHeight);
                g.drawString(String.valueOf(val),x+30,y-5);
                g.drawString(e.getKey(),x-10,height-padding+20);
                x+=barWidth+40;
            }
        }
    }
}
