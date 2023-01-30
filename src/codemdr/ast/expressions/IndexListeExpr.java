package codemdr.ast.expressions;

import codemdr.objects.CodeMdrInt;
import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrTableau;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;
import org.ascore.lang.objects.ASCObject;

import java.util.ArrayList;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public record IndexListeExpr(Expression<?> listeExpr, Expression<?> indexExpr,
                             int indexDepart) implements Expression<CodeMdrObj<?>> {

    private int getIndex(CodeMdrTableau liste) {
        var index = ((CodeMdrInt) indexExpr.eval()).getValue().intValue();
        if (index < indexDepart || index >= liste.taille() + indexDepart) {
            throw new ASCErrors.ErreurIndex("La position " + index + " n'est pas contenu dans la liste. " +
                    "Les positions doivent être entre 1 et " + liste.taille() + ". Je suis très déçu de toi.");
        }
        return index - indexDepart;
    }

    public void setValeur(CodeMdrObj<?> valeur) {
        var liste = ((CodeMdrTableau) listeExpr.eval());
        liste.setValeur(getIndex(liste), valeur);
    }


    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public CodeMdrObj<?> eval() {
        var liste = ((CodeMdrTableau) listeExpr.eval());
        int index = getIndex(liste);
        return liste.getValeur(index);
    }
}
