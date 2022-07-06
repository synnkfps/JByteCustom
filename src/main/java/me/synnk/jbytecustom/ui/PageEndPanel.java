package me.synnk.jbytecustom.ui;

import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.laf.WebLookAndFeel;
import com.formdev.flatlaf.ui.FlatProgressBarUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.alee.managers.language.data.TooltipType.weblaf;

public class PageEndPanel extends JPanel {

    private static final String copyright = "\u00A9 JByteCustom - 2022 ";
    private final JProgressBar pb;
    private final JLabel percent;
    private final JLabel label;
    private WebMemoryBar memoryBar;
    Runtime instance = Runtime.getRuntime();


    public PageEndPanel() throws InterruptedException {
        this.pb = new JProgressBar() {
            @Override
            public void setValue(int n) {
                if (n == 100) {
                    super.setValue(0);
                    percent.setText("");
                } else {
                    super.setValue(n);
                    percent.setText(n + "%");
                }
            }
        };


        this.setLayout(new GridLayout(1, 4, 10, 10));
        this.setBorder(new EmptyBorder(3, 0, 0, 0));

        this.add(pb);
        this.add(percent = new JLabel());
        percent.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        label = new JLabel(copyright);

        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        this.add(label);

        JProgressBar pgb = new JProgressBar();
        this.add(pgb);

        int mb = 1048576;

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        pgb.setMaximum((int) (instance.maxMemory()) / 1000000); // 1700-1900 (gradle.properties)
        pgb.setStringPainted(true);
        pgb.setBorderPainted(true);
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                pgb.setValue((int)((instance.totalMemory() - instance.freeMemory()))/1000000);
                pgb.setName("" + (int)((instance.totalMemory() - instance.freeMemory()))/1000000);
            }
        }, 0, 1, TimeUnit.MILLISECONDS);


    }

    public void setValue(int n) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                pb.setValue(n);
                pb.repaint();
            }
        });
    }


    public void setTip(String s) {
        if (s != null) {
            label.setText(s);
        } else {
            label.setText(copyright);
        }
    }
}
