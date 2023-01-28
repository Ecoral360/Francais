package codemdr.ast;

import codemdr.execution.CodeMdrExecutorState;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.executor.ASCExecutor;
import org.jetbrains.annotations.NotNull;

public abstract class CodeMdrStatement extends Statement {
    protected CodeMdrStatement(@NotNull ASCExecutor<?> executorInstance) {
        super(executorInstance);
    }

    public void nextCoord() {
        var state = (CodeMdrExecutorState) executorInstance.getExecutorState();
        var prochaineCoord = state.getGestionnaireDeBlocDeCode().obtenirProchaineCoord(executorInstance.obtenirCoordRunTime().copy());
        prochaineCoord.ifPresent(coordinate -> executorInstance.setCoordRunTime(coordinate.toString()));
    }
}
