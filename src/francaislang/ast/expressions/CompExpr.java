package francaislang.ast.expressions;


import francaislang.objects.*;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;
import org.ascore.lang.objects.ASCObject;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public record CompExpr(Expression<?> left, Expression<?> right, String comp) implements Expression<ASCObject<?>> {

    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public ASCObject<?> eval() {
        var leftValue = (FrancaisLangObj<?>) left.eval();
        var rightValue = (FrancaisLangObj<?>) right.eval();
        switch (comp) {
            case "veau", "ne veau pas" -> {
                throw new ASCErrors.ErreurComparaison("Meuuuh. Je suis très déçu de toi.");
            }
            case "vaut" -> {
                return new FrancaisLangBool(leftValue.equals(rightValue));
            }
            case "ne vaut pas" -> {
                return new FrancaisLangBool(!leftValue.equals(rightValue));
            }
        }

        if (leftValue instanceof FrancaisLangNombre francaisLangNombreGauche && rightValue instanceof FrancaisLangNombre francaisLangNombreDroite) {
            var gauche = francaisLangNombreGauche.getValue().doubleValue();
            var droite = francaisLangNombreDroite.getValue().doubleValue();
            var result = switch (comp) {
                case "vaut moins que" -> gauche < droite;
                case "vaut moins ou autant que" -> gauche <= droite;
                case "vaut plus que" -> gauche > droite;
                case "vaut plus ou autant que" -> gauche >= droite;
                default -> throw new UnsupportedOperationException(comp);
            };
            return new FrancaisLangBool(result);
        } else {
            throw new ASCErrors.ErreurArithmetique(
                    "Comparaison not supported for '" + leftValue.getClass().getSimpleName() +
                            "' and '" + rightValue.getClass().getSimpleName()
            );
        }
    }
}
