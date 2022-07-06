package me.synnk.jbytecustom.discord;

import club.minnced.discord.rpc.*;
import me.synnk.jbytecustom.JByteCustom;

public class Discord {
    public static DiscordRPC discordRPC;
    public static long startTimestamp;
    public static DiscordRichPresence presence = new DiscordRichPresence();

    public static String currentDecompiler = "Not Decompiling";

    public static void init() {
        discordRPC = DiscordRPC.INSTANCE;
        String applicationId = "905625817822924830";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> JByteCustom.LOGGER.log("Discord is now ready.");
        discordRPC.Discord_Initialize(applicationId, handlers, true, "");
        startTimestamp = System.currentTimeMillis();
        updatePresence("", "Idle...");
        updateDecompiler(currentDecompiler);
    }

    public static void updatePresence(String details, String state) {
        presence.details = details;
        presence.largeImageKey = "big";
        presence.largeImageText = "Java " + System.getProperty("java.version");
        presence.joinSecret = "java";
        presence.smallImageKey = "decompiler";
        presence.startTimestamp = startTimestamp;
        if (!state.equals("") && JByteCustom.ops.get("discord_state").getBoolean()) {
            presence.state = state;
        }
        discordRPC.Discord_UpdatePresence(presence);
    }

    public static void updateDecompiler(String decompiler){
        presence.smallImageText = "Using " + decompiler + " Decompiler";
        refresh();
    }

    public static void updateCustomState(String state) {
        presence.state = state;
        refresh();
    }
    public static void updateCustomDetails(String details) {
        presence.details = details;
        refresh();
    }
    public static void refresh(){
        discordRPC.Discord_UpdatePresence(presence);
    }
}