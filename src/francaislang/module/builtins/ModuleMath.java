package francaislang.module.builtins;

import francaislang.execution.FrancaisLangExecutorState;
import francaislang.module.FrancaisLangModuleInterface;
import francaislang.objects.*;
import francaislang.objects.function.FrancaisLangFonctionModule;
import francaislang.objects.function.FrancaisLangParam;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;

public class ModuleMath implements FrancaisLangModuleInterface {
    @Override
    public FrancaisLangFonctionModule[] chargerFonctions(FrancaisLangExecutorState executeurState) {
        return new FrancaisLangFonctionModule[]{
                new FrancaisLangFonctionModule("Sinus", List.of(
                        new FrancaisLangParam("angle")
                ), params -> new FrancaisLangDecimal(Math.sin(Math.toRadians(((FrancaisLangNombre) params.get(0)).getValue().doubleValue()))))
        };
    }

    @Override
    public ASCVariable<?>[] chargerVariables(FrancaisLangExecutorState executeurState) {
        return new ASCVariable[]{
        };
    }
}
