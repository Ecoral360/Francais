package codemdr.module;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.module.builtins.Builtins;
import org.ascore.lang.objects.ASCVariable;

import java.util.Arrays;

public enum CodeMdrModules {
    BUILTINS(new Builtins()),
    ;

    private final CodeMdrModule module;

    CodeMdrModules(CodeMdrModule module) {
        this.module = module;
    }

    public static void charger(CodeMdrExecutorState executorState) {
        for (var module : values()) {
            var fonctions = module.module.chargerFonctions(executorState);
            var variables = module.module.chargerVariables(executorState);
            Arrays.stream(fonctions).forEach(func ->
                    executorState.getScopeManager().getCurrentScope().declareVariable(new ASCVariable<>(func.getNom(), func))
            );
            Arrays.stream(variables).forEach(var -> executorState.getScopeManager().getCurrentScope().declareVariable(var));
        }
    }
}
