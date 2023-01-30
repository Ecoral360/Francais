package codemdr.ast.expressions;

import codemdr.objects.CodeMdrInt;
import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrString;
import codemdr.objects.CodeMdrTableau;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public record IndexTexteExpr(Expression<?> listeExpr, Expression<?> indexExpr,
                             int indexDepart) implements Expression<CodeMdrObj<?>> {

    private int getIndex(CodeMdrString liste) {
        var index = ((CodeMdrInt) indexExpr.eval()).getValue().intValue();
        if (index < indexDepart || index >= liste.getValue().length() + indexDepart) {
            throw new ASCErrors.ErreurIndex("La position " + index + " n'est pas contenu dans la liste. " +
                    "Les positions doivent être entre 1 et " + liste.getValue().length() + ". Je suis très déçu de toi.");
        }
        return index - indexDepart;
    }


    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public CodeMdrObj<?> eval() {
        var liste = ((CodeMdrString) listeExpr.eval());
        int index = getIndex(liste);
        return new CodeMdrString(liste.getValue().charAt(index) + "");
    }
}
