package francaislang.ast;

import francaislang.ast.statements.EvalExprStmt;
import francaislang.execution.FrancaisLangExecutorState;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.executor.ASCExecutor;
import org.jetbrains.annotations.NotNull;

public abstract class FrancaisLangStatement extends Statement {

    public static FrancaisLangStatement statementVide(ASCExecutor<?> executorInstance) {
        return new FrancaisLangStatement(executorInstance) {
            @Override
            public Object execute() {
                nextCoord();
                return null;
            }
        };
    }

    public static FrancaisLangStatement evalExpression(ASCExecutor<FrancaisLangExecutorState> executorInstance, Expression<?> expression) {
        return new EvalExprStmt(executorInstance, expression);
    }

    protected FrancaisLangStatement(@NotNull ASCExecutor<?> executorInstance) {
        super(executorInstance);
    }

    public void nextCoord() {
        var state = (FrancaisLangExecutorState) executorInstance.getExecutorState();
        var prochaineCoord = state.getGestionnaireDeBlocDeCode().obtenirProchaineCoord(executorInstance.obtenirCoordRunTime().copy());
        prochaineCoord.ifPresent(coordinate -> executorInstance.setCoordRunTime(coordinate.toString()));
    }
}
