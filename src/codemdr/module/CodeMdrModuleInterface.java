package codemdr.module;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.objects.function.CodeMdrFonctionModule;
import org.ascore.lang.objects.ASCVariable;

public interface CodeMdrModuleInterface {
    CodeMdrFonctionModule[] chargerFonctions(CodeMdrExecutorState executeurState);
    ASCVariable<?>[] chargerVariables(CodeMdrExecutorState executeurState);
}
