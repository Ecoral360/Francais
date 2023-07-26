package francaislang.ast.statements;

import francaislang.ast.FrancaisLangStatement;
import francaislang.execution.FrancaisLangExecutorState;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.executor.ASCExecutor;

public class EvalExprStmt extends FrancaisLangStatement {
    private final Expression<?> expression;
    public EvalExprStmt(ASCExecutor<FrancaisLangExecutorState> executor, Expression<?> expression) {
        super(executor);
        this.expression = expression;
    }

    public Expression<?> getExpression() {
        return expression;
    }

    @Override
    public Object execute() {
        expression.eval();
        nextCoord();
        return null;
    }
}
