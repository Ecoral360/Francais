package codemdr.module.builtins;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.module.CodeMdrModule;
import codemdr.objects.CodeMdrBool;
import codemdr.objects.CodeMdrFloat;
import codemdr.objects.CodeMdrObj;
import codemdr.objects.function.CodeMdrFonctionModule;
import codemdr.objects.function.CodeMdrParam;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;

public class Builtins implements CodeMdrModule {
    @Override
    public CodeMdrFonctionModule[] chargerFonctions(CodeMdrExecutorState executeurState) {
        return new CodeMdrFonctionModule[]{
                new CodeMdrFonctionModule("Somme", List.of(
                        new CodeMdrParam("Liste")
                ), params -> {
                    double sum = 0;
                    for (var param : params) {
                        sum += ((Number) param.getValue()).doubleValue();
                    }

                    return new CodeMdrFloat(sum);
                })
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
