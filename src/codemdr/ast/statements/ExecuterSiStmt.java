package codemdr.ast.statements;

import codemdr.ast.CodeMdrStatement;
import codemdr.ast.expressions.ConstValueExpr;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.execution.blocdecode.BlocDeCodeNbEnonces;
import codemdr.objects.CodeMdrBool;
import codemdr.objects.CodeMdrInt;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.executor.ASCExecutor;

/**
 * Squelette de l'impl\u00E9mentation d'un programme.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public class ExecuterSiStmt extends CodeMdrStatement {
    private final Expression<?> conditionExpr, nbEnoncesSiExpr, nbEnoncesSinonExpr;

    /**
     * Si le programme n'a pas besoin d'avoir accès à l'exécuteur lorsque la méthode {@link #execute()}
     * est appelée
     */
    public ExecuterSiStmt(Expression<?> nbEnoncesSiExpr, Expression<?> nbEnoncesSinonExpr, Expression<?> conditionExpr, ASCExecutor<CodeMdrExecutorState> executeurInstance) {
        super(executeurInstance);
        this.conditionExpr = conditionExpr;
        this.nbEnoncesSiExpr = nbEnoncesSiExpr;
        this.nbEnoncesSinonExpr = nbEnoncesSinonExpr == null ? new ConstValueExpr(new CodeMdrInt(0)) : nbEnoncesSinonExpr;
    }

    public ExecuterSiStmt(Expression<?> nbEnoncesSiExpr, Expression<?> conditionExpr, ASCExecutor<CodeMdrExecutorState> executeurInstance) {
        this(nbEnoncesSiExpr, null, conditionExpr, executeurInstance);
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
    public Object execute() {
        var condition = (CodeMdrBool) conditionExpr.eval();
        var nbEnoncesSi = (CodeMdrInt) nbEnoncesSiExpr.eval();
        var nbEnoncesSinon = (CodeMdrInt) nbEnoncesSinonExpr.eval();
        var state = (CodeMdrExecutorState) executorInstance.getExecutorState();

        var fin = nbEnoncesSi.getValue().intValue() + nbEnoncesSinon.getValue().intValue();

        if (condition.getValue()) {
            var currCoord = executorInstance.obtenirCoordRunTime().copy();
            var coordFin = currCoord.copy();

            state.getGestionnaireDeBlocDeCode().empilerBlocDeCode(
                    new BlocDeCodeNbEnonces(currCoord, null, nbEnoncesSi.getValue().intValue())
            );
        } else {
            for (int i = 0; i < nbEnoncesSi.getValue().intValue() - 1; i++) {
                executorInstance.obtenirCoordRunTime().plusUn();
            }
        }
        super.nextCoord();
        return null;
    }
}
