package codemdr.ast.expressions;

import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrTableau;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.lang.objects.ASCObject;

public class CreationTableauExpr implements Expression<CodeMdrTableau> {
    private final EnumerationExpr valeurs;

    public CreationTableauExpr(EnumerationExpr valeurs) {
        this.valeurs = valeurs;
    }

    @Override
    public CodeMdrTableau eval() {
        return new CodeMdrTableau(valeurs.evalValeurs());
    }
}
