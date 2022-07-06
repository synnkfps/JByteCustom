package me.synnk.jbytecustom.utils.task;

import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.JarArchive;
import me.synnk.jbytecustom.ui.PageEndPanel;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.objectweb.asm.tree.ClassNode;

import de.xbrowniecodez.jbytemod.asm.CustomClassWriter;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class SaveTask extends SwingWorker<Void, Integer> {

    private File output;
    private PageEndPanel jpb;
    private JarArchive file;

    public SaveTask(JByteCustom jbm, File output, JarArchive file) {
        this.output = output;
        this.file = file;
        this.jpb = jbm.getPP();
    }

    @Override
    protected Void doInBackground() throws Exception {
        synchronized (this.file) {
            try {
                Map<String, ClassNode> classes = this.file.getClasses();
                Map<String, byte[]> outputBytes = this.file.getOutput();
                int flags = JByteCustom.ops.get("compute_maxs").getBoolean() ? 1 : 0;
                JByteCustom.LOGGER.log("Writing..");
                if (this.file.isSingleEntry()) {
                    ClassNode node = classes.values().iterator().next();
                    CustomClassWriter writer = new CustomClassWriter(flags);
                    node.accept(writer);
                    publish(50);
                    JByteCustom.LOGGER.log("Saving..");
                    Files.write(this.output.toPath(), writer.toByteArray());
                    publish(100);
                    JByteCustom.LOGGER.log("Saving successful!");
                    return null;
                }

                publish(0);
                double size = classes.keySet().size();
                double i = 0;
                for (String s : classes.keySet()) {
                    try{
                        ClassNode node = classes.get(s);
                        CustomClassWriter writer = new CustomClassWriter(flags);
                        node.accept(writer);
                        outputBytes.put(s + ".class", writer.toByteArray());
                        publish((int) ((i++ / size) * 50d));
                    }catch(StringIndexOutOfBoundsException exception) {
                        JByteCustom.LOGGER.println("Failed to save " + classes.get(s).name);
                    }
                }
                publish(50);
                JByteCustom.LOGGER.log("Saving..");
                this.saveAsJarNew(outputBytes, output.getAbsolutePath());
                JByteCustom.LOGGER.log("Saving successful!");
            } catch (Exception e) {
                e.printStackTrace();
                JByteCustom.LOGGER.log("Saving failed!");
            }
            publish(100);
            return null;
        }
    }

    public void saveAsJarNew(Map<String, byte[]> outBytes, String fileName) {
        try {
            ZipOutputStream out = new ZipOutputStream(new java.io.FileOutputStream(fileName));
            out.setEncoding("UTF-8");
            for (String entry : outBytes.keySet()) {
                out.putNextEntry(new ZipEntry(entry));
                if (!entry.endsWith("/") || !entry.endsWith("\\"))
                    out.write(outBytes.get(entry));
                out.closeEntry();
            }
            if (out != null) {
                out.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void process(List<Integer> chunks) {
        int i = chunks.get(chunks.size() - 1);
        jpb.setValue(i);
        super.process(chunks);
    }

}