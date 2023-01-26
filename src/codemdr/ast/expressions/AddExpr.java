package codemdr.ast.expressions;


import codemdr.objects.CodeMdrFloat;
import codemdr.objects.CodeMdrInt;
import codemdr.objects.CodeMdrNumber;
import codemdr.objects.CodeMdrString;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;
import org.ascore.lang.objects.ASCObject;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public record AddExpr(Expression<?> left, Expression<?> right) implements Expression<ASCObject<?>> {

    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public ASCObject<?> eval() {
        var leftValue = left.eval();
        var rightValue = right.eval();
        if (leftValue instanceof CodeMdrString || rightValue instanceof CodeMdrString) {
            return new CodeMdrString("" + leftValue.getValue() + right.eval().getValue());
        } else if (leftValue instanceof CodeMdrNumber codeMdrNumberLeft && rightValue instanceof CodeMdrNumber codeMdrNumberRight) {
            var result = codeMdrNumberLeft.getValue().doubleValue() + codeMdrNumberRight.getValue().doubleValue();
            return result == (int) result ? new CodeMdrInt((int) result) : new CodeMdrFloat(result);
        } else {
            throw new ASCErrors.ErreurArithmetique(
                    "Addition not supported for '" + leftValue.getClass().getSimpleName() +
                            "' and '" + rightValue.getClass().getSimpleName()
            );
        }
    }
}
