package me.synnk.jbytecustom.utils.task.search;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.ui.PageEndPanel;
import me.synnk.jbytecustom.ui.lists.SearchList;
import me.synnk.jbytecustom.ui.lists.entries.SearchEntry;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;
import me.synnk.jbytecustom.utils.list.LazyListModel;
import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

public class SFTask extends SwingWorker<Void, Integer> {

    private PageEndPanel jpb;
    private JByteCustom jbm;
    private String sf;
    private SearchList sl;

    public SFTask(SearchList sl, JByteCustom jbm, String sf) {
        this.sl = sl;
        this.jbm = jbm;
        this.jpb = jbm.getPP();
        this.sf = sf;
    }

    @Override
    protected Void doInBackground() throws Exception {
        LazyListModel<SearchEntry> model = new LazyListModel<>();
        Collection<ClassNode> values = jbm.getFile().getClasses().values();
        double size = values.size();
        double i = 0;
        for (ClassNode cn : values) {
            if (cn.sourceFile != null && cn.sourceFile.contains(sf)) {
                SearchEntry se = new SearchEntry(cn, cn.methods.get(0), TextUtils.escape(TextUtils.max(cn.sourceFile, 100)));
                se.setText(TextUtils.toHtml(InstrUtils.getDisplayClass(cn.name) + " - " + cn.sourceFile));
                model.addElement(se);
            }

            publish(Math.min((int) ((i++ / size) * 100d) + 1, 100));
        }
        sl.setModel(model);
        publish(100);
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        int i = chunks.get(chunks.size() - 1);
        jpb.setValue(i);
        super.process(chunks);
    }

    @Override
    protected void done() {
        JByteCustom.LOGGER.log("Search finished!");
    }
}