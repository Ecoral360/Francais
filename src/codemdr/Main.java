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
            Posons que Liste vaut un tableau contenant 1, "un tableau contenant 1, 3, « Allo » et 2" et 4.
            Imprimer Liste.
            Maintenant, Liste vaut un tableau contenant un tableau contenant seulement 1 et 2.
            Imprimer Liste.
            Maintenant, Liste vaut un tableau contenant 1 et "un tableau contenant 1, 3 et « Allo »".
            Imprimer Liste.
            """;

    private static final String CODE_3 = """
            Imprimer « Faire la somme de 1, 2 et 4! ».
            Posons que Résultat vaut l'appel à Somme avec les arguments 1, 2 et 4.

            Note à moi-même : Ce code sera ignoré, il est dans un commentaire :).

            Imprimer Résultat.
            """;

    private static final String CODE_4 = """
            Posons que I vaut 0.

            Exécuter 2 énoncés tant que I < 10.
                Imprimer I.
                Maintenant, I vaut I plus 1.

            Imprimer « On a fini 3! ».
            Imprimer Vrai.
            """;

    private static final String CODE_6 = """
            Posons que I vaut 11.

            Exécuter 2 énoncés si I < 10 ; sinon, sauter 1 énoncé puis exécuter 1 énoncé.
                Imprimer I.
                Maintenant, I vaut I plus 1.

            Imprimer « On a fini 1 ! ».
            Imprimer I.
                        
            Posons que X vaut 1.

            Exécuter 2 énoncés si X < 10.
                Imprimer X.
                Maintenant, X vaut X plus 1.

            Imprimer « On a fini 2 ! ».
            Imprimer X.
            """;

    private static final String CODE_5 = """
            Posons que MaListe vaut un tableau contenant 1, « Bonjour », un tableau contenant seulement 10 et 22.
            Imprimer élément de MaListe à la position 2.
            Maintenant, élément de MaListe à la position 2 vaut 23.
            Imprimer élément de MaListe à la position 2.
            """;

    private static final String CODE = """
            Demander un nombre entier
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
        // System.out.println(executionResult); // print the result
    }
}
