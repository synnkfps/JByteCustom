package me.grax.jbytemod;

import me.grax.jbytemod.discord.Discord;
import me.grax.jbytemod.ui.RPCFrame;
import org.objectweb.asm.tree.ClassNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomRPC {
    public static boolean isCustom;
    public static String customStatus;
    public static boolean checkIfStillTheSame;
    public static boolean isVanilla;

    static {
        customStatus = "Idle";
    }

    public static void checkIfIsCustom(ClassNode cn) {
        String regexed = cn.name.toString();
        final Pattern pattern = Pattern.compile(".+/", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(regexed);

        if (isCustom && !RPCFrame.fieldUsername.getText().isEmpty()) {
            customStatus = RPCFrame.fieldUsername.getName();
            Discord.presence.state = customStatus;
            Discord.refresh();
        }
        if (!isCustom){
            Discord.updatePresence("Working on " + JByteMod.lastEditFile, "Editing " + regexed.replaceAll(".+/", "") + ".class");
            Discord.refresh();
        }

    }
}
