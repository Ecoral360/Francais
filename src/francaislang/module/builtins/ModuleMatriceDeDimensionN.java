package francaislang.module.builtins;

import francaislang.execution.FrancaisLangExecutorState;
import francaislang.module.FrancaisLangModuleInterface;
import francaislang.objects.FrancaisLangClasse;
import francaislang.objects.FrancaisLangTableau;
import francaislang.objects.function.FrancaisLangFonctionModule;
import org.ascore.lang.objects.ASCVariable;

import java.util.Map;

public class ModuleMatriceDeDimensionN implements FrancaisLangModuleInterface {
    @Override
    public FrancaisLangFonctionModule[] chargerFonctions(FrancaisLangExecutorState executeurState) {
        return new FrancaisLangFonctionModule[]{};
    }

    @Override
    public ASCVariable<?>[] chargerVariables(FrancaisLangExecutorState executeurState) {
        var matrice = new FrancaisLangClasse("Matrice", Map.ofEntries(
                Map.entry("TableauInterne", new FrancaisLangTableau())
        )).ajouterMethode(
                new FrancaisLangFonctionModule("Créer", params -> {
                    var instance = params.get(0);
                    var tableau = (FrancaisLangTableau) params.get(1);
                    instance.setPropriete("TableauInterne", tableau);
                    return instance;
                })
        );

        return new ASCVariable[]{
                new ASCVariable<>("Matrice", matrice)
        };
    }
}
