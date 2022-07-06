package me.synnk.jbytecustom.ui;

import me.synnk.jbytecustom.JByteCustom;

import javax.swing.*;
import java.awt.*;

public class MySplitPane extends JSplitPane {
    private JTabbedPane rightSide;
    private JPanel leftSide;

    public MySplitPane(JByteCustom jbm, ClassTree classTree) {
        rightSide = new MyTabbedPane(jbm);
        leftSide = new JPanel();
        leftSide.setLayout(new BorderLayout(0, 0));
        leftSide.add(new JLabel(" " + JByteCustom.res.getResource("java_archive")), BorderLayout.NORTH);
        leftSide.add(new JScrollPane(classTree), BorderLayout.CENTER);
        this.setLeftComponent(leftSide);
        this.setRightComponent(rightSide);
        this.setDividerLocation(280);
        this.setContinuousLayout(true);
    }
}
