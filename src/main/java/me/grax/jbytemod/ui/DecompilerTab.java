package me.grax.jbytemod.ui;

import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.grax.jbytemod.JByteMod;
import me.grax.jbytemod.decompiler.*;
import me.grax.jbytemod.discord.Discord;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class DecompilerTab extends JPanel {
    private static final File tempDir = new File(System.getProperty("java.io.tmpdir"));
    private static final File userDir = new File(System.getProperty("user.dir"));
    public static String decomp = null;
    public static Decompilers decompiler = Decompilers.CFR;
    public static DecompilerPanel dp;
    private final JLabel label;
    private final JByteMod jbm;
    private final JButton compile = new JButton("Compile");

    static {
        dp = new DecompilerPanel();
    }

    public DecompilerTab(JByteMod jbm) {
        this.jbm = jbm;
        this.label = new JLabel(decompiler + " Decompiler");
        jbm.setDP(dp);
        this.setLayout(new BorderLayout(0, 0));
        JPanel lpad = new JPanel();
        lpad.setBorder(new EmptyBorder(1, 5, 0, 1));
        lpad.setLayout(new GridLayout());
        lpad.add(label);
        JPanel rs = new JPanel();
        rs.setLayout(new GridLayout(1, 5));
        for (int i = 0; i < 3; i++)
            rs.add(new JPanel());
        JComboBox<Decompilers> decompilerCombo = new JComboBox<Decompilers>(Decompilers.values());

        Discord.currentDecompiler = decompiler.getName() + " " + decompiler.getVersion();
        Discord.updateDecompiler(Discord.currentDecompiler);

        decompilerCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DecompilerTab.this.decompiler = (Decompilers) decompilerCombo.getSelectedItem();
                assert decompiler != null;
                label.setText(decompiler.getName() + " " + decompiler.getVersion());

                Discord.currentDecompiler = decompiler.getName() + " " + decompiler.getVersion();
                Discord.updateDecompiler(Discord.currentDecompiler);

                decompile(Decompiler.last, Decompiler.lastMn, true);
            }
        });
        rs.add(decompilerCombo);

        compile.setVisible(false);
        rs.add(compile);

        JButton reload = new JButton(JByteMod.res.getResource("reload"));
        reload.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                decompile(Decompiler.last, Decompiler.lastMn, true);
            }
        });
        rs.add(reload);
        lpad.add(rs);
        this.add(lpad, BorderLayout.NORTH);
        JScrollPane scp = new RTextScrollPane(dp);
        scp.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scp, BorderLayout.CENTER);
    }

    public static void checkCompiler() throws IOException {
        File kotlinc = new File(userDir.getAbsolutePath() + "/kotlinc");
        if (!userDir.exists()) {
            userDir.mkdirs();
        } else {
            if (kotlinc.exists()) {
                return;
            }
        }

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(DecompilerTab.class.getResourceAsStream("/resources/kotlinc.zip"));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            File newFile = new File(userDir, fileName);
            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
            } else {
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    public void decompile(ClassNode cn, MethodNode mn, boolean deleteCache) {
        if (cn == null) {
            return;
        }
        Decompiler d = null;

        compile.setVisible(false);
        dp.setEditable(false);

        switch (decompiler) {
            case PROCYON:
                d = new ProcyonDecompiler(jbm, dp);
                break;
            case FERNFLOWER:
                d = new FernflowerDecompiler(jbm, dp);
                break;
            case CFR:
                d = new CFRDecompiler(jbm, dp);
                break;
            case KRAKATAU:
                d = new KrakatauDecompiler(jbm, dp);
                break;
            case KOFFEE:
                d = new KoffeeDecompiler(jbm, dp);
                break;
        }
        d.setNode(cn, mn);
        if (deleteCache) {
            d.deleteCache();
        }
        d.start();
    }

}