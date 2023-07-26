package francaislang.ast.expressions;

import francaislang.objects.FrancaisLangTableau;
import org.ascore.ast.buildingBlocs.Expression;

public record CreationTableauExpr(EnumerationExpr valeurs) implements Expression<FrancaisLangTableau> {

    @Override
    public FrancaisLangTableau eval() {
        return new FrancaisLangTableau(valeurs.evalValeurs());
    }
}
