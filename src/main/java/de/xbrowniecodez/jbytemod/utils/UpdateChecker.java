package de.xbrowniecodez.jbytemod.utils;

import java.awt.Desktop;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.synnk.jbytecustom.JByteCustom;

public class UpdateChecker {
	public UpdateChecker() throws Exception {
		JByteCustom.LOGGER.log("Checking for updates...");
		String sURL = "https://api.github.com/repos/xBrownieCodezV2/JByteMod-Remastered/releases/latest"; 																										// string
		URL url = new URL(sURL);
		URLConnection request = url.openConnection();
		request.connect();
		JsonParser jp = new JsonParser();
		JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		JsonObject rootobj = root.getAsJsonObject();
		String version = rootobj.get("name").getAsString();
		if (!version.equals(JByteCustom.version)) {
			int buttonComponent = JOptionPane.YES_NO_OPTION;
			int result = JOptionPane.showConfirmDialog(null,
					String.format("Version %s is available, would you like to download it?", version),
					"Update available", buttonComponent);
			if (result == 0) {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					Desktop.getDesktop().browse(new URI(String.format(
							"https://github.com/xBrownieCodezV2/JByteMod-Remastered/releases/download/%s/JByteMod-Remastered-%s.jar",
							version, version)));
				}
			}
		}

	}

}
