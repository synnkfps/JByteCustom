package me.synnk.jbytecustom.ui.lists;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.ui.dialogue.InsnEditDialogue;
import me.synnk.jbytecustom.ui.lists.entries.LVPEntry;
import me.synnk.jbytecustom.utils.ErrorDisplay;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LVPList extends JList<LVPEntry> {

    public void addNodes(ClassNode cn, MethodNode mn) {
        DefaultListModel<LVPEntry> model = new DefaultListModel<>();
        this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        if (mn.localVariables != null)
            for (LocalVariableNode lvn : mn.localVariables) {
                model.addElement(new LVPEntry(cn, mn, lvn));
            }
        this.setModel(model);
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    LVPEntry selected = LVPList.this.getSelectedValue();
                    JPopupMenu menu = new JPopupMenu();
                    if (selected != null) {
                        JMenuItem remove = new JMenuItem(JByteCustom.res.getResource("remove"));
                        remove.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                ClassNode cn = selected.getCn();
                                MethodNode mn = selected.getMn();
                                mn.localVariables.remove(selected.getLvn());
                                LVPList.this.addNodes(cn, mn);
                            }
                        });
                        menu.add(remove);
                        JMenuItem edit = new JMenuItem(JByteCustom.res.getResource("edit"));
                        edit.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    new InsnEditDialogue(mn, selected.getLvn()).open();
                                } catch (Exception ex) {
                                    new ErrorDisplay(ex);
                                }
                                LVPList.this.addNodes(cn, mn);
                            }
                        });
                        menu.add(edit);
                    }
                    JMenuItem insert = new JMenuItem(selected != null ? JByteCustom.res.getResource("insert") : JByteCustom.res.getResource("add"));
                    insert.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                LocalVariableNode lvn = new LocalVariableNode("", "", "", null, null, mn.localVariables.size());
                                if (new InsnEditDialogue(mn, lvn).open())
                                    if (lvn.start != null && lvn.end != null) {
                                        mn.localVariables.add(lvn);
                                    }
                            } catch (Exception ex) {
                                new ErrorDisplay(ex);
                            }
                            LVPList.this.addNodes(cn, mn);
                        }
                    });
                    menu.add(insert);
                    menu.show(LVPList.this, e.getX(), e.getY());
                }
            }
        });
    }
}
