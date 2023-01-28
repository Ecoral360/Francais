package codemdr;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.execution.CodeMdrPreCompiler;
import codemdr.lexer.CodeMdrJetoniseur;
import codemdr.module.CodeMdrModule;
import codemdr.module.CodeMdrModules;
import codemdr.parser.CodeMdrGASA;
import org.ascore.executor.ASCExecutorBuilder;
import org.json.JSONArray;

/**
 * Entry point for the CodeMdr language where you can try it out and experiment with it while developing it.
 */
public class Main {
    /**
     * The CODE lines to execute.
     */
    private static final String CODE_2 = """
            Imprimer « Bonjour! ».
            Posons que MonNombre vaut « Allo ».
            Imprimer MonNombre.
            Maintenant, MonNombre vaut 11,3.
            Imprimer MonNombre.
            Posons que Liste vaut un tableau contenant 1, un tableau contenant 1, 3, « Allo » et 2 et 4.
            Imprimer Liste.
            Maintenant, Liste vaut un tableau contenant un tableau contenant 1 et 2.
            Imprimer Liste.
            Maintenant, Liste vaut un tableau contenant 1 et un tableau contenant 1, 3 et « Allo ».
            Imprimer Liste.
            """;

    private static final String CODE_3 = """
            Imprimer « Faire la somme de 1, 2 et 4! ».
            Posons que Résultat vaut l'appel à Somme avec les paramètres 1, 2 et 4.
                        
            Note à moi-même : Ce code sera ignoré, il est dans un commentaire :).

            Imprimer Résultat.
            """;

    private static final String CODE = """
            Posons que I vaut 0.
                        
            Exécuter 2 énoncés tant que I < 10.
            Imprimer I.
            Maintenant, I vaut I + 1.

            Imprimer « On a fini! ».
            """;

    public static void main(String[] args) {
        var executor = new ASCExecutorBuilder<CodeMdrExecutorState>() // create an executor builder
                .withLexer(new CodeMdrJetoniseur("/codemdr/grammar_rules/Grammar.yaml")) // add the lexer to the builder
                .withParser(CodeMdrGASA::new) // add the parser to the builder
                .withExecutorState(new CodeMdrExecutorState()) // add the executor state to the builder
                .withPrecompiler(new CodeMdrPreCompiler()) // add the precompiler to the builder
                .build(); // build the executor
        JSONArray compilationResult = executor.compile(CODE, true); // compile the code
        if (compilationResult.length() != 0) {
            System.out.println(compilationResult);
            return;
        }

        CodeMdrModules.charger(executor.getExecutorState());

        JSONArray executionResult = executor.executerMain(false); // execute the code
        System.out.println(executionResult); // print the result
    }
}
