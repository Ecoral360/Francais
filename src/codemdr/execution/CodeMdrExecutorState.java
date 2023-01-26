package codemdr.execution;

import org.ascore.executor.ASCExecutorState;

/**
 * This class is used to store the state of the execution. The {@link ASCExecutorState} class already takes
 * care of the storage of the variables' scope stack.
 */
public class CodeMdrExecutorState extends ASCExecutorState {
    public CodeMdrExecutorState() {
        super();
    }
}
