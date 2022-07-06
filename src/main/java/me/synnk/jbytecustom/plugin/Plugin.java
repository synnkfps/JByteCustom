package me.synnk.jbytecustom.plugin;

import me.synnk.jbytecustom.JByteCustom;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import java.util.Map;

public abstract class Plugin {
    protected String name;
    protected String version;
    protected String author;

    public Plugin(String name, String version, String author) {
        this.name = name;
        this.version = version;
        this.author = author;
    }

    public abstract void init();

    public abstract void loadFile(Map<String, ClassNode> map);

    public abstract boolean isClickable();

    public abstract void menuClick();

    protected final Map<String, ClassNode> getCurrentFile() {
        return JByteCustom.instance.getFile().getClasses();
    }

    protected final void updateTree() {
        JByteCustom.instance.refreshTree();
    }

    protected final JMenuBar getMenu() {
        return JByteCustom.instance.getMyMenuBar();
    }

    protected final JTree gerTree() {
        return JByteCustom.instance.getJarTree();
    }

    protected final ClassNode gerSelectedNode() {
        return JByteCustom.instance.getCurrentNode();
    }

    protected final MethodNode gerSelectedMethod() {
        return JByteCustom.instance.getCurrentMethod();
    }

    public final String getName() {
        return name;
    }

    public final String getVersion() {
        return version;
    }

    public final String getAuthor() {
        return author;
    }
}
