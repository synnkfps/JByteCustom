package me.synnk.jbytecustom.analysis.obfuscation.result;

import me.synnk.jbytecustom.analysis.obfuscation.enums.NameObfType;

import java.util.ArrayList;

public class NamesResult {
    public ArrayList<NameObfType> cnames;
    public ArrayList<NameObfType> mnames;
    public ArrayList<NameObfType> fnames;

    public NamesResult(ArrayList<NameObfType> cnames, ArrayList<NameObfType> mnames, ArrayList<NameObfType> fnames) {
        super();
        this.cnames = cnames;
        this.mnames = mnames;
        this.fnames = fnames;
    }

}
