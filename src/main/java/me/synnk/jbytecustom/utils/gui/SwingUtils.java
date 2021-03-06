package me.synnk.jbytecustom.utils.gui;

import com.formdev.flatlaf.ui.FlatListUI;

import javax.swing.*;
import javax.swing.plaf.ListUI;
import java.awt.*;
import java.awt.event.ActionListener;

public class SwingUtils {
    public static JPanel withButton(Component c, String text, ActionListener e) {
        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        jp.add(c, BorderLayout.CENTER);
        JButton help = new JButton(text);
        help.addActionListener(e);
        jp.add(help, BorderLayout.EAST);
        return jp;
    }

    public static void disableSelection(JList<?> jl) {
        ListUI ui = (ListUI) jl.getUI();

        if (ui instanceof FlatListUI) {
            FlatListUI flui = (FlatListUI) ui;
        }

    }
}
