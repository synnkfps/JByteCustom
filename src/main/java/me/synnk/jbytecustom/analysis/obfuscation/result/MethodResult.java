package me.synnk.jbytecustom.analysis.obfuscation.result;

import me.synnk.jbytecustom.analysis.obfuscation.enums.MethodObfType;

import java.util.ArrayList;

public class MethodResult {
    public ArrayList<MethodObfType> mobf;

    public MethodResult(ArrayList<MethodObfType> mobf) {
        super();
        this.mobf = mobf;
    }

}
