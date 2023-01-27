package codemdr.ast.statements;

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

import java.util.List;

/**
 * Squelette de l'impl\u00E9mentation d'un programme.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public class CreerFonctionStmt extends Statement {
    private final VarExpr fonction;
    private final List<VarExpr> args;

    /**
     * Si le programme n'a pas besoin d'avoir accès à l'exécuteur lorsque la méthode {@link #execute()}
     * est appelée
     */
    public CreerFonctionStmt(VarExpr nom, List<VarExpr> args, ASCExecutor<CodeMdrExecutorState> executeurInstance) {
        super(executeurInstance);
        this.fonction = nom;
        this.args = args;

        var scope = executeurInstance.getExecutorState()
                .getScopeManager()
                .getCurrentScope();

        if (scope.getVariable(fonction.nom()) != null) {
            throw new ASCErrors.ErreurDeclaration("La variable " + fonction.nom() +
                    " a déjà été déclarée. Utilisez `Maintenant, " + fonction.nom() + " vaut <valeur>.`");
        }

        scope.declareVariable(new ASCVariable<>(fonction.nom(), CodeMdrObj.AUCUNE_VALEUR));
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
        var valeur = this.args.stream().map(Expression::eval).toList();
        var variable = (ASCVariable<Object>) executorInstance.getExecutorState().getScopeManager().getCurrentScopeInstance()
                .getVariable(this.fonction.nom());

        variable.setAscObject((ASCObject<Object>) valeur);

        return null;
    }
}
