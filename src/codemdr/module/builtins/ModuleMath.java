package codemdr.module.builtins;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.module.CodeMdrModuleInterface;
import codemdr.objects.*;
import codemdr.objects.function.CodeMdrFonctionModule;
import codemdr.objects.function.CodeMdrParam;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;

public class ModuleMath implements CodeMdrModuleInterface {
    @Override
    public CodeMdrFonctionModule[] chargerFonctions(CodeMdrExecutorState executeurState) {
        return new CodeMdrFonctionModule[]{
                new CodeMdrFonctionModule("Sinus", List.of(
                        new CodeMdrParam("angle")
                ), params -> new CodeMdrFloat(Math.sin(Math.toRadians(((CodeMdrNumber) params.get(0)).getValue().doubleValue()))))
        };
    }

    @Override
    public ASCVariable<?>[] chargerVariables(CodeMdrExecutorState executeurState) {
        return new ASCVariable[]{
                new ASCVariable<>("Vrai", new CodeMdrBool(true)),
                new ASCVariable<>("Faux", new CodeMdrBool(false))
        };
    }
}
