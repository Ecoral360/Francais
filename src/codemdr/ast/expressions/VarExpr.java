package codemdr.ast.expressions;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.objects.CodeMdrObj;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;
import org.ascore.lang.objects.ASCObject;
import org.ascore.lang.objects.ASCVariable;

/**
 * Squelette de l'impl\u00E9mentation d'une expression.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public record VarExpr(String nom, CodeMdrExecutorState executorState) implements Expression<CodeMdrObj<?>> {


    public ASCVariable<?> getVar() {
        return executorState
                .getScopeManager()
                .getCurrentScopeInstance()
                .getVariable(nom);
    }

    public ASCVariable<?> getCompileTimeVar() {
        return executorState
                .getScopeManager()
                .getCurrentScope()
                .getVariable(nom);
    }

    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    @Override
    public CodeMdrObj<?> eval() {
        var variable = getVar();
        if (variable == null) {
            throw new ASCErrors.ErreurVariableInconnue("La variable " + nom + " n'a pas été définie. Je suis très déçu de toi.");
        }
        return (CodeMdrObj<?>) variable.getAscObject();
    }
}
