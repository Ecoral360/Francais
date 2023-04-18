package codemdr.module.builtins;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.module.CodeMdrModuleInterface;
import codemdr.objects.*;
import codemdr.objects.function.CodeMdrFonctionModule;
import codemdr.objects.function.CodeMdrParam;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;

public class Builtins implements CodeMdrModuleInterface {
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
                }),
                new CodeMdrFonctionModule("TailleDe", List.of(
                        new CodeMdrParam("Liste")
                ), params -> new CodeMdrInt(((CodeMdrTableau) params.get(0)).getValue().size())),

                new CodeMdrFonctionModule("TypeDe", List.of(
                        new CodeMdrParam("Element")
                ), params -> new CodeMdrString(params.get(0).getType().toString())),
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
