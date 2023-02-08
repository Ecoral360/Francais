package codemdr.ast.statements;

import codemdr.ast.expressions.VarExpr;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.module.CodeMdrModules;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.executor.ASCExecutor;

public class ImporterStmt extends Statement {
    private final String nomModule;
    private final String alias;

    public ImporterStmt(ASCExecutor<CodeMdrExecutorState> executor, String nomModule, String alias) {
        super(executor);
        this.nomModule = nomModule;
        this.alias = alias;

    }

    @Override
    public Object execute() {
        CodeMdrModules.getModule(nomModule).chargerRuntime(
                (CodeMdrExecutorState) executorInstance.getExecutorState(),
                alias
        );
        return null;
    }
}
