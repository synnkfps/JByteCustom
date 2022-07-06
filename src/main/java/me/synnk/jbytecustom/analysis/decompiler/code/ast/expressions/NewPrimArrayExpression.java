package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.analysis.decompiler.code.ast.VarType;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;

public class NewPrimArrayExpression extends Expression {
    private Expression count;
    private VarType type;

    public NewPrimArrayExpression(Expression count, VarType type) {
        super();
        this.count = count;
        this.type = type;
    }

    @Override
    public String toString() {
        return "<b>new</b> " + TextUtils.addTag(type.getType(), "font color=" + InstrUtils.secColor.getString()) + "[" + count + "]";
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Expression clone() {
        return new NewPrimArrayExpression(count.clone(), type);
    }
}
