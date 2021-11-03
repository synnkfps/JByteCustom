package me.grax.jbytemod.decompiler;

public enum Decompilers {
    CFR("CFR", "1.51"), PROCYON("Procyon", "1.0-SNAPSHOT"), FERNFLOWER("FernFlower", ""), KRAKATAU("Krakatau", "502"), KOFFEE("Koffee", "");
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
