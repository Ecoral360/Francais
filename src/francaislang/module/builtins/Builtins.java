package francaislang.module.builtins;

import francaislang.execution.FrancaisLangExecutorState;
import francaislang.module.FrancaisLangModuleInterface;
import francaislang.objects.*;
import francaislang.objects.function.FrancaisLangFonctionModule;
import francaislang.objects.function.FrancaisLangParam;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;

public class Builtins implements FrancaisLangModuleInterface {
    @Override
    public FrancaisLangFonctionModule[] chargerFonctions(FrancaisLangExecutorState executeurState) {
        return new FrancaisLangFonctionModule[]{
                new FrancaisLangFonctionModule("Somme", List.of(
                        new FrancaisLangParam("Liste")
                ), params -> {
                    double sum = 0;
                    for (var param : params) {
                        sum += ((Number) param.getValue()).doubleValue();
                    }

                    return new FrancaisLangDecimal(sum);
                }),
                new FrancaisLangFonctionModule("TailleDe", List.of(
                        new FrancaisLangParam("Liste")
                ), params -> new FrancaisLangEntier(((FrancaisLangTableau) params.get(0)).getValue().size())),

                new FrancaisLangFonctionModule("TypeDe", List.of(
                        new FrancaisLangParam("Element")
                ), params -> new FrancaisLangTexte(params.get(0).getType().toString())),
        };
    }

    @Override
    public ASCVariable<?>[] chargerVariables(FrancaisLangExecutorState executeurState) {
        return new ASCVariable[]{
                new ASCVariable<>("Vrai", new FrancaisLangBool(true)),
                new ASCVariable<>("Faux", new FrancaisLangBool(false))
        };
    }
}
