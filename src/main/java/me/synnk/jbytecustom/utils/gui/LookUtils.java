package me.synnk.jbytecustom.utils.gui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import me.synnk.jbytecustom.JByteCustom;

import javax.swing.*;

public class LookUtils {
    public static void setLAF() {
        try {
            JByteCustom.LOGGER.log("Setting default Look and Feel");
            FlatIntelliJLaf.setup();

        } catch (Throwable t) {
            t.printStackTrace();
            JByteCustom.LOGGER.err("Failed to set Look and Feel");
        }
    }

    public static boolean changeLAF(String name) {
        try {
            JByteCustom.LOGGER.log("Changing UI to " + name);
            UIManager.setLookAndFeel(name);
            UIManager.getLookAndFeel().uninitialize();
            UIManager.setLookAndFeel(name);
            if (JByteCustom.instance != null) {
                JByteCustom.restartGUI();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
