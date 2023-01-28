package codemdr.execution;

import codemdr.execution.blocdecode.CodeMdrGestionnaireDeBlocDeCode;
import org.ascore.executor.ASCExecutorState;

/**
 * This class is used to store the state of the execution. The {@link ASCExecutorState} class already takes
 * care of the storage of the variables' scope stack.
 */
public class CodeMdrExecutorState extends ASCExecutorState {
    private final CodeMdrGestionnaireDeBlocDeCode gestionnaireDeBlocDeCode = new CodeMdrGestionnaireDeBlocDeCode();

    public CodeMdrExecutorState() {
        super();
    }

    public CodeMdrGestionnaireDeBlocDeCode getGestionnaireDeBlocDeCode() {
        return gestionnaireDeBlocDeCode;
    }
}
