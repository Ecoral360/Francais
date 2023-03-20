package codemdr.ast.statements;

import codemdr.ast.CodeMdrStatement;
import codemdr.ast.expressions.ConstValueExpr;
import codemdr.ast.expressions.VarExpr;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.objects.CodeMdrObj;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.lang.objects.ASCObject;
import org.ascore.lang.objects.ASCVariable;
import org.jetbrains.annotations.Nullable;

/**
 * Squelette de l'impl\u00E9mentation d'un programme.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public class DeclarerStmt extends CodeMdrStatement {
    private final VarExpr variable;
    private final Expression<?> valeur;

    /**
     * Si le programme n'a pas besoin d'avoir accès à l'exécuteur lorsque la méthode {@link #execute()}
     * est appelée
     */
    public DeclarerStmt(VarExpr variable, @Nullable Expression<?> valeur, ASCExecutor<CodeMdrExecutorState> executeurInstance) {
        super(executeurInstance);
        this.variable = variable;
        this.valeur = valeur == null ? new ConstValueExpr(CodeMdrObj.AUCUNE_VALEUR) : valeur;

        var scope = executeurInstance.getExecutorState()
                .getScopeManager()
                .getCurrentScope();

        if (scope.getVariable(variable.nom()) != null) {
            throw new ASCErrors.ErreurDeclaration("La variable " + variable.nom() +
                    " a déjà été déclarée. Utilisez `Maintenant, " + variable.nom() + " vaut <valeur>.`");
        }

        CodeMdrObj<?> value;
        try {
            value = (CodeMdrObj<?>) valeur.eval();
        } catch (Exception e) {
            value = CodeMdrObj.aucuneValeur();
        }

        scope.declareVariable(new ASCVariable<>(variable.nom(), value == null ? CodeMdrObj.aucuneValeur() : value));
    }

    public Expression<?> getValeur() {
        return valeur;
    }

    public VarExpr getVariable() {
        return variable;
    }

    /**
     * M\u00E9thode d\u00E9crivant l'effet de la ligne de code
     *
     * @return Peut retourner plusieurs choses, ce qui provoque plusieurs effets diff\u00E9rents:
     * <ul>
     *     <li>
     *         <code>null</code> -> continue normalement l'ex\u00E9cution
     *         (la plupart des statements retournent <code>null</code>)
     *     </li>
     *     <li>
     *         <code>instance of Data</code> -> data ajout\u00E9e \u00e0 la liste de data tenue par l'ex\u00E9cuteur
     *     </li>
     *     <li>
     *         autre -> si le programme est appel\u00E9 \u00e0 l'int\u00E9rieur d'une fonction,
     *         cette valeur est celle retourn\u00E9e par la fonction. Sinon, la valeur est ignor\u00E9e
     *     </li>
     * </ul>
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object execute() {
        var valeur = this.valeur.eval();
        var variable = (ASCVariable<Object>) executorInstance.getExecutorState().getScopeManager().getCurrentScopeInstance()
                .getVariable(this.variable.nom());

        variable.setAscObject((ASCObject<Object>) valeur);

        super.nextCoord();
        return null;
    }
}
