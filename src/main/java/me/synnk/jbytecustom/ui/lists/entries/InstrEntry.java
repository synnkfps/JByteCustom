package me.synnk.jbytecustom.ui.lists.entries;

import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;
import me.synnk.jbytecustom.utils.asm.Hints;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class InstrEntry {
    private MethodNode m;
    private AbstractInsnNode i;

    public InstrEntry(MethodNode m, AbstractInsnNode i) {
        this.m = m;
        this.i = (AbstractInsnNode) i;
    }

    public MethodNode getMethod() {
        return m;
    }

    public void setM(MethodNode m) {
        this.m = m;
    }

    public AbstractInsnNode getInstr() {
        return i;
    }

    public void setI(AbstractInsnNode i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return TextUtils.toHtml(InstrUtils.toString(i));
    }

    public String toEasyString() {
        return InstrUtils.toEasyString(i);
    }

    public String getHint() {
        if (i != null && i.getOpcode() >= 0) {
            return Hints.hints[i.getOpcode()];
        }
        return null;
    }
}
