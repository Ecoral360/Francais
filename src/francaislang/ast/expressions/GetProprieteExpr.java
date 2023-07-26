package francaislang.ast.expressions;

import francaislang.objects.FrancaisLangObj;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public record GetProprieteExpr(Expression<?> objExpr, VarExpr nomProprieteExpr) implements Expression<FrancaisLangObj<?>> {

    public void setValeur(FrancaisLangObj<?> valeur) {
        var obj = (FrancaisLangObj<?>) objExpr.eval();
        var nomPropriete = nomProprieteExpr.nom();
        obj.setPropriete(nomPropriete, valeur);
    }


    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public FrancaisLangObj<?> eval() {
        var obj = (FrancaisLangObj<?>) objExpr.eval();
        var nomPropriete = nomProprieteExpr.nom();
        return obj.getPropriete(nomPropriete).orElseThrow(() -> new ASCErrors.ErreurVariableInconnue(
                "Propriété " + nomPropriete + " n'existe pas sur l'objet " + obj + ". Je suis très déçu de toi."
        ));
    }
}
