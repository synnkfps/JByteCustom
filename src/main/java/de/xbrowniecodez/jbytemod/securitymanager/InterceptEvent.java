package de.xbrowniecodez.jbytemod.securitymanager;

public class InterceptEvent {
    private final String host;
    private final StackTraceElement[] stackTrace;

    public InterceptEvent(String host, StackTraceElement[] stackTrace) {
        this.host = host;
        this.stackTrace = stackTrace;
    }

    public String getHost() {
        return this.host;
    }

    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }
}
