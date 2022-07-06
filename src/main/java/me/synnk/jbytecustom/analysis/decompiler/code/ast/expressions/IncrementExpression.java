package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;

public class IncrementExpression extends Expression {
    private Expression object;
    private int increment;

    public IncrementExpression(Expression object, int increment) {
        super();
        this.object = object;
        this.increment = increment;
    }

    public Expression getObject() {
        return object;
    }

    public void setObject(Expression object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return TextUtils.addTag(object.toString(), "font color=" + InstrUtils.primColor.getString()) + " += " + increment;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Expression clone() {
        return new IncrementExpression(object.clone(), increment);
    }
}
