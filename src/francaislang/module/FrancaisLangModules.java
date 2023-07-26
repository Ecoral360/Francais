package francaislang.module;

import francaislang.execution.FrancaisLangExecutorState;
import francaislang.module.builtins.Builtins;
import francaislang.module.builtins.ModuleMath;
import francaislang.module.builtins.ModuleMatriceDeDimensionN;
import francaislang.objects.FrancaisLangModule;
import org.ascore.errors.ASCErrors;
import org.ascore.lang.objects.ASCVariable;

import java.util.Arrays;

public enum FrancaisLangModules {
    BUILTINS("", new Builtins()),
    MATH("Mathématique", new ModuleMath()),
    MATRICE_DE_DIMENSIONS_N("MatriceDeDimensionN", new ModuleMatriceDeDimensionN()),
    ;

    private final FrancaisLangModuleInterface module;
    private final String nomModule;

    FrancaisLangModules(String nomModule, FrancaisLangModuleInterface module) {
        this.module = module;
        this.nomModule = nomModule;
    }

    public static FrancaisLangModules getModule(String nom) {
        for (var module : values()) {
            if (module.nomModule.equals(nom)) return module;
        }
        throw new ASCErrors.ErreurModule("Le module " + nom + " n'existe pas. Je suis très déçu de toi.");
    }

    public void charger(FrancaisLangExecutorState executorState) {
        var fonctions = module.chargerFonctions(executorState);
        var variables = module.chargerVariables(executorState);
        Arrays.stream(fonctions).forEach(func ->
                executorState.getScopeManager().getCurrentScope().declareVariable(new ASCVariable<>(func.getNom(), func))
        );
        Arrays.stream(variables).forEach(var -> executorState.getScopeManager().getCurrentScope().declareVariable(var));
    }

    public void charger(FrancaisLangExecutorState executorState, String alias) {
        if (alias == null) alias = nomModule;

        var fonctions = module.chargerFonctions(executorState);
        var variables = module.chargerVariables(executorState);
        var moduleObj = new FrancaisLangModule(nomModule, fonctions, variables);
        executorState.getScopeManager().getCurrentScope().declareVariable(new ASCVariable<>(alias, moduleObj));
    }

    public void chargerRuntime(FrancaisLangExecutorState executorState, String alias) {
        if (alias == null) alias = nomModule;

        var fonctions = module.chargerFonctions(executorState);
        var variables = module.chargerVariables(executorState);
        var moduleObj = new FrancaisLangModule(nomModule, fonctions, variables);
        executorState.getScopeManager().getCurrentScopeInstance().declareScopeInstanceVariable(new ASCVariable<>(alias, moduleObj));
    }

    public void chargerRuntime(FrancaisLangExecutorState executorState) {
        var fonctions = module.chargerFonctions(executorState);
        var variables = module.chargerVariables(executorState);
        Arrays.stream(fonctions).forEach(func ->
                executorState.getScopeManager()
                        .getCurrentScopeInstance()
                        .declareScopeInstanceVariable(new ASCVariable<>(func.getNom(), func))
        );
        Arrays.stream(variables).forEach(var -> executorState.getScopeManager()
                .getCurrentScopeInstance()
                .declareScopeInstanceVariable(var.clone()));
    }
}
