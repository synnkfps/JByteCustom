package me.grax.jbytemod;

import me.grax.jbytemod.discord.Discord;
import me.grax.jbytemod.ui.RPCFrame;
import org.objectweb.asm.tree.ClassNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomRPC {
    public static boolean isCustom;
    public static String customStatus;
    public static String details_text = "";
    public static String state_text = "";
    public static String details_default = "Working on" + JByteMod.lastEditFile;
    public static String reg = "";

    static {
        customStatus = "Idle...";
    }

    public static void checkIfIsCustom(ClassNode cn) {
        String regex = cn.name.toString();
        reg = regex;
        final Pattern pattern = Pattern.compile(".+/", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(regex);

        if (isCustom && !RPCFrame.fieldState.getText().isEmpty()) {
            Discord.presence.state = RPCFrame.fieldState.getName();
            Discord.refresh();
        }

        if (isCustom && !RPCFrame.fieldDetails.getText().isEmpty()) {
            Discord.presence.details = RPCFrame.fieldDetails.getName();
            Discord.refresh();
        }

        if (!isCustom){
            Discord.updatePresence("Working on" + JByteMod.lastEditFile, "Editing " + regex.replaceAll(".+/", "") + ".class");
            Discord.refresh();
        }

    }
}
