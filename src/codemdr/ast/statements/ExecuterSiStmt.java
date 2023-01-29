package codemdr.ast.statements;

import codemdr.ast.CodeMdrStatement;
import codemdr.ast.expressions.ConstValueExpr;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.execution.blocdecode.BlocDeCodeNbEnonces;
import codemdr.objects.CodeMdrBool;
import codemdr.objects.CodeMdrInt;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.Coordinate;

/**
 * Squelette de l'impl\u00E9mentation d'un programme.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public class ExecuterSiStmt extends CodeMdrStatement {
    private final Expression<?> conditionExpr, nbEnoncesSiExpr, nbEnoncesSautesApresSiExpr, nbEnoncesSinonExpr, nbEnoncesSautesAvantSinonExpr;

    public ExecuterSiStmt(Expression<?> nbEnoncesSiExpr,
                          Expression<?> nbEnoncesSautesApresSiExpr,
                          Expression<?> nbEnoncesSautesAvantSinonExpr,
                          Expression<?> nbEnoncesSinonExpr,
                          Expression<?> conditionExpr,
                          ASCExecutor<CodeMdrExecutorState> executeurInstance) {
        super(executeurInstance);
        this.conditionExpr = conditionExpr;
        this.nbEnoncesSiExpr = nbEnoncesSiExpr;
        this.nbEnoncesSinonExpr = nbEnoncesSinonExpr != null ? nbEnoncesSinonExpr : new ConstValueExpr(new CodeMdrInt(0));
        this.nbEnoncesSautesApresSiExpr = nbEnoncesSautesApresSiExpr != null ? nbEnoncesSautesApresSiExpr : this.nbEnoncesSinonExpr;
        this.nbEnoncesSautesAvantSinonExpr = nbEnoncesSautesAvantSinonExpr != null ? nbEnoncesSautesAvantSinonExpr : this.nbEnoncesSiExpr;
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

        var state = (CodeMdrExecutorState) executorInstance.getExecutorState();

        if (condition.getValue()) {
            var nbEnoncesSi = (CodeMdrInt) nbEnoncesSiExpr.eval();
            var nbEnoncesSautesApresSi = ((CodeMdrInt) nbEnoncesSautesApresSiExpr.eval()).getValue().intValue();
            var currCoord = executorInstance.obtenirCoordRunTime().copy();
            var coordFin = currCoord.copy();

            state.getGestionnaireDeBlocDeCode().empilerBlocDeCode(
                    new BlocDeCodeNbEnonces(currCoord, c -> {
                        for (int i = 0; i < nbEnoncesSautesApresSi + 1; i++) {
                            c.plusUn();
                        }
                        return c;
                    }, nbEnoncesSi.getValue().intValue())
            );
        } else {
            var nbEnoncesAvantSinon = (CodeMdrInt) nbEnoncesSautesAvantSinonExpr.eval();
            for (int i = 0; i < nbEnoncesAvantSinon.getValue().intValue(); i++) {
                executorInstance.obtenirCoordRunTime().plusUn();
            }
            var nbEnoncesSinon = (CodeMdrInt) nbEnoncesSinonExpr.eval();
            var currCoord = executorInstance.obtenirCoordRunTime().copy();

            state.getGestionnaireDeBlocDeCode().empilerBlocDeCode(
                    new BlocDeCodeNbEnonces(currCoord, Coordinate::plusUn, nbEnoncesSinon.getValue().intValue())
            );
        }
        super.nextCoord();
        return null;
    }
}
