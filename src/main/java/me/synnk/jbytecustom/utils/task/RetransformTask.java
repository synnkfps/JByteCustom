package me.synnk.jbytecustom.utils.task;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.JarArchive;
import me.synnk.jbytecustom.ui.PageEndPanel;
import me.synnk.jbytecustom.utils.ErrorDisplay;
import me.lpk.util.ASMUtils;
import org.objectweb.asm.tree.ClassNode;

import javax.swing.*;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.Map.Entry;

public class RetransformTask extends SwingWorker<Void, Integer> {

    private Instrumentation ins;
    private PageEndPanel jpb;
    private JarArchive file;

    public RetransformTask(JByteCustom jbm, Instrumentation ins, JarArchive file) {
        this.ins = ins;
        this.file = file;
        this.jpb = jbm.getPP();
    }

    @Override
    protected Void doInBackground() throws Exception {
        publish(0);
        try {
            Map<String, ClassNode> classes = file.getClasses();
            Map<String, byte[]> original = file.getOutput();
            Map<String, byte[]> newOriginal = new HashMap<>();

            ArrayList<ClassDefinition> definitions = new ArrayList<>();
            double size = classes.size();
            if (size == 0) {
                publish(100);
                return null;
            }
            int i = 0;
            for (Entry<String, ClassNode> e : classes.entrySet()) {
                publish((int) ((i / size) * 80d));
                byte[] originalBytes = original.get(e.getKey());
                byte[] bytes = ASMUtils.getNodeBytes0(e.getValue());
                //probably not the best solution but whatever
                if (!Arrays.equals(bytes, originalBytes)) {
                    definitions.add(new ClassDefinition(ClassLoader.getSystemClassLoader().loadClass(e.getKey().replace('/', '.')), bytes));
                    newOriginal.put(e.getKey(), bytes);
                }
                i++;
            }
            if (!definitions.isEmpty()) {
                publish(80);
                ins.redefineClasses(definitions.toArray(new ClassDefinition[0]));
                JByteCustom.LOGGER.log("Successfully retransformed " + newOriginal.size() + " classes");
                original.putAll(newOriginal);
            }
        } catch (VerifyError v) {
            JOptionPane.showMessageDialog(null, JByteCustom.res.getResource("verify_error"));
        } catch (Throwable t) {
            new ErrorDisplay(t);
            t.printStackTrace();
        }
        publish(100);
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        int i = chunks.get(chunks.size() - 1);
        jpb.setValue(i);
        super.process(chunks);
    }
}
