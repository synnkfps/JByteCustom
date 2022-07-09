package me.synnk.jbytecustom.ui

import me.lpk.util.drop.IDropUser
import me.lpk.util.drop.JarDropHandler
import me.synnk.jbytecustom.JByteCustom
import me.synnk.jbytecustom.JarArchive
import me.synnk.jbytecustom.ui.dialogue.InsnEditDialogue
import me.synnk.jbytecustom.ui.tree.SortedTreeNode
import me.synnk.jbytecustom.utils.ErrorDisplay
import me.synnk.jbytecustom.utils.MethodUtils
import me.synnk.jbytecustom.utils.asm.FrameGen
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.*
import javax.swing.event.TreeSelectionListener
import javax.swing.tree.*

class ClassTree(private val jbm: JByteCustom) : JTree(), IDropUser {
    private val model: DefaultTreeModel
    private var preloadMap: HashMap<String, SortedTreeNode?>? = null

    init {
        this.isRootVisible = false
        setShowsRootHandles(true)
        setCellRenderer(TreeCellRenderer())
        addTreeSelectionListener(TreeSelectionListener {
            val node = this@ClassTree.lastSelectedPathComponent as SortedTreeNode ?: return@TreeSelectionListener
            if (node.cn != null && node.mn != null) {
                jbm.selectMethod(node.cn, node.mn)
            } else if (node.cn != null) {
                jbm.selectClass(node.cn)
            } else {
            }
        })
        model = DefaultTreeModel(SortedTreeNode(""))
        setModel(model)
        this.transferHandler = JarDropHandler(this, 0)
        getSelectionModel().selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
    }

    fun refreshTree(jar: JarArchive) {
        val tm = model
        val root = tm.root as SortedTreeNode
        root.removeAllChildren()
        tm.reload()
        preloadMap = HashMap()
        if (jar.classes != null) for (c in jar.classes.values) {
            var name = c.name
            val path = array_unique(name.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
            name = java.lang.String.join("/", *path)
            var i = 0
            var slashIndex = 0
            var prev: SortedTreeNode? = root
            while (true) {
                slashIndex = name.indexOf("/", slashIndex + 1)
                if (slashIndex == -1) {
                    break
                }
                val p = name.substring(0, slashIndex)
                if (preloadMap!!.containsKey(p)) {
                    prev = preloadMap!![p]
                } else {
                    try {
                        val stn = SortedTreeNode(path[i])
                        prev!!.add(stn)
                        prev = stn
                        preloadMap!![p] = prev
                    } catch (ex: ArrayIndexOutOfBoundsException) {
                        JByteCustom.LOGGER.println("Failed to load " + path[i])
                    }
                }
                i++
            }
            val clazz = SortedTreeNode(c)
            prev!!.add(clazz)

            // for each method in c.methods
            for (m in c.methods) {
                clazz.add(SortedTreeNode(c, m))
            }
        }
        val sort = JByteCustom.ops["sort_methods"].boolean
        sort(tm, root, sort)
        tm.reload()
        addListener()
        if (!expandedNodes.isEmpty()) {
            expandSaved(root)
        }
    }

    fun expandSaved(node: SortedTreeNode) {
        val tp = TreePath(node.path)
        if (node.cn != null && expandedNodes.contains(node.cn)) {
            super.expandPath(tp)
        }
        if (expandedNodes.contains(tp.toString())) {
            super.expandPath(tp)
        }
        if (node.childCount >= 0) {
            val e = node.children()
            while (e.hasMoreElements()) {
                val n = e.nextElement() as SortedTreeNode
                expandSaved(n)
            }
        }
    }

    override fun expandPath(path: TreePath) {
        val stn = path.lastPathComponent as SortedTreeNode
        if (stn.cn != null) {
            expandedNodes.add(stn.cn)
        } else {
            expandedNodes.add(path.toString())
        }
        super.expandPath(path)
    }

    override fun collapsePath(path: TreePath) {
        val stn = path.lastPathComponent as SortedTreeNode
        if (stn.cn != null) {
            expandedNodes.remove(stn.cn)
        } else {
            expandedNodes.remove(path.toString())
        }
        super.collapsePath(path)
    }

    private fun addListener() {
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(me: MouseEvent) {
                if (SwingUtilities.isRightMouseButton(me)) {
                    val tp = getPathForLocation(me.x, me.y)
                    if (tp != null && tp.parentPath != null) {
                        this@ClassTree.selectionPath = tp
                        if (this@ClassTree.lastSelectedPathComponent == null) {
                            return
                        }
                        val stn = this@ClassTree.lastSelectedPathComponent as SortedTreeNode
                        val mn = stn.mn
                        val cn = stn.cn
                        if (mn != null) {
                            //method selected
                            val menu = JPopupMenu()
                            val edit = JMenuItem(JByteCustom.res.getResource("edit"))
                            edit.addActionListener {
                                InsnEditDialogue(mn, mn).open()
                                changedChilds(model.root as TreeNode)
                            }
                            menu.add(edit)
                            val duplicate = JMenuItem(JByteCustom.res.getResource("duplicate"))
                            duplicate.addActionListener(ActionListener {
                                val dup = MethodUtils.copy(mn)
                                val name = JOptionPane.showInputDialog(
                                    null,
                                    "Duplicated method name ?",
                                    "Rename",
                                    JOptionPane.QUESTION_MESSAGE
                                )
                                if (MethodUtils.equalName(cn, name)) {
                                    JOptionPane.showMessageDialog(
                                        null,
                                        "The name is already existed.",
                                        "Existed Name!",
                                        JOptionPane.WARNING_MESSAGE
                                    )
                                    return@ActionListener
                                }
                                dup.name = name
                                cn!!.methods.add(dup)
                                jbm.jarTree.refreshTree(jbm.file)
                            })
                            menu.add(duplicate)
                            val search = JMenuItem(JByteCustom.res.getResource("search"))
                            search.addActionListener {
                                jbm.searchList.searchForFMInsn(
                                    cn!!.name,
                                    mn.name,
                                    mn.desc,
                                    false,
                                    false
                                )
                            }
                            menu.add(search)
                            val remove = JMenuItem(JByteCustom.res.getResource("remove"))
                            remove.addActionListener {
                                if (JOptionPane.showConfirmDialog(
                                        JByteCustom.instance, JByteCustom.res.getResource("confirm_remove"),
                                        JByteCustom.res.getResource("confirm"), JOptionPane.YES_NO_OPTION
                                    ) == JOptionPane.YES_OPTION
                                ) {
                                    cn!!.methods.remove(mn)
                                    model.removeNodeFromParent(stn)
                                }
                            }
                            menu.add(remove)
                            val tools = JMenu(JByteCustom.res.getResource("tools"))
                            val clear = JMenuItem(JByteCustom.res.getResource("clear"))
                            clear.addActionListener {
                                if (JOptionPane.showConfirmDialog(
                                        JByteCustom.instance,
                                        JByteCustom.res.getResource("confirm_clear"),
                                        JByteCustom.res.getResource("confirm"),
                                        JOptionPane.YES_NO_OPTION
                                    ) == JOptionPane.YES_OPTION
                                ) {
                                    MethodUtils.clear(mn)
                                    jbm.selectMethod(cn, mn)
                                }
                            }
                            tools.add(clear)
                            val lines = JMenuItem(JByteCustom.res.getResource("remove_lines"))
                            lines.addActionListener {
                                if (JOptionPane.showConfirmDialog(
                                        JByteCustom.instance,
                                        JByteCustom.res.getResource("confirm_lines"),
                                        JByteCustom.res.getResource("confirm"),
                                        JOptionPane.YES_NO_OPTION
                                    ) == JOptionPane.YES_OPTION
                                ) {
                                    MethodUtils.removeLines(mn)
                                    jbm.selectMethod(cn, mn)
                                }
                            }
                            tools.add(lines)
                            val deadcode = JMenuItem(JByteCustom.res.getResource("remove_dead_code"))
                            deadcode.addActionListener {
                                if (JOptionPane.showConfirmDialog(
                                        JByteCustom.instance, JByteCustom.res.getResource("confirm_dead_code"),
                                        JByteCustom.res.getResource("confirm"), JOptionPane.YES_NO_OPTION
                                    ) == JOptionPane.YES_OPTION
                                ) {
                                    MethodUtils.removeDeadCode(cn, mn)
                                    jbm.selectMethod(cn, mn)
                                }
                            }
                            tools.add(deadcode)
                            menu.add(tools)
                            menu.show(this@ClassTree, me.x, me.y)
                        } else if (cn != null) {
                            //class selected
                            val menu = JPopupMenu()
                            val insert = JMenuItem(JByteCustom.res.getResource("add_method"))
                            insert.addActionListener(ActionListener {
                                val mn = MethodNode(1, "", "()V", null, null)
                                mn.maxLocals = 1
                                if (InsnEditDialogue(mn, mn).open()) {
                                    if (mn.name.isEmpty() || mn.desc.isEmpty()) {
                                        ErrorDisplay.error("Method name / desc cannot be empty")
                                        return@ActionListener
                                    }
                                    cn.methods.add(mn)
                                    model.insertNodeInto(SortedTreeNode(cn, mn), stn, 0)
                                }
                            })
                            menu.add(insert)
                            val edit = JMenuItem(JByteCustom.res.getResource("edit"))
                            edit.addActionListener {
                                if (InsnEditDialogue(mn, cn).open()) {
                                    jbm.refreshTree()
                                }
                            }
                            menu.add(edit)
                            val tools = JMenu(JByteCustom.res.getResource("tools"))
                            val frames = JMenuItem(JByteCustom.res.getResource("generate_frames"))
                            frames.addActionListener { FrameGen.regenerateFrames(jbm, cn) }
                            val remove = JMenuItem(JByteCustom.res.getResource("remove"))
                            remove.addActionListener {
                                if (JOptionPane.showConfirmDialog(
                                        JByteCustom.instance, JByteCustom.res.getResource("confirm_remove"),
                                        JByteCustom.res.getResource("confirm"), JOptionPane.YES_NO_OPTION
                                    ) == JOptionPane.YES_OPTION
                                ) {
                                    jbm.file.classes.remove(cn.name)
                                    var parent = stn.parent
                                    model.removeNodeFromParent(stn)
                                    while (parent != null && !parent.children()
                                            .hasMoreElements() && parent !== model.root
                                    ) {
                                        val par = parent.parent
                                        model.removeNodeFromParent(parent as MutableTreeNode)
                                        parent = par
                                    }
                                }
                            }
                            menu.add(remove)
                            tools.add(frames)
                            menu.add(tools)
                            menu.show(this@ClassTree, me.x, me.y)
                        } else {
                            val menu = JPopupMenu()
                            val remove = JMenuItem(JByteCustom.res.getResource("remove"))
                            remove.addActionListener {
                                if (JOptionPane.showConfirmDialog(
                                        JByteCustom.instance, JByteCustom.res.getResource("confirm_remove"),
                                        JByteCustom.res.getResource("confirm"), JOptionPane.YES_NO_OPTION
                                    ) == JOptionPane.YES_OPTION
                                ) {
                                    var parent = stn.parent
                                    deleteItselfAndChilds(stn)
                                    while (parent != null && !parent!!.children()
                                            .hasMoreElements() && parent !== model.root
                                    ) {
                                        val par = parent!!.parent
                                        model.removeNodeFromParent(parent as MutableTreeNode)
                                        parent = par
                                    }
                                }
                            }
                            menu.add(remove)
                            val add = JMenuItem(JByteCustom.res.getResource("add"))
                            add.addActionListener {
                                val cn = ClassNode()
                                cn.version = 52
                                cn.name = getPath(stn)
                                cn.superName = "java/lang/Object"
                                if (InsnEditDialogue(mn, cn).open()) {
                                    jbm.file.classes[cn.name] = cn
                                    jbm.refreshTree()
                                }
                            }
                            menu.add(add)
                            menu.show(this@ClassTree, me.x, me.y)
                        }
                    }
                } else {
                    val tp = getPathForLocation(me.x, me.y)
                    if (tp != null && tp.parentPath != null) {
                        this@ClassTree.selectionPath = tp
                        if (this@ClassTree.lastSelectedPathComponent == null) {
                            return
                        }
                        val stn = this@ClassTree.lastSelectedPathComponent as SortedTreeNode
                        if (stn.mn == null && stn.cn == null) {
                            if (this@ClassTree.isExpanded(tp)) {
                                collapsePath(tp)
                            } else {
                                expandPath(tp)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun getPath(stn: SortedTreeNode): String {
        var stn: SortedTreeNode? = stn
        var path = ""
        while (stn != null && stn !== model.root) {
            path = "$stn/$path"
            stn = stn.parent as SortedTreeNode
        }
        return path
    }

    private fun sort(model: DefaultTreeModel, node: SortedTreeNode, sm: Boolean) {
        if (!node.isLeaf && (if (sm) true else !node.toString().endsWith(".class"))) {
            node.sort()
            for (i in 0 until model.getChildCount(node)) {
                val child = model.getChild(node, i) as SortedTreeNode
                sort(model, child, sm)
            }
        }
    }

    override fun preLoadJars(id: Int) {}
    override fun onJarLoad(id: Int, input: File) {
        jbm.loadFile(input)
    }

    fun refreshMethod(cn: ClassNode?, mn: MethodNode?) {
        changedChilds(model.root as TreeNode)
    }

    fun changedChilds(node: TreeNode) {
        model.nodeChanged(node)
        if (node.childCount >= 0) {
            val e = node.children()
            while (e.hasMoreElements()) {
                val n = e.nextElement() as TreeNode
                changedChilds(n)
            }
        }
    }

    fun deleteItselfAndChilds(node: SortedTreeNode) {
        if (node.childCount >= 0) {
            val e = node.children()
            while (e.hasMoreElements()) {
                val n = e.nextElement() as TreeNode
                deleteItselfAndChilds(n as SortedTreeNode)
            }
        }
        if (node.cn != null) jbm.file.classes.remove(node.cn.name)
        model.removeNodeFromParent(node)
    }

    fun collapseAll() {
        expandedNodes.clear()
        JByteCustom.instance.refreshTree()
    }

    companion object {
        private val expandedNodes = ArrayList<Any>()
        fun array_unique(ss: Array<String>): Array<String> {
            // array_unique
            val list: MutableList<String> = ArrayList()
            for (s in ss) {
                if (!list.contains(s)) //或者list.indexOf(s)!=-1
                    list.add(s)
            }
            return list.toTypedArray()
        }
    }
}