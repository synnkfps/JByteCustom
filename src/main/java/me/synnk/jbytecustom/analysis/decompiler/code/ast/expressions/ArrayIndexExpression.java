package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;

public class ArrayIndexExpression extends Expression {

    private Expression array;
    private Expression index;
    private boolean twoword;

    public ArrayIndexExpression(Expression array, Expression index, boolean twoword) {
        super();
        this.array = array;
        this.index = index;
        this.twoword = twoword;
    }

    @Override
    public String toString() {
        return TextUtils.addTag(array.toString(), "font color=" + InstrUtils.primColor.getString()) + "[" + index + "]";
    }

    @Override
    public int size() {
        return twoword ? 2 : 1;
    }

    @Override
    public Expression clone() {
        return new ArrayIndexExpression(array.clone(), index.clone(), twoword);
    }
}
