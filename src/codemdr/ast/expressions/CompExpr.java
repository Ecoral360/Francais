package codemdr.ast.expressions;


import codemdr.objects.*;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;
import org.ascore.lang.objects.ASCObject;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public record CompExpr(Expression<?> left, Expression<?> right) implements Expression<ASCObject<?>> {

    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public ASCObject<?> eval() {
        var leftValue = left.eval();
        var rightValue = right.eval();
        if (leftValue instanceof CodeMdrNumber codeMdrNumberLeft && rightValue instanceof CodeMdrNumber codeMdrNumberRight) {
            return new CodeMdrBool(codeMdrNumberLeft.getValue().doubleValue() < codeMdrNumberRight.getValue().doubleValue());
        } else {
            throw new ASCErrors.ErreurArithmetique(
                    "Comparaison not supported for '" + leftValue.getClass().getSimpleName() +
                            "' and '" + rightValue.getClass().getSimpleName()
            );
        }
    }
}
