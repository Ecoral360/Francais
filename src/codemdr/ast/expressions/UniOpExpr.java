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
public record UniOpExpr(Expression<?> expr, String op) implements Expression<ASCObject<?>> {

    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public ASCObject<?> eval() {
        var exprValue = expr.eval();
        if (exprValue instanceof CodeMdrNumber codeMdrNumberLeft) {
            var value = codeMdrNumberLeft.getValue().doubleValue();

            var result = switch (op) {
                case "l'inverse binaire de" -> ~(int) value;
                case "l'inverse de" -> 1 / value;
                case "l'opposé de" -> -value;
                default -> throw new ASCErrors.ErreurSyntaxe(
                        "L'opération '" + op + "' n'est pas valide. Je suis très déçu de toi."
                );
            };
            return result == (int) result ? new CodeMdrInt((int) result) : new CodeMdrFloat(result);
        } else {
            throw new ASCErrors.ErreurArithmetique(
                    "L'opération unaire '" + op + "' n'est pas supportée pour un élément de type '" +
                            ((CodeMdrObj<?>) exprValue).getType() + "'. Je suis très déçu de toi."
            );
        }
    }
}
