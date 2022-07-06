package me.synnk.jbytecustom.ui;

import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.formdev.flatlaf.icons.FlatSearchIcon;
import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.ui.frames.JAccessHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MyToolBar extends JToolBar {
    private final MyMenuBar menubar;

    public MyToolBar(JByteCustom jbm) {
        this.menubar = (MyMenuBar) jbm.getJMenuBar();
        this.setFloatable(false);
        if (!menubar.isAgent()) {
            this.add(makeNavigationButton(JByteCustom.res.getResource("load"), getIcon("load"), e -> menubar.openLoadDialogue()));
            this.add(makeNavigationButton(JByteCustom.res.getResource("save"), getIcon("save"), e -> {
                if (menubar.getLastFile() != null) {
                    jbm.saveFile(menubar.getLastFile());
                } else {
                    menubar.openSaveDialogue();
                }
            }));
        } else {
            this.add(makeNavigationButton(JByteCustom.res.getResource("reload"), getIcon("reload"), e -> jbm.refreshAgentClasses()));
            this.add(makeNavigationButton(JByteCustom.res.getResource("apply"), getIcon("save"), e -> jbm.applyChangesAgent()));
        }
        this.addSeparator();
        this.setBorderPainted(false);
        this.add(makeNavigationButton(JByteCustom.res.getResource("search"), getIcon("search"), e -> menubar.searchLDC()));

        this.addSeparator();
        this.add(makeNavigationButton("Access Helper", getIcon("table"), e -> new JAccessHelper().setVisible(true)));
        this.add(makeNavigationButton("Attach to other process", getIcon("plug"), e -> menubar.openProcessSelection()));
    }

    private ImageIcon getIcon(String string) {
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/resources/toolbar/" + string + ".png")));
    }

    protected JButton makeNavigationButton(String action, ImageIcon i, ActionListener a) {
        JButton button = WebButton.createIconWebButton(i, StyleConstants.smallRound, true);
        button.setToolTipText(action);
        button.addActionListener(a);
        button.setFocusable(false);
        button.setBorderPainted(true);
        button.setRolloverEnabled(true);
        return button;
    }
}