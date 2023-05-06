package codemdr.module.builtins;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.module.CodeMdrModuleInterface;
import codemdr.objects.CodeMdrClasse;
import codemdr.objects.CodeMdrInt;
import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrTableau;
import codemdr.objects.function.CodeMdrFonctionModule;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;
import java.util.Map;

public class ModuleMatriceDeDimensionN implements CodeMdrModuleInterface {
    @Override
    public CodeMdrFonctionModule[] chargerFonctions(CodeMdrExecutorState executeurState) {
        return new CodeMdrFonctionModule[]{};
    }

    @Override
    public ASCVariable<?>[] chargerVariables(CodeMdrExecutorState executeurState) {
        var matrice = new CodeMdrClasse("Matrice", Map.ofEntries(
                Map.entry("TableauInterne", new CodeMdrTableau())
        )).ajouterMethode(
                new CodeMdrFonctionModule("Créer", params -> {
                    var instance = params.get(0);
                    var tableau = (CodeMdrTableau) params.get(1);
                    instance.setPropriete("TableauInterne", tableau);
                    return instance;
                })
        );

        return new ASCVariable[]{
                new ASCVariable<>("Matrice", matrice)
        };
    }
}
