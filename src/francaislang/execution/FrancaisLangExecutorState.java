package francaislang.execution;

import francaislang.execution.blocdecode.FrancaisLangGestionnaireDeBlocDeCode;
import org.ascore.executor.ASCExecutorState;

/**
 * This class is used to store the state of the execution. The {@link ASCExecutorState} class already takes
 * care of the storage of the variables' scope stack.
 */
public class FrancaisLangExecutorState extends ASCExecutorState {
    private final FrancaisLangGestionnaireDeBlocDeCode gestionnaireDeBlocDeCode = new FrancaisLangGestionnaireDeBlocDeCode();

    public FrancaisLangExecutorState() {
        super();
    }

    public FrancaisLangGestionnaireDeBlocDeCode getGestionnaireDeBlocDeCode() {
        return gestionnaireDeBlocDeCode;
    }
}
