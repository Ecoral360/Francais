package francaislang.module;

import francaislang.execution.FrancaisLangExecutorState;
import francaislang.objects.function.FrancaisLangFonctionModule;
import org.ascore.lang.objects.ASCVariable;

public interface FrancaisLangModuleInterface {
    FrancaisLangFonctionModule[] chargerFonctions(FrancaisLangExecutorState executeurState);
    ASCVariable<?>[] chargerVariables(FrancaisLangExecutorState executeurState);
}
