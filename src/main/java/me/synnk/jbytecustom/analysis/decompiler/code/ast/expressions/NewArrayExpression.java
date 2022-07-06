package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.ClassDefinition;
import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;

public class NewArrayExpression extends Expression {
    private Expression count;
    private ClassDefinition object;

    public NewArrayExpression(Expression count, ClassDefinition object) {
        super();
        this.count = count;
        this.object = object;
    }

    @Override
    public String toString() {
        return "<b>new</b> " + TextUtils.addTag(object.getName(), "font color=" + InstrUtils.secColor.getString()) + "[" + count + "]";
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Expression clone() {
        return new NewArrayExpression(count.clone(), object);
    }
}
