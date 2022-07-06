package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.analysis.decompiler.code.ast.VarType;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;

public class VarAssignExpression extends Expression {
    private int index;
    private VarType type;
    private Expression value;

    public VarAssignExpression(int index, VarType type, Expression value) {
        super();
        this.index = index;
        this.type = type;
        this.value = value;
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

    public Expression getValue() {
        return value;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type.getType());
        sb.append(" ");
        sb.append(TextUtils.addTag("var" + index, "font color=" + InstrUtils.primColor.getString()));
        sb.append(" = ");
        sb.append(value.toString());
        return sb.toString();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Expression clone() {
        return new VarAssignExpression(index, type, value.clone());
    }
}
