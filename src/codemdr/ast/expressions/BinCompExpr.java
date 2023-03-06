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
public record BinCompExpr(Expression<?> left, Expression<?> right, String op) implements Expression<ASCObject<?>> {

    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public ASCObject<?> eval() {
        var leftValue = left.eval();
        var rightValue = right.eval();
        if (op.equals("")) {
            return new CodeMdrString("" + leftValue + rightValue);
        } else if (leftValue instanceof CodeMdrNumber codeMdrNumberLeft && rightValue instanceof CodeMdrNumber codeMdrNumberRight) {
            var gauche = codeMdrNumberLeft.getValue().doubleValue();
            var droite = codeMdrNumberRight.getValue().doubleValue();
            var result = switch (op) {
                case "plus" -> gauche + droite;
                case "moins" -> gauche - droite;
                case "fois" -> gauche * droite;
                case "divisé par" -> gauche / droite;
                case "modulo" -> gauche % droite;
                default -> throw new UnsupportedOperationException(op);
            };
            return result == (int) result ? new CodeMdrInt((int) result) : new CodeMdrFloat(result);
        } else {
            throw new ASCErrors.ErreurArithmetique(
                    "Addition not supported for '" + leftValue.getClass().getSimpleName() +
                            "' and '" + rightValue.getClass().getSimpleName()
            );
        }
    }
}
