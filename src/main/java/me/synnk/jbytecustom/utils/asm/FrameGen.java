package me.synnk.jbytecustom.utils.asm;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.utils.ErrorDisplay;
import me.lpk.util.ASMUtils;
import me.lpk.util.JarUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class FrameGen extends Thread {

    private static Map<String, ClassNode> libraries;

    public static void regenerateFrames(JByteCustom jbm, ClassNode cn) {
        if (libraries == null && JByteCustom.ops.get("use_rt").getBoolean()) {
            if (JOptionPane.showConfirmDialog(null, JByteCustom.res.getResource("load_rt")) == JOptionPane.OK_OPTION) {
                try {
                    libraries = JarUtils.loadRT();
                } catch (IOException e) {
                    new ErrorDisplay(e);
                }
                if (libraries == null) {
                    return;
                }
            } else {
                return;
            }
        }
        ClassWriter cw = new LibClassWriter(ClassWriter.COMPUTE_FRAMES, jbm.getFile().getClasses(), libraries);
        try {
            cn.accept(cw);
            ClassNode node2 = ASMUtils.getNode(cw.toByteArray());
            cn.methods.clear();
            cn.methods.addAll(node2.methods);
            JByteCustom.LOGGER.log("Successfully regenerated frames at class " + cn.name);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void run() {
        try {
            libraries = JarUtils.loadRT();
            JByteCustom.LOGGER.log("Successfully loaded RT.jar");
        } catch (IOException e) {
            new ErrorDisplay(e);
        }
    }
}
