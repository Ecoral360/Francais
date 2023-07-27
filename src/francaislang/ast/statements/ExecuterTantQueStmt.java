package francaislang.ast.statements;

import francaislang.ast.FrancaisLangStatement;
import francaislang.execution.FrancaisLangExecutorState;
import francaislang.execution.blocdecode.BlocDeCodeNbEnonces;
import francaislang.objects.FrancaisLangBool;
import francaislang.objects.FrancaisLangEntier;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.executor.ASCExecutor;

/**
 * Squelette de l'impl\u00E9mentation d'un programme.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public class ExecuterTantQueStmt extends FrancaisLangStatement {
    private final Expression<?> conditionExpr, nbEnoncesExpr, nbEnoncesSautesExpr;

    /**
     * Si le programme n'a pas besoin d'avoir accès à l'exécuteur lorsque la méthode {@link #execute()}
     * est appelée
     */
    public ExecuterTantQueStmt(Expression<?> nbEnoncesExpr, Expression<?> conditionExpr, Expression<?> nbEnoncesSautesExpr, ASCExecutor<FrancaisLangExecutorState> executeurInstance) {
        super(executeurInstance);
        this.conditionExpr = conditionExpr;
        this.nbEnoncesExpr = nbEnoncesExpr;
        this.nbEnoncesSautesExpr = nbEnoncesSautesExpr;
    }

    public Expression<?> getConditionExpr() {
        return conditionExpr;
    }

    public Expression<?> getNbEnoncesExpr() {
        return nbEnoncesExpr;
    }

    public Expression<?> getNbEnoncesSautesExpr() {
        return nbEnoncesSautesExpr;
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
        var condition = (FrancaisLangBool) conditionExpr.eval();
        var state = (FrancaisLangExecutorState) executorInstance.getExecutorState();
        if (condition.getValue()) {
            var nbEnonces = (FrancaisLangEntier) nbEnoncesExpr.eval();
            var currCoord = executorInstance.obtenirCoordRunTime().copy();
            state.getGestionnaireDeBlocDeCode().empilerBlocDeCode(
                    new BlocDeCodeNbEnonces(currCoord, c -> currCoord, nbEnonces.getValue())
            );
        } else {
            var nbEnoncesSautes = (FrancaisLangEntier) nbEnoncesSautesExpr.eval();
            for (int i = 0; i < nbEnoncesSautes.getValue(); i++) {
                executorInstance.obtenirCoordRunTime().plusUn();
                ((FrancaisLangExecutorState) executorInstance.getExecutorState()).getGestionnaireDeBlocDeCode().plusUn();
            }
        }
        super.nextCoord();
        return null;
    }
}
