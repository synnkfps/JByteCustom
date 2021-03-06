package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.analysis.decompiler.code.ast.VarType;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;

public class VarLoadExpression extends Expression {
    private int index;
    private VarType type;

    public VarLoadExpression(int index, VarType type) {
        super();
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public VarType getType() {
        return type;
    }

    public void setType(VarType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return TextUtils.addTag("var" + index, "font color=" + InstrUtils.primColor.getString()); //TODO use localvartable
    }

    @Override
    public int size() {
        return type.size();
    }

    @Override
    public Expression clone() {
        return new VarLoadExpression(index, type);
    }
}
