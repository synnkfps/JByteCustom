package me.grax.jbytemod.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.ui.FlatButtonBorder;
import me.grax.jbytemod.JByteMod;
import me.grax.jbytemod.utils.TextUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JAboutFrame extends JDialog {

    public JAboutFrame(JByteMod jbm) {
        this.setTitle(JByteMod.res.getResource("about") + " " + jbm.getTitle());
        this.setModal(true);
        setBounds(100, 100, 400, 300);
        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());
        cp.setBorder(new EmptyBorder(10, 10, 10, 10));
        setResizable(false);
        JButton close = new JButton(JByteMod.res.getResource("close"));
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JAboutFrame.this.dispose();
            }
        });
        JPanel jp = new JPanel(new GridLayout(1, 4));
        for (int i = 0; i < 3; i++)
            jp.add(new JPanel());

        jp.add(close);
        cp.add(jp, BorderLayout.PAGE_END);

        JTextPane title = new JTextPane();
        Font font = new Font("Segoe UI", Font.PLAIN, 12);
        title.setContentType("text/html");
        title.setFont(font);
        title.setText(TextUtils.toLight("JByteCustom " + JByteMod.version + " - by SynnK & JohnShiozo") + "<br>Reborn by Panda,<br> Remastered by xBrownieCodez,<br> Customized by " + TextUtils.toBold("SynnK") + ", libs containment by JohnShiozo<br>");
        UIDefaults defaults = new UIDefaults();
        defaults.put("TextPane[Enabled].backgroundPainter", this.getBackground());
        title.putClientProperty("Nimbus.Overrides", defaults);
        title.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
        title.setBackground(null);
        title.setEditable(false);
        title.setBorder(null);
        cp.add(title, BorderLayout.CENTER);

        getContentPane().add(cp);
    }
}
