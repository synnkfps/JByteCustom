package me.synnk.jbytecustom.analysis.decompiler.code.ast.expressions;

import me.synnk.jbytecustom.analysis.decompiler.ClassDefinition;
import me.synnk.jbytecustom.analysis.decompiler.code.ast.Expression;
import me.synnk.jbytecustom.utils.InstrUtils;
import me.synnk.jbytecustom.utils.TextUtils;

import java.util.ArrayList;

public class NewTypeExpression extends Expression {
    private ClassDefinition object;
    private MethodExpression init;

    public NewTypeExpression(ClassDefinition object) {
        super();
        this.object = object;
    }

    @Override
    public String toString() {
        if (init != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<b>new</b> " + TextUtils.addTag(object.getName(), "font color=" + InstrUtils.secColor.getString()));
            sb.append("</font>(");
            ArrayList<String> argsString = new ArrayList<>();
            for (Expression ref : init.getArgs()) {
                argsString.add(ref.toString());
            }
            sb.append(String.join(", ", argsString));
            sb.append(")");
            return sb.toString();
        }
        return "<b>new</b> " + TextUtils.addTag(object.getName(), "font color=" + InstrUtils.secColor.getString());
    }

    @Override
    public int size() {
        return 1;
    }

    public Expression getInit() {
        return init;
    }

    public void setInit(MethodExpression init) {
        this.init = init;
    }

    @Override
    public Expression clone() {
        NewTypeExpression nte = new NewTypeExpression(object);
        if (init != null) {
            nte.setInit((MethodExpression) init.clone());
        }
        return nte;
    }
}
