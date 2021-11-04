package me.grax.jbytemod.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.grax.jbytemod.JByteMod;
import me.grax.jbytemod.decompiler.Decompilers;
import me.grax.jbytemod.ui.DecompilerPanel;
import me.grax.jbytemod.ui.DecompilerTab;

import javax.swing.*;

public class Discord {
    public static DiscordRPC discordRPC;
    public static long startTimestamp;
    private static final JComboBox<Decompilers> decompilerCombo = new JComboBox<Decompilers>(Decompilers.values());;
    private static Decompilers decompiler = (Decompilers) decompilerCombo.getSelectedItem();
    public static String decompilerUsed = decompiler.toString();
    public static DiscordRichPresence presence = new DiscordRichPresence();
    public static int shittySystem;
    public static String currentDecompiler = "Not Decompiling";

    public static void init() {
        discordRPC = DiscordRPC.INSTANCE;
        String applicationId = "905625817822924830";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> JByteMod.LOGGER.log("Discord is now ready.");
        discordRPC.Discord_Initialize(applicationId, handlers, true, "");
        startTimestamp = System.currentTimeMillis();
        updatePresence("Idling.", "");
        updateDecompiler(currentDecompiler);
    }

    public static void updatePresence(String details, String state) {
        presence.details = details;
        presence.largeImageKey = "big";
        presence.largeImageText = "Java " + System.getProperty("java.version");
        presence.joinSecret = "java";
        presence.smallImageKey = "decompiler";
        presence.startTimestamp = startTimestamp;
        if (!state.equals("") && JByteMod.ops.get("discord_state").getBoolean()) {
            presence.state = state;
        }
        discordRPC.Discord_UpdatePresence(presence);
    }

    public static void updateDecompiler(String decompiler){
        /**
         * @TODO: IF USER IS USING KOFFEE, CHANGE smallImageText TO DISASSEMBLER (INSTEAD OF DECOMPILER).
         */
        if (decompilerUsed.equalsIgnoreCase("Koffee")) {
            presence.smallImageText = "Using " + decompiler + " Disassembler";
        }
        presence.smallImageText = "Using " + decompiler + " Decompiler";
        refresh();
    }

    public static void refresh(){
        discordRPC.Discord_UpdatePresence(presence);
    }
}