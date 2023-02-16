package codemdr.module.builtins;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.module.CodeMdrModuleInterface;
import codemdr.objects.function.CodeMdrFonctionModule;
import org.ascore.lang.objects.ASCVariable;

public class ModuleMatriceDeDimensionN implements CodeMdrModuleInterface {
    @Override
    public CodeMdrFonctionModule[] chargerFonctions(CodeMdrExecutorState executeurState) {
        return new CodeMdrFonctionModule[]{
                // new CodeMdrFonctionModule("Matrice")
        };
    }

    @Override
    public ASCVariable<?>[] chargerVariables(CodeMdrExecutorState executeurState) {
        return new ASCVariable[0];
    }
}
