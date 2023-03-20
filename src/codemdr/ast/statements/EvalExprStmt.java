package codemdr.ast.statements;

import codemdr.ast.CodeMdrStatement;
import codemdr.execution.CodeMdrExecutorState;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.executor.ASCExecutor;

public class EvalExprStmt extends CodeMdrStatement {
    private final Expression<?> expression;
    public EvalExprStmt(ASCExecutor<CodeMdrExecutorState> executor, Expression<?> expression) {
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
