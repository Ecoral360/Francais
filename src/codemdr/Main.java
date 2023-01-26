package codemdr;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.execution.CodeMdrPreCompiler;
import codemdr.lexer.CodeMdrJetoniseur;
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
    private static final String CODE = """
            Posons que A vaut 10,3.
            Imprimer « Bonjour, Monde! ».
            Imprimer A.
            Maintenant, A vaut 12.
            Imprimer A.
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
        JSONArray executionResult = executor.executerMain(false); // execute the code
        // System.out.println(executionResult); // print the result
    }
}
