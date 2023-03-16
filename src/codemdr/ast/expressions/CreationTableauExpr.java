package codemdr.ast.expressions;

import codemdr.objects.CodeMdrTableau;
import org.ascore.ast.buildingBlocs.Expression;

public record CreationTableauExpr(EnumerationExpr valeurs) implements Expression<CodeMdrTableau> {

    @Override
    public CodeMdrTableau eval() {
        return new CodeMdrTableau(valeurs.evalValeurs());
    }
}
