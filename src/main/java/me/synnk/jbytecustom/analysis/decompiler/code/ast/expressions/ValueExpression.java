package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.analysis.decompiler.code.ast.VarType;

public class ValueExpression extends Expression {
    private VarType type;
    private Object value;

    public ValueExpression(VarType type, Object value) {
        super();
        this.type = type;
        this.value = value;
    }

    private String getSub() {
        switch (type) {
            case OBJECT:
            case INT:
                return "";
            case DOUBLE:
                return "d";
            case FLOAT:
                return "f";
            case LONG:
                return "L";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return value.toString() + getSub();
    }

    @Override
    public int size() {
        return type.size();
    }

    @Override
    public Expression clone() {
        return new ValueExpression(type, value);
    }
}
