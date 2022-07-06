package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;

public class ArrayStoreExpression extends Expression {
    private Expression array;
    private Expression index;
    private Expression value;

    public ArrayStoreExpression(Expression array, Expression index, Expression value) {
        super();
        this.array = array;
        this.index = index;
        this.value = value;

    }

    @Override
    public String toString() {
        return TextUtils.addTag(array.toString(), "font color=" + InstrUtils.primColor.getString()) + "[" + index + "]" + " = " + value;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Expression clone() {
        return new ArrayStoreExpression(array.clone(), index.clone(), value.clone());
    }
}
