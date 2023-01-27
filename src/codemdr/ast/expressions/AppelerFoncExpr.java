package codemdr.ast.expressions;

import codemdr.objects.CodeMdrObj;
import codemdr.objects.function.CodeMdrCallable;
import org.ascore.ast.buildingBlocs.Expression;


public record AppelerFoncExpr(VarExpr fonctionExpr, EnumerationExpr args) implements Expression<CodeMdrObj<?>> {

    public AppelerFoncExpr(VarExpr fonctionExpr) {
        this(fonctionExpr, EnumerationExpr.completeEnumeration());
    }

    @Override
    public CodeMdrObj<?> eval() {
        var fonction = (CodeMdrCallable) fonctionExpr.eval();
        return fonction.appeler(args.evalValeurs());
    }
}
