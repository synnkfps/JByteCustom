package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.utils.TextUtils;

public class TextExpression extends Expression {

    private String text;

    public TextExpression(String var) {
        this.text = var;
    }

    @Override
    public String toString() {
        return "<i>" + TextUtils.escape(text) + "</i>";
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Expression clone() {
        return new TextExpression(text);
    }
}
