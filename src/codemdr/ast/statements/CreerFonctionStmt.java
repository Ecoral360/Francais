package codemdr.ast.statements;

import codemdr.ast.CodeMdrStatement;
import codemdr.ast.expressions.VarExpr;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.objects.CodeMdrObj;
import codemdr.objects.function.CodeMdrFonction;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.Coordinate;
import org.ascore.lang.objects.ASCVariable;
import org.ascore.lang.objects.ASScope;
import org.ascore.managers.scope.ASScopeManager;
import org.ascore.tokens.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Squelette de l'impl\u00E9mentation d'un programme.<br>
 * Pour trouver un exemple d'une impl\u00E9mentation compl\u00E8te
 *
 * @author Mathis Laroche
 */
public class CreerFonctionStmt extends CodeMdrStatement {
    private final String nomFonction;
    private final List<VarExpr> args;
    private final ASScope scope;

    /**
     * Si le programme n'a pas besoin d'avoir accès à l'exécuteur lorsque la méthode {@link #execute()}
     * est appelée
     */
    public CreerFonctionStmt(String nom, List<VarExpr> args, ASCExecutor<CodeMdrExecutorState> executeurInstance) {
        super(executeurInstance);
        this.nomFonction = nom;
        this.args = args;

        var scope = executeurInstance.getExecutorState()
                .getScopeManager()
                .getCurrentScope();

        if (scope.getVariable(nomFonction) != null) {
            throw new ASCErrors.ErreurDeclaration("La variable " + nomFonction +
                    " a déjà été déclarée. Utilisez `Maintenant, " + nomFonction + " vaut <valeur>.`");
        }

        scope.declareVariable(new ASCVariable<>(nomFonction, CodeMdrObj.AUCUNE_VALEUR));

        this.scope = executeurInstance.getExecutorState().getScopeManager().makeNewCurrentScope();
    }

    public List<VarExpr> getArgs() {
        return args;
    }

    public String getNomFonction() {
        return nomFonction;
    }

    public ASScope getScope() {
        return scope;
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
        var scope = new ASScope(this.scope);
        var currentScope = executorInstance.obtenirCoordRunTime().getScope();
        var callingCoord = ASScopeManager.formatNewScope(ASScopeManager.ScopeKind.FONCTION, currentScope, this.nomFonction);

        var variable = (ASCVariable<Object>) executorInstance.getExecutorState().getScopeManager().getCurrentScopeInstance()
                .getVariable(this.nomFonction);

        var fonction = new CodeMdrFonction(this.nomFonction,
                args.stream().map(arg -> new ASCVariable<>(arg.nom(), CodeMdrObj.AUCUNE_VALEUR)).toList(),
                callingCoord,
                (ASCExecutor<CodeMdrExecutorState>) executorInstance);

        variable.setAscObject(fonction);

        for (var arg : this.args) {
            scope.declareVariable(new ASCVariable<>(arg.nom(), CodeMdrObj.AUCUNE_VALEUR));
        }

        fonction.setScope(scope);
        scope.setParent(executorInstance.getExecutorState().getScopeManager().getCurrentScopeInstance());

        super.nextCoord();
        return null;
    }

    public String getScopeCoordinate(Coordinate coord) {
        return ASScopeManager.formatNewScope(ASScopeManager.ScopeKind.FONCTION, coord.getScope(), nomFonction);
    }

    @Override
    public Coordinate getNextCoordinate(Coordinate coord, List<Token> ligne) {
        return new Coordinate(executorInstance.nouveauScope(getScopeCoordinate(coord)));
    }
}
