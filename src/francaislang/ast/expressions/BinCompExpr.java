package francaislang.ast.expressions;


import francaislang.objects.FrancaisLangDecimal;
import francaislang.objects.FrancaisLangEntier;
import francaislang.objects.FrancaisLangNombre;
import francaislang.objects.FrancaisLangTexte;
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
            return new FrancaisLangTexte("" + leftValue + rightValue);
        } else if (leftValue instanceof FrancaisLangNombre francaisLangNombreGauche && rightValue instanceof FrancaisLangNombre francaisLangNombreDroite) {
            var gauche = francaisLangNombreGauche.getValue().doubleValue();
            var droite = francaisLangNombreDroite.getValue().doubleValue();
            var result = switch (op) {
                case "plus" -> gauche + droite;
                case "moins" -> gauche - droite;
                case "fois" -> gauche * droite;
                case "divisé par" -> gauche / droite;
                case "modulo" -> gauche % droite;
                default -> throw new UnsupportedOperationException(op);
            };
            return result == (int) result ? new FrancaisLangEntier((int) result) : new FrancaisLangDecimal(result);
        } else {
            throw new ASCErrors.ErreurArithmetique(
                    "Addition not supported for '" + leftValue.getClass().getSimpleName() +
                            "' and '" + rightValue.getClass().getSimpleName()
            );
        }
    }
}
