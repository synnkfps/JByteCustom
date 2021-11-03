package de.xbrowniecodez.jbytemod.securitymanager;

import java.net.SocketTimeoutException;
import java.security.Permission;

import me.grax.jbytemod.JByteMod;

public class CustomSecurityManager extends SecurityManager{
	public CustomSecurityManager() {
		enable();
	}

	public void enable() {
		JByteMod.LOGGER.log("[SecurityManager] Trying to set SecurityManager...");
		System.setSecurityManager(this);
		JByteMod.LOGGER.log("[SecurityManager] Successfully set the SecurityManager");
	}

	@Override
	public void checkConnect(String host, int port) {
		JByteMod.LOGGER.log(String.format("[Security Manager] Blocked a connection to %s:%s", host, port));
		SneakyThrow.sneakyThrow(new SocketTimeoutException("[SecurityManager] Connection blocked."));
		

	}

	@Override
	public void checkConnect(String host, int port, Object context) {
		checkConnect(host, port);
	}

	@Override
	public void checkPermission(Permission perm) {
		String name = perm.getName();
		if (name == null) {
			return;
		}
		if (name.equals("setSecurityManager")) {
			JByteMod.LOGGER.err("[SecurityManager] Error while setting SecurityManager, JByteMod-Remastered will now exit for your own safety.");
			System.exit(0);
		}
	}

	@Override
	public void checkPermission(Permission perm, Object context) {
		checkPermission(perm);
	}
}
