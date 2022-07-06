package me.synnk.jbytecustom.utils.task;

import me.synnk.jbytecustom.CustomRPC;
import me.synnk.jbytecustom.JByteCustom;
import me.synnk.jbytecustom.JarArchive;
import me.synnk.jbytecustom.discord.Discord;
import me.synnk.jbytecustom.scanner.ScannerThread;
import me.synnk.jbytecustom.ui.PageEndPanel;
import me.synnk.jbytecustom.utils.ErrorDisplay;
import me.synnk.jbytecustom.utils.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import de.xbrowniecodez.jbytemod.asm.CustomClassReader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadTask extends SwingWorker<Void, Integer> {

    private ZipFile input;
    private PageEndPanel jpb;
    private JByteCustom jbm;
    private File file;
    private int jarSize; // including directories
    private int loaded;
    private JarArchive ja;
    private long maxMem;
    private boolean memoryWarning;
    private long startTime;
    private ScannerThread scannerThread;
    private int othersFile;
    private int junkClasses;

    public LoadTask(JByteCustom jbm, File input, JarArchive ja) {
        try {
            this.othersFile = 0;
            this.startTime = System.currentTimeMillis();
            this.jarSize = countFiles(this.input = new ZipFile(input, "UTF-8"));
            JByteCustom.LOGGER.log(jarSize + " files to load!");
            this.jbm = jbm;
            this.jpb = jbm.getPP();
            this.ja = ja;
            this.file = input;
            // clean old cache
            // ja.setClasses(null);
            this.maxMem = Runtime.getRuntime().maxMemory();
            this.memoryWarning = JByteCustom.ops.get("memory_warning").getBoolean();
        } catch (IOException e) {
            new ErrorDisplay(e);
        }
    }

    public static ClassNode convertToASM(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        CustomClassReader cr = new CustomClassReader(bytes);
        ClassNode cn = new ClassNode();

        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        return cn;
    }

    @Override
    protected Void doInBackground() throws Exception {
        publish(0);
        this.loadFiles(input);
        publish(100);
        return null;
    }

    public int countFiles(final ZipFile zipFile) {
        final Enumeration<ZipEntry> entries = zipFile.getEntries();
        int c = 0;
        while (entries.hasMoreElements()) {
            entries.nextElement();
            ++c;
        }
        return c;
    }

    /**
     * loads both classes and other files at the same time
     */
    public void loadFiles(ZipFile jar) throws IOException {
        long mem = Runtime.getRuntime().totalMemory();
        if (mem / (double) maxMem > 0.75) {
            JByteCustom.LOGGER.warn("Memory usage is high: " + Math.round((mem / (double) maxMem * 100d)) + "%");
        }
        System.gc();
        Map<String, ClassNode> classes = new HashMap<String, ClassNode>();
        Map<String, byte[]> otherFiles = new HashMap<String, byte[]>();

        final Enumeration<ZipEntry> entries = jar.getEntries();
        while (entries.hasMoreElements()) {
            readJar(jar, entries.nextElement(), classes, otherFiles);
        }
        jar.close();
        ja.setClasses(classes);
        ja.setOutput(otherFiles);

        this.othersFile = otherFiles.size();
        for(String name : otherFiles.keySet()){
            if(name.endsWith(".class") || name.endsWith(".class/")) junkClasses++;
        }

        return;
    }

    private void readJar(ZipFile jar, ZipEntry zipEntry, Map<String, ClassNode> classes,
                         Map<String, byte[]> otherFiles) {
        long ms = System.currentTimeMillis();
        publish((int) (((float) loaded++ / (float) jarSize) * 100f));
        String name = zipEntry.getName();
        try (InputStream jis = jar.getInputStream(zipEntry)) {
            byte[] bytes = IOUtils.toByteArray(jis);
            if (name.endsWith(".class") || name.endsWith(".class/")) {
                synchronized (classes) {
                    try {
                        //JByteCustom.LOGGER.log("Class file: " + name + "-" + bytes.length);
                        String cafebabe = String.format("%02X%02X%02X%02X", bytes[0], bytes[1], bytes[2], bytes[3]);
                        if (cafebabe.toLowerCase().equals("cafebabe")) {
                            try {
                                final ClassNode cn = convertToASM(bytes);
                                int rate;
                                if(!JByteCustom.ops.get("bad_class_check").getBoolean()){
                                    rate = 0;
                                }else {
                                    rate = FileUtils.isBadClass(cn);
                                }
                                if (cn != null && rate <= 80) { // && (cn.name.equals("java/lang/Object") ? true : cn.superName != null)
                                    classes.put(cn.name, cn);
                                }else {
                                    synchronized (otherFiles) {
                                        otherFiles.put(name, bytes);
                                    }
                                }
                            } catch (Exception e) {
                                synchronized (otherFiles) {
                                    otherFiles.put(name, bytes);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        synchronized (otherFiles) {
                            otherFiles.put(name, bytes);
                        }
                    }

                }
            }else if(name.equals("META-INF/MANIFEST.MF")) {
                ja.setJarManifest(bytes);
                synchronized (otherFiles) {
                    otherFiles.put(name, bytes);
                }
            }else {
                synchronized (otherFiles) {
                    otherFiles.put(name, bytes);
                }
            }

            if (memoryWarning) {
                long timeDif = System.currentTimeMillis() - ms;
                if (timeDif > 60 * 3 * 1000 && Runtime.getRuntime().totalMemory() / (double) maxMem > 1.95) { // if
                    JByteCustom.LOGGER.logNotification(JByteCustom.res.getResource("memory_full"));
                    publish(100);
                    this.cancel(true);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JByteCustom.LOGGER.err("Failed loading file");
        }
    }

    @Override
    protected void process(List<Integer> chunks) {
        int i = chunks.get(chunks.size() - 1);
        Discord.updateCustomDetails("(" + i + "%) Loading " + file.getName());
        jpb.setValue(i);
        super.process(chunks);
    }

    @Override
    protected void done() {
        JByteCustom.lastEditFile = file.getName();
        CustomRPC.customDetails = "Working on " + file.getName();
        Discord.updateCustomDetails(CustomRPC.customDetails);
        JByteCustom.LOGGER.log("Successfully loaded file!");
        jbm.refreshTree();
        JByteCustom.LOGGER.log("Tree refreshed.");
        JByteCustom.LOGGER.log("Loaded classes in " + (System.currentTimeMillis() - startTime) + "ms" + ", bypassed " + othersFile + " files because I can't load them. (Include " + junkClasses + " junk classes.)");

        if(!JByteCustom.ops.get("auto_scan").getBoolean()) return;

        scannerThread = new ScannerThread(ja.getClasses());
        scannerThread.setJarManifest(ja.getJarManifest());
        scannerThread.start();
    }
}
