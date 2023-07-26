package francaislang.ast.statements;

import francaislang.execution.FrancaisLangExecutorState;
import francaislang.execution.FrancaisLangPreCompiler;
import francaislang.module.FrancaisLangModules;
import francaislang.parser.FrancaisLangGASA;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.ASCExecutorBuilder;
import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

public class InclureStmt extends Statement {
    private final String nomModule;
    private final String cheminFichier;

    public InclureStmt(ASCExecutor<FrancaisLangExecutorState> executor, String nomModule, String cheminFichier) {
        super(executor);
        this.nomModule = nomModule;
        this.cheminFichier = cheminFichier;
    }

    @Override
    public Object execute() {
        String codeToExecute;

        try (Scanner scanner = new Scanner(new File(cheminFichier))) {
            StringBuilder s = new StringBuilder();
            while (scanner.hasNextLine()) s.append(scanner.nextLine());
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

        JSONArray executionResult = executor.executerMain(false); // execute the code
        for (int i = 0; i < executionResult.length(); i++) {
            var action = executionResult.getJSONObject(i);
            if (action.getInt("id") == 400) {
                throw new ASCErrors.ASCError(action.getJSONArray("p").getString(1), action.getJSONArray("p").getString(0));
            }
        }
        var contenuModule = new Hashtable<String, Statement>();

        executorInstance.obtenirCoordCompileDict().put(nomModule, contenuModule);
        return null;
    }
}
