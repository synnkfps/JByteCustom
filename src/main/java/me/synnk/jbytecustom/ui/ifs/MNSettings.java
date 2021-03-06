package me.synnk.jbytecustom.ui.ifs;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.ui.frames.JAccessHelper;
import me.synnk.jbytecustom.ui.frames.JAnnotationEditor;
import me.synnk.jbytecustom.ui.dialogue.ClassDialogue;
import me.synnk.jbytecustom.utils.gui.SwingUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MNSettings extends MyInternalFrame {
    /**
     * Save position
     */
    private static Rectangle bounds = new Rectangle(670, 10, 1280 / 4, 720 / 3 + 80);

    public MNSettings(ClassNode cn, MethodNode mn) {
        super("Method Settings");
        this.setBounds(bounds);
        this.setLayout(new BorderLayout(0, 0));
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        final JPanel input = new JPanel(new GridLayout(0, 1));
        final JPanel labels = new JPanel(new GridLayout(0, 1));
        panel.add(labels, "West");
        panel.add(input, "Center");
        panel.add(new JLabel(JByteCustom.res.getResource("ref_warn")), "South");
        labels.add(new JLabel("Method Name:"));
        JTextField name = new JTextField(mn.name);
        input.add(name);
        labels.add(new JLabel("Method Desc:"));
        JTextField desc = new JTextField(mn.desc);
        input.add(desc);
        labels.add(new JLabel("Method Access:"));
        JFormattedTextField access = ClassDialogue.createNumberField(Integer.class, 0, Short.MAX_VALUE);
        access.setValue(mn.access);
        input.add(SwingUtils.withButton(access, "...", e -> {
            JAccessHelper jah = new JAccessHelper(mn, "access", access);
            jah.setVisible(true);
        }));
        labels.add(new JLabel("Method Signature:"));
        JTextField signature = new JTextField(mn.signature);
        input.add(signature);
        labels.add(new JLabel("Method MaxLocals:"));
        JFormattedTextField maxL = ClassDialogue.createNumberField(Integer.class, 0, Short.MAX_VALUE);
        maxL.setValue(mn.maxLocals);
        input.add(maxL);
        labels.add(new JLabel("Method MaxStack:"));
        JFormattedTextField maxS = ClassDialogue.createNumberField(Integer.class, 0, Short.MAX_VALUE);
        maxS.setValue(mn.maxStack);
        input.add(maxS);
        labels.add(new JLabel("Annotations:"));
        JButton annotations = new JButton(JByteCustom.res.getResource("edit"));
        annotations.addActionListener(a -> {
            if (!JAnnotationEditor.isOpen("visibleAnnotations"))
                new JAnnotationEditor("Annotations", mn, "visibleAnnotations").setVisible(true);
        });
        input.add(annotations);
        labels.add(new JLabel("Invisible Annotations:"));
        JButton invisAnnotations = new JButton(JByteCustom.res.getResource("edit"));
        invisAnnotations.addActionListener(a -> {
            if (!JAnnotationEditor.isOpen("invisibleAnnotations"))
                new JAnnotationEditor("Invisible Annotations", mn, "invisibleAnnotations").setVisible(true);
        });
        input.add(invisAnnotations);
        this.add(panel, BorderLayout.CENTER);
        JButton update = new JButton("Update");
        update.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean refresh = false;
                if (!mn.name.equals(name.getText())) {
                    refresh = true;
                    mn.name = name.getText();
                }
                mn.desc = desc.getText();
                mn.access = (int) access.getValue();
                mn.maxLocals = (int) maxL.getValue();
                mn.maxStack = (int) maxS.getValue();
                String sig = signature.getText();
                if (sig.isEmpty()) {
                    mn.signature = null;
                } else {
                    mn.signature = sig;
                }
                if (refresh) {
                    JByteCustom.instance.getJarTree().refreshMethod(cn, mn);
                }
            }
        });
        this.add(update, BorderLayout.PAGE_END);
        this.show();
    }

    @Override
    public void setVisible(boolean aFlag) {
        if (!aFlag && !(getLocation().getY() == 0 && getLocation().getX() == 0)) {
            bounds = getBounds();
        }
        super.setVisible(aFlag);
    }
}
