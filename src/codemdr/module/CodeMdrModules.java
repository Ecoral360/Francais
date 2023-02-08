package codemdr.module;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.module.builtins.Builtins;
import codemdr.module.builtins.ModuleMath;
import codemdr.objects.CodeMdrModule;
import org.ascore.errors.ASCErrors;
import org.ascore.lang.objects.ASCVariable;

import java.util.Arrays;

public enum CodeMdrModules {
    BUILTINS("", new Builtins()),
    MATH("Mathématique", new ModuleMath()),
    ;

    private final CodeMdrModuleInterface module;
    private final String nomModule;

    CodeMdrModules(String nomModule, CodeMdrModuleInterface module) {
        this.module = module;
        this.nomModule = nomModule;
    }

    public static CodeMdrModules getModule(String nom) {
        for (var module : values()) {
            if (module.nomModule.equals(nom)) return module;
        }
        throw new ASCErrors.ErreurModule("Le module " + nom + " n'existe pas. Je suis très déçu de toi.");
    }

    public void charger(CodeMdrExecutorState executorState) {
        var fonctions = module.chargerFonctions(executorState);
        var variables = module.chargerVariables(executorState);
        Arrays.stream(fonctions).forEach(func ->
                executorState.getScopeManager().getCurrentScope().declareVariable(new ASCVariable<>(func.getNom(), func))
        );
        Arrays.stream(variables).forEach(var -> executorState.getScopeManager().getCurrentScope().declareVariable(var));
    }

    public void charger(CodeMdrExecutorState executorState, String alias) {
        if (alias == null) alias = nomModule;

        var fonctions = module.chargerFonctions(executorState);
        var variables = module.chargerVariables(executorState);
        var moduleObj = new CodeMdrModule(nomModule, fonctions, variables);
        executorState.getScopeManager().getCurrentScope().declareVariable(new ASCVariable<>(alias, moduleObj));
    }

    public void chargerRuntime(CodeMdrExecutorState executorState, String alias) {
        if (alias == null) alias = nomModule;

        var fonctions = module.chargerFonctions(executorState);
        var variables = module.chargerVariables(executorState);
        var moduleObj = new CodeMdrModule(nomModule, fonctions, variables);
        executorState.getScopeManager().getCurrentScopeInstance().declareScopeInstanceVariable(new ASCVariable<>(alias, moduleObj));
    }
}
