package me.synnk.jbytecustom.ui;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.ui.lists.AdressList;
import me.synnk.jbytecustom.ui.lists.ErrorList;
import me.synnk.jbytecustom.ui.lists.MyCodeList;

import javax.swing.*;
import java.awt.*;

public class MyCodeEditor extends JPanel {
    private MyCodeList cl;

    public MyCodeEditor(JByteCustom jbm, JLabel editor) {
        this.setLayout(new BorderLayout());
        cl = new MyCodeList(jbm, editor);
        this.add(cl, BorderLayout.CENTER);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, JByteCustom.border));
        p.add(new AdressList(cl), BorderLayout.CENTER);
        this.add(p, BorderLayout.WEST);
        this.add(new ErrorList(jbm, cl), BorderLayout.EAST);

    }

    public MyCodeList getEditor() {
        return cl;
    }
}
