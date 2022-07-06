package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.analysis.decompiler.defs.Keywords;

public class NullExpression extends Expression {

    @Override
    public String toString() {
        return "<b>" + Keywords.NULL + "</b>";
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Expression clone() {
        return new NullExpression();
    }

}
