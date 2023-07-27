package francaislang.ast.statements;

import francaislang.ast.FrancaisLangStatement;
import francaislang.execution.FrancaisLangExecutorState;
import francaislang.execution.FrancaisLangPreCompiler;
import francaislang.module.FrancaisLangModules;
import francaislang.objects.FrancaisLangModule;
import francaislang.objects.function.FrancaisLangFonctionModule;
import francaislang.parser.FrancaisLangGASA;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.ASCExecutorBuilder;
import org.ascore.lang.objects.ASCVariable;
import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

public class InclureStmt extends FrancaisLangStatement {
    private final String nomModule;
    private final String cheminFichier;

    public InclureStmt(ASCExecutor<FrancaisLangExecutorState> executor, String nomModule, String cheminFichier) {
        super(executor);
        this.nomModule = nomModule;
        this.cheminFichier = cheminFichier;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public String getNomModule() {
        return nomModule;
    }

    @Override
    public Object execute() {
        String codeToExecute;

        try (Scanner scanner = new Scanner(new File(cheminFichier))) {
            StringBuilder s = new StringBuilder();
            while (scanner.hasNextLine()) s.append(scanner.nextLine()).append('\n');
            codeToExecute = s.toString();
        } catch (FileNotFoundException e) {
            throw new ASCErrors.ErreurModule("Fichier " + cheminFichier + " n'a pas été trouvé. Je suis très déçu de toi.");
        }

        var executor = new ASCExecutorBuilder<FrancaisLangExecutorState>() // create an executor builder
                .withLexer(executorInstance.getLexer()) // add the lexer to the builder
                .withParser(FrancaisLangGASA::new) // add the parser to the builder
                .withExecutorState(new FrancaisLangExecutorState()) // add the executor state to the builder
                .withPrecompiler(new FrancaisLangPreCompiler()) // add the precompiler to the builder
                .build(); // build the executor
        JSONArray compilationResult = executor.compile(codeToExecute, true); // compile the code
        if (compilationResult.length() != 0) {
            throw new ASCErrors.ErreurModule(compilationResult + ". Je suis très déçu de toi.");
        }

        FrancaisLangModules.BUILTINS.charger(executor.getExecutorState());

        JSONArray executionResult = executor.executerMain(false, false); // execute the code
        for (int i = 0; i < executionResult.length(); i++) {
            var action = executionResult.getJSONObject(i);
            if (action.getInt("id") == 400) {
                throw new ASCErrors.ASCError(action.getJSONArray("p").getString(1), action.getJSONArray("p").getString(0));
            }
        }
        var variablesModule = executor.getExecutorState().getScopeManager().getCurrentScopeInstance().getVariableStack();
        if (nomModule == null) {
            for (var variable : variablesModule) {
                executorInstance
                        .getExecutorState()
                        .getScopeManager()
                        .getCurrentScopeInstance()
                        .declareScopeInstanceVariable(variable);
            }
        } else {
            var module = new FrancaisLangModule(nomModule, new FrancaisLangFonctionModule[0], variablesModule.toArray(ASCVariable[]::new));
            executorInstance
                    .getExecutorState()
                    .getScopeManager()
                    .getCurrentScopeInstance()
                    .declareScopeInstanceVariable(new ASCVariable<>(nomModule, module));
        }

        for (var scope : executor.obtenirCoordCompileDict().entrySet()) {
            if (scope.getKey().equals("main")) continue;
            executorInstance.obtenirCoordCompileDict().put(scope.getKey(), scope.getValue());
        }

        super.nextCoord();
        return null;
    }
}
