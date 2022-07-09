package me.synnk.jbytecustom.ui;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbToggleButton;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.alee.utils.SwingUtils;
import me.synnk.jbytecustom.CustomRPC;
import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.decompiler.DecompilerOutput;
import me.synnk.jbytecustom.discord.Discord;
import me.synnk.jbytecustom.ui.graph.ControlFlowPanel;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyEditorTab extends JPanel {
    private static String analysisText = JByteCustom.res.getResource("analysis");
    private MyCodeEditor codeEditor;
    public static JLabel label;

    private JPanel code, info;
    private DecompilerTab decompiler;
    private ControlFlowPanel analysis;
    private JPanel center;
    private WebBreadcrumbToggleButton decompilerBtn;
    private WebBreadcrumbToggleButton analysisBtn;
    private WebBreadcrumbToggleButton codeBtn;
    private boolean classSelected = false;
    public static DiscordRPC discordRPC;
    DiscordRichPresence presence = new DiscordRichPresence();

    public MyEditorTab(JByteCustom jbm) {
        setLayout(new BorderLayout());
        this.center = new JPanel();
        center.setLayout(new GridLayout());
        label = new JLabel("JByteCustom");
        this.codeEditor = new MyCodeEditor(jbm, label);
        jbm.setCodeList(codeEditor.getEditor());
        this.code = withBorder(label, codeEditor);
        InfoPanel sp = new InfoPanel(jbm);
        jbm.setSP(sp);

        this.info = this.withBorder(new JLabel(JByteCustom.res.getResource("settings")), sp);

        this.decompiler = new DecompilerTab(jbm);

        jbm.setCFP(this.analysis = new ControlFlowPanel(jbm));

        center.add(code);
        WebBreadcrumb selector = new WebBreadcrumb(true);
        codeBtn = new WebBreadcrumbToggleButton("Code");
        codeBtn.setSelected(true);
        codeBtn.addActionListener(e -> {
            if (center.getComponent(0) != code) {
                center.removeAll();
                center.add(code);
                center.revalidate();
                repaint();
            }
        });

        WebBreadcrumbToggleButton infoBtn = new WebBreadcrumbToggleButton("Info");
        infoBtn.addActionListener(e -> {
            if (center.getComponent(0) != info) {
                center.removeAll();
                center.add(info);
                center.revalidate();
                repaint();
            }
        });
        decompilerBtn = new WebBreadcrumbToggleButton("Decompiler");
        decompilerBtn.addActionListener(e -> {
            Discord.currentDecompiler = DecompilerTab.decompiler.getName() + " " + DecompilerTab.decompiler.getVersion();
            Discord.updateDecompiler(Discord.currentDecompiler);
            if (center.getComponent(0) != decompiler) {
                center.removeAll();
                center.add(decompiler);
                center.revalidate();
                repaint();
                decompiler.decompile(jbm.getCurrentNode(), jbm.getCurrentMethod(), true);
            }
        });
        analysisBtn = new WebBreadcrumbToggleButton(analysisText);
        analysisBtn.addActionListener(e -> {
            if (center.getComponent(0) != analysis) {
                center.removeAll();
                center.add(analysis);
                if (!classSelected) {
                    analysis.generateList();
                } else {
                    analysis.clear();
                }
                center.revalidate();
                repaint();
            }
        });
        selector.add(codeBtn);
        selector.add(infoBtn);
        selector.add(decompilerBtn);
        selector.add(analysisBtn);
        SwingUtils.groupButtons(selector);
        JPanel south = new JPanel();
        south.setLayout(new FlowLayout(FlowLayout.LEFT));
        south.add(selector);
        this.add(center, BorderLayout.CENTER);
        this.add(south, BorderLayout.PAGE_END);
    }

    public WebBreadcrumbToggleButton getCodeBtn() {
        return codeBtn;
    }

    private JPanel withBorder(JLabel label, Component c) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        JPanel lpad = new JPanel();
        lpad.setBorder(new EmptyBorder(1, 5, 0, 5));
        lpad.setLayout(new GridLayout());
        lpad.add(label);
        panel.add(lpad, BorderLayout.NORTH);
        JScrollPane scp = new JScrollPane(c);
        scp.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scp, BorderLayout.CENTER);
        return panel;
    }

    public void selectClass(ClassNode cn) {
        String regexed = cn.name.toString();
        final Pattern pattern = Pattern.compile(".+/", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(regexed);

        if (decompilerBtn.isSelected()) {
            decompiler.decompile(cn, null, true);
        }
        if (analysisBtn.isSelected()) {
            analysis.clear();
        }
        this.classSelected = true;
    }

    public void selectMethod(ClassNode cn, MethodNode mn) {
        String regexed = cn.name.toString();
        final Pattern pattern = Pattern.compile(".+/", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(regexed);

        if (decompilerBtn.isSelected()) {
            decompiler.decompile(cn, mn, true);
        }
        if (analysisBtn.isSelected()) {
            analysis.generateList();
        }
        this.classSelected = false;
    }
}