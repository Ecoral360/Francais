package francaislang.ast.expressions;

import francaislang.objects.FrancaisLangObj;
import francaislang.objects.function.FrancaisLangCallable;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;


public record AppelerFoncExpr(Expression<?> fonctionExpr, EnumerationExpr args) implements Expression<FrancaisLangObj<?>> {

    public AppelerFoncExpr {
        if (!(fonctionExpr instanceof VarExpr) && !(fonctionExpr instanceof IndexListeExpr) && !(fonctionExpr instanceof GetProprieteExpr)) {
            throw new ASCErrors.ErreurAppelFonction("Tu ne peux pas appeler cela. Je suis très déçu de toi.");
        }
    }

    public AppelerFoncExpr(Expression<?> fonctionExpr) {
        this(fonctionExpr, EnumerationExpr.completeEnumeration());
    }

    @Override
    public FrancaisLangObj<?> eval() {
        var fonction = (FrancaisLangCallable) fonctionExpr.eval();
        return fonction.appeler(args.evalValeurs());
    }
}
