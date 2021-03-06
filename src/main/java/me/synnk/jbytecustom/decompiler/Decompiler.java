package me.synnk.jbytecustom.decompiler;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.ui.DecompilerPanel;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;

public abstract class Decompiler extends Thread {
    /**
     * Do not reload if we already know the output
     */
    public static ClassNode last;
    public static MethodNode lastMn;
    public static String lastOutput;
    protected JByteCustom jbm;
    protected ClassNode cn;
    protected DecompilerPanel dp;
    private MethodNode mn;

    public Decompiler(JByteCustom jbm, DecompilerPanel dp) {
        this.jbm = jbm;
        this.dp = dp;
    }

    public Decompiler setNode(ClassNode cn, MethodNode mn) {
        this.cn = cn;
        this.mn = mn;
        return this;
    }

    public Decompiler deleteCache() {
        last = null;
        return this;
    }

    @Override
    public final void run() {
        dp.setText("Loading...");
        if (cn == null) {
            dp.setText("ClassNode is null.");
            return;
        }
        dp.setText(lastOutput = this.decompile(cn, mn));
    }

    protected String decompile(ClassNode cn, MethodNode mn) {
        if (cn.equals(last)
                && ((lastMn == null && mn == null) || (mn != null && lastMn != null && mn.equals(lastMn)))) {
            return lastOutput;
        }
        last = cn;
        lastMn = mn;
        // do not regenerate anything here
        ClassWriter cw = new ClassWriter(0);
        cn.accept(cw);
        try{
            return decompile(cw.toByteArray(), mn);
        }catch(Exception exception){
            return "Failed to decompile, reason: " + exception + "\n" + Arrays.toString(exception.getStackTrace())+ "\n" + exception.getMessage();
        }

    }

    public abstract String decompile(byte[] b, MethodNode mn);

}