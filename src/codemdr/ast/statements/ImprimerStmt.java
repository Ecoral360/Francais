package codemdr.ast.statements;

import codemdr.ast.CodeMdrStatement;
import codemdr.execution.CodeMdrExecutorState;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.executor.ASCExecutor;

/**
 * Squelette de l'impl\u00E9mentation d'un programme.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public class ImprimerStmt extends CodeMdrStatement {
    private final Expression<?> expression;

    /**
     * Si le programme n'a pas besoin d'avoir acc\u00E8s \u00e0 l'ex\u00E9cuteur lorsque la m\u00E9thode {@link #execute()}
     * est appel\u00E9e
     */
    public ImprimerStmt(Expression<?> expression, ASCExecutor<CodeMdrExecutorState> executorInstance) {
        super(executorInstance);
        this.expression = expression;
    }

    public Expression<?> getExpression() {
        return expression;
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
        System.out.println(expression.eval());
        super.nextCoord();
        return null;
    }
}
