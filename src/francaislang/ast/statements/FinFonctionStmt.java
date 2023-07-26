package francaislang.ast.statements;

import francaislang.ast.FrancaisLangStatement;
import francaislang.execution.FrancaisLangExecutorState;
import francaislang.objects.FrancaisLangObj;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.Coordinate;
import org.ascore.tokens.Token;

import java.util.List;

/**
 * Squelette de l'impl\u00E9mentation d'un programme.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public class FinFonctionStmt extends FrancaisLangStatement {
    /**
     * Si le programme n'a pas besoin d'avoir accès à l'exécuteur lorsque la méthode {@link #execute()}
     * est appelée
     */
    public FinFonctionStmt(ASCExecutor<FrancaisLangExecutorState> executeurInstance) {
        super(executeurInstance);
        executorInstance.getExecutorState().getScopeManager().popCurrentScope();
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
        super.nextCoord();
        return FrancaisLangObj.AUCUNE_VALEUR;
    }


    @Override
    public Coordinate getNextCoordinate(Coordinate coord, List<Token> ligne) {
        return new Coordinate(executorInstance.finScope());
    }
}
