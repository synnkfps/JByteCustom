package me.grax.jbytemod.decompiler;

public enum Decompilers {
    CFR("CFR", "v1.52"),
    PROCYON("Procyon", "v1.0"),
    FERNFLOWER("FernFlower", ""),
    KRAKATAU("Krakatau", "v502"),
    KOFFEE("Koffee", "");

    private String version;
    private String name;

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
