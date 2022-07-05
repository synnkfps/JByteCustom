package me.grax.jbytemod.decompiler;

public enum Decompilers {
    CFR("CFR", "v1.51"),
    PROCYON("Procyon", "v1.0"),
    FERNFLOWER("FernFlower", ""),
    KRAKATAU("Krakatau", "v502"),
    KOFFEE("Koffee", "");


    private final String version;
    private final String name;

    Decompilers(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " " + version;
    }
}
