package me.grax.jbytemod.utils.gui;

import com.formdev.flatlaf.FlatIntelliJLaf;
import me.grax.jbytemod.JByteMod;
import me.grax.jbytemod.utils.ThemeChanges;

import javax.swing.*;

public class LookUtils {
    public static void setLAF() {
        try {
            JByteMod.LOGGER.log("Setting default Look and Feel");
            FlatIntelliJLaf.setup();

        } catch (Throwable t) {
            t.printStackTrace();
            JByteMod.LOGGER.err("Failed to set Look and Feel");
        }
    }

    public static boolean changeLAF(String name) {
        try {
            JByteMod.LOGGER.log("Changing UI to " + name);
            UIManager.setLookAndFeel(name);
            UIManager.getLookAndFeel().uninitialize();
            UIManager.setLookAndFeel(name);
            if (name.equals("javax.swing.plaf.nimbus.NimbusLookAndFeel")) {
                ThemeChanges.setDefaults();
            }
            if (JByteMod.instance != null) {
                JByteMod.restartGUI();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
