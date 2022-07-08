package me.synnk.jbytecustom.ui;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.JarArchive;
import me.synnk.jbytecustom.decompiler.Decompiler;
import me.synnk.jbytecustom.decompiler.Decompilers;
import me.synnk.jbytecustom.ui.dialogue.InsnEditDialogue;
import me.synnk.jbytecustom.ui.tree.SortedTreeNode;
import me.synnk.jbytecustom.utils.ErrorDisplay;
import me.synnk.jbytecustom.utils.MethodUtils;
import me.synnk.jbytecustom.utils.asm.FrameGen;
import me.lpk.util.drop.IDropUser;
import me.lpk.util.drop.JarDropHandler;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class ClassTree extends JTree implements IDropUser {

    private static final ArrayList<Object> expandedNodes = new ArrayList<>();
    private final JByteCustom jbm;
    private final DefaultTreeModel model;

    public ClassTree(JByteCustom jam) {
        this.jbm = jam;
        this.setRootVisible(false);
        this.setShowsRootHandles(true);
        this.setCellRenderer(new TreeCellRenderer());
        this.addTreeSelectionListener(e -> {
            SortedTreeNode node = (SortedTreeNode) ClassTree.this.getLastSelectedPathComponent();
            if (node == null)
                return;
            if (node.getCn() != null && node.getMn() != null) {
                jam.selectMethod(node.getCn(), node.getMn());
            } else if (node.getCn() != null) {
                jam.selectClass(node.getCn());
            }
        });
        this.model = new DefaultTreeModel(new SortedTreeNode(""));
        this.setModel(model);
        this.setTransferHandler(new JarDropHandler(this, 0));
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public void refreshTree(JarArchive jar) {
        DefaultTreeModel tm = this.model;
        SortedTreeNode root = (SortedTreeNode) tm.getRoot();
        root.removeAllChildren();
        tm.reload();

        HashMap<String, SortedTreeNode> preloadMap = new HashMap<>();
        if (jar.getClasses() != null)
            for (ClassNode c : jar.getClasses().values()) {
                String name = c.name;
                String[] path = array_unique(name.split("/"));
                name = String.join("/", path);

                int i = 0;
                int slashIndex = 0;
                SortedTreeNode prev = root;
                while (true) {
                    slashIndex = name.indexOf("/", slashIndex + 1);
                    if (slashIndex == -1) {
                        break;
                    }
                    String p = name.substring(0, slashIndex);
                    if (preloadMap.containsKey(p)) {
                        prev = preloadMap.get(p);
                    } else {
                        try{
                            SortedTreeNode stn = new SortedTreeNode(path[i]);
                            prev.add(stn);
                            prev = stn;
                            preloadMap.put(p, prev);
                        }catch(ArrayIndexOutOfBoundsException ex){
                            JByteCustom.LOGGER.println("Failed to load " + path[i]);
                        }
                    }
                    i++;
                }
                SortedTreeNode clazz = new SortedTreeNode(c);
                prev.add(clazz);
                for (MethodNode m : c.methods) {
                    clazz.add(new SortedTreeNode(c, m));
                }
            }
        boolean sort = JByteCustom.ops.get("sort_methods").getBoolean();
        sort(tm, root, sort);
        tm.reload();
        addListener();
        if (!expandedNodes.isEmpty()) {
            expandSaved(root);
        }
    }


    public static String[] array_unique(String[] ss) {
        // array_unique
        List<String> list = new ArrayList<>();
        for(String s:ss){
            if(!list.contains(s)) list.add(s);
        }
        return list.toArray(new String[0]);
    }

    public void expandSaved(SortedTreeNode node) {
        TreePath tp = new TreePath(node.getPath());
        if (node.getCn() != null && expandedNodes.contains(node.getCn())) {
            super.expandPath(tp);
        }
        if (expandedNodes.contains(tp.toString())) {
            super.expandPath(tp);
        }
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                SortedTreeNode n = (SortedTreeNode) e.nextElement();
                expandSaved(n);
            }
        }
    }

    @Override
    public void expandPath(TreePath path) {
        SortedTreeNode stn = (SortedTreeNode) path.getLastPathComponent();
        if (stn.getCn() != null) {
            expandedNodes.add(stn.getCn());

        } else {
            expandedNodes.add(path.toString());
        }
        super.expandPath(path);
    }

    @Override
    public void collapsePath(TreePath path) {
        SortedTreeNode stn = (SortedTreeNode) path.getLastPathComponent();
        if (stn.getCn() != null) {
            expandedNodes.remove(stn.getCn());
        } else {
            expandedNodes.remove(path.toString());
        }
        super.collapsePath(path);
    }

    private void addListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    TreePath tp = ClassTree.this.getPathForLocation(me.getX(), me.getY());
                    if (tp != null && tp.getParentPath() != null) {
                        ClassTree.this.setSelectionPath(tp);
                        if (ClassTree.this.getLastSelectedPathComponent() == null) {
                            return;
                        }
                        SortedTreeNode stn = (SortedTreeNode) ClassTree.this.getLastSelectedPathComponent();
                        MethodNode mn = stn.getMn();
                        ClassNode cn = stn.getCn();

                        if (mn != null) {
                            //method selected
                            JPopupMenu menu = new JPopupMenu();
                            JMenuItem edit = new JMenuItem(JByteCustom.res.getResource("edit"));
                            edit.addActionListener(e -> {
                                new InsnEditDialogue(mn, mn).open();
                                changedChild((TreeNode) model.getRoot());
                            });
                            menu.add(edit);
                            JMenuItem duplicate = new JMenuItem(JByteCustom.res.getResource("duplicate"));
                            duplicate.addActionListener(e -> {
                                MethodNode dup = MethodUtils.copy(mn);
                                String name = JOptionPane.showInputDialog(null, "Duplicated method name ?", "Rename", JOptionPane.QUESTION_MESSAGE);
                                if(MethodUtils.equalName(cn, name)){
                                    JOptionPane.showMessageDialog(null, "The name is already existed.", "Existed Name!", JOptionPane.WARNING_MESSAGE);
                                    return;
                                }
                                dup.name = name;
                                cn.methods.add(dup);
                                jbm.getJarTree().refreshTree(jbm.getFile());
                            });
                            menu.add(duplicate);
                            JMenuItem search = new JMenuItem(JByteCustom.res.getResource("search"));
                            search.addActionListener(e -> jbm.getSearchList().searchForFMInsn(cn.name, mn.name, mn.desc, false, false));
                            menu.add(search);
                            JMenuItem remove = new JMenuItem(JByteCustom.res.getResource("remove"));
                            remove.addActionListener(e -> {
                                if (JOptionPane.showConfirmDialog(JByteCustom.instance, JByteCustom.res.getResource("confirm_remove"),
                                        JByteCustom.res.getResource("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    cn.methods.remove(mn);
                                    model.removeNodeFromParent(stn);
                                }
                            });
                            menu.add(remove);
                            JMenu tools = new JMenu(JByteCustom.res.getResource("tools"));
                            JMenuItem clear = new JMenuItem(JByteCustom.res.getResource("clear"));
                            clear.addActionListener(e -> {
                                if (JOptionPane.showConfirmDialog(JByteCustom.instance, JByteCustom.res.getResource("confirm_clear"), JByteCustom.res.getResource("confirm"),
                                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    MethodUtils.clear(mn);
                                    jbm.selectMethod(cn, mn);
                                }
                            });
                            tools.add(clear);

                            JMenuItem lines = new JMenuItem(JByteCustom.res.getResource("remove_lines"));
                            lines.addActionListener(e -> {
                                if (JOptionPane.showConfirmDialog(JByteCustom.instance, JByteCustom.res.getResource("confirm_lines"), JByteCustom.res.getResource("confirm"),
                                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    MethodUtils.removeLines(mn);
                                    jbm.selectMethod(cn, mn);
                                }
                            });
                            tools.add(lines);
                            JMenuItem deadcode = new JMenuItem(JByteCustom.res.getResource("remove_dead_code"));
                            deadcode.addActionListener(e -> {
                                if (JOptionPane.showConfirmDialog(JByteCustom.instance, JByteCustom.res.getResource("confirm_dead_code"),
                                        JByteCustom.res.getResource("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    MethodUtils.removeDeadCode(cn, mn);
                                    jbm.selectMethod(cn, mn);
                                }
                            });
                            tools.add(deadcode);
                            menu.add(tools);
                            menu.show(ClassTree.this, me.getX(), me.getY());
                        } else if (cn != null) {
                            //class selected
                            JPopupMenu menu = new JPopupMenu();
                            JMenuItem insert = new JMenuItem(JByteCustom.res.getResource("add_method"));
                            insert.addActionListener(e -> {
                                MethodNode mn1 = new MethodNode(1, "", "()V", null, null);
                                mn1.maxLocals = 1;
                                if (new InsnEditDialogue(mn1, mn1).open()) {
                                    if (mn1.name.isEmpty() || mn1.desc.isEmpty()) {
                                        ErrorDisplay.error("Method name / desc cannot be empty");
                                        return;
                                    }
                                    cn.methods.add(mn1);
                                    model.insertNodeInto(new SortedTreeNode(cn, mn1), stn, 0);
                                }
                            });
                            menu.add(insert);

                            JMenuItem edit = new JMenuItem(JByteCustom.res.getResource("edit"));
                            edit.addActionListener(e -> {
                                if (new InsnEditDialogue(mn, cn).open()) {
                                    jbm.refreshTree();
                                }
                            });
                            menu.add(edit);
                            JMenu tools = new JMenu(JByteCustom.res.getResource("tools"));
                            JMenuItem frames = new JMenuItem(JByteCustom.res.getResource("generate_frames"));
                            frames.addActionListener(e -> FrameGen.regenerateFrames(jbm, cn));
                            JMenuItem remove = new JMenuItem(JByteCustom.res.getResource("remove"));
                            remove.addActionListener(e -> {
                                if (JOptionPane.showConfirmDialog(JByteCustom.instance, JByteCustom.res.getResource("confirm_remove"),
                                        JByteCustom.res.getResource("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    jbm.getFile().getClasses().remove(cn.name);
                                    TreeNode parent = stn.getParent();
                                    model.removeNodeFromParent(stn);
                                    while (parent != null && !parent.children().hasMoreElements() && parent != model.getRoot()) {
                                        TreeNode par = parent.getParent();
                                        model.removeNodeFromParent((MutableTreeNode) parent);
                                        parent = par;
                                    }
                                }
                            });
                            menu.add(remove);
                            tools.add(frames);
                            menu.add(tools);
                            menu.show(ClassTree.this, me.getX(), me.getY());
                        } else {
                            JPopupMenu menu = new JPopupMenu();
                            JMenuItem remove = new JMenuItem(JByteCustom.res.getResource("remove"));
                            remove.addActionListener(e -> {
                                if (JOptionPane.showConfirmDialog(JByteCustom.instance, JByteCustom.res.getResource("confirm_remove"),
                                        JByteCustom.res.getResource("confirm"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                    TreeNode parent = stn.getParent();
                                    deleteItselfAndChild(stn);
                                    while (parent != null && !parent.children().hasMoreElements() && parent != model.getRoot()) {
                                        TreeNode par = parent.getParent();
                                        model.removeNodeFromParent((MutableTreeNode) parent);
                                        parent = par;
                                    }
                                }
                            });
                            menu.add(remove);
                            JMenuItem add = new JMenuItem(JByteCustom.res.getResource("add"));
                            add.addActionListener(e -> {
                                ClassNode cn1 = new ClassNode();
                                cn1.version = 52;
                                cn1.name = getPath(stn);
                                cn1.superName = "java/lang/Object";
                                if (new InsnEditDialogue(mn, cn1).open()) {
                                    jbm.getFile().getClasses().put(cn1.name, cn1);
                                    jbm.refreshTree();
                                }
                            });
                            menu.add(add);
                            menu.show(ClassTree.this, me.getX(), me.getY());
                        }
                    }
                } else {
                    TreePath tp = ClassTree.this.getPathForLocation(me.getX(), me.getY());
                    if (tp != null && tp.getParentPath() != null) {
                        ClassTree.this.setSelectionPath(tp);
                        if (ClassTree.this.getLastSelectedPathComponent() == null) {
                            return;
                        }
                        SortedTreeNode stn = (SortedTreeNode) ClassTree.this.getLastSelectedPathComponent();
                        if (stn.getMn() == null && stn.getCn() == null) {
                            if (ClassTree.this.isExpanded(tp)) {
                                ClassTree.this.collapsePath(tp);
                            } else {
                                ClassTree.this.expandPath(tp);
                            }
                        }
                    }
                }
            }
        });
    }

    private String getPath(SortedTreeNode stn) {
        StringBuilder path = new StringBuilder();
        while (stn != null && stn != model.getRoot()) {
            path.insert(0, stn + "/");
            stn = (SortedTreeNode) stn.getParent();
        }
        return path.toString();
    }

    private void sort(DefaultTreeModel model, SortedTreeNode node, boolean sm) {
        if (!node.isLeaf() && (sm || (!node.toString().endsWith(".class")))) {
            node.sort();
            for (int i = 0; i < model.getChildCount(node); i++) {
                SortedTreeNode child = ((SortedTreeNode) model.getChild(node, i));
                sort(model, child, sm);
            }
        }
    }

    @Override
    public void preLoadJars(int id) {

    }

    @Override
    public void onJarLoad(int id, File input) {
        jbm.loadFile(input);
    }

    public void refreshMethod(ClassNode cn, MethodNode mn) {
        changedChild((TreeNode) model.getRoot());
    }

    public void changedChild(TreeNode node) {
        model.nodeChanged(node);
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                changedChild(n);
            }
        }
    }

    public void deleteItselfAndChild(SortedTreeNode node) {
        if (node.getChildCount() >= 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                deleteItselfAndChild((SortedTreeNode) n);
            }
        }
        if (node.getCn() != null)
            jbm.getFile().getClasses().remove(node.getCn().name);
        model.removeNodeFromParent(node);
    }

    public void collapseAll() {
        expandedNodes.clear();
        JByteCustom.instance.refreshTree();
    }
}