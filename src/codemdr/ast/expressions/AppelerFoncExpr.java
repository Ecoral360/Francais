package codemdr.ast.expressions;

import codemdr.objects.CodeMdrObj;
import codemdr.objects.function.CodeMdrCallable;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;


public record AppelerFoncExpr(Expression<?> fonctionExpr, EnumerationExpr args) implements Expression<CodeMdrObj<?>> {

    public AppelerFoncExpr {
        if (!(fonctionExpr instanceof VarExpr) && !(fonctionExpr instanceof IndexListeExpr) && !(fonctionExpr instanceof GetProprieteExpr)) {
            throw new ASCErrors.ErreurAppelFonction("Tu ne peux pas appeler cela. Je suis très déçu de toi.");
        }
    }

    public AppelerFoncExpr(Expression<?> fonctionExpr) {
        this(fonctionExpr, EnumerationExpr.completeEnumeration());
    }

    @Override
    public CodeMdrObj<?> eval() {
        var fonction = (CodeMdrCallable) fonctionExpr.eval();
        return fonction.appeler(args.evalValeurs());
    }
}
