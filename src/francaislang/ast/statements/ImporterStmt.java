package francaislang.ast.statements;

import francaislang.execution.FrancaisLangExecutorState;
import francaislang.module.FrancaisLangModules;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.executor.ASCExecutor;

public class ImporterStmt extends Statement {
    private final String nomModule;
    private final String alias;
    private final boolean noAlias;

    public ImporterStmt(ASCExecutor<FrancaisLangExecutorState> executor, String nomModule, String alias) {
        super(executor);
        this.nomModule = nomModule;
        this.alias = alias;
        noAlias = false;
    }

    public ImporterStmt(ASCExecutor<FrancaisLangExecutorState> executor, String nomModule) {
        super(executor);
        this.nomModule = nomModule;
        this.alias = null;
        noAlias = true;
    }

    @Override
    public Object execute() {
        if (noAlias) {
            FrancaisLangModules.getModule(nomModule).chargerRuntime((FrancaisLangExecutorState) executorInstance.getExecutorState());
        } else {
            FrancaisLangModules.getModule(nomModule).charger((FrancaisLangExecutorState) executorInstance.getExecutorState(), alias);
        }
        return null;
    }
}
