package codemdr

import codemdr.execution.CodeMdrExecutorState
import codemdr.execution.CodeMdrPreCompiler
import codemdr.lexer.CodeMdrLexer
import codemdr.parser.CodeMdrParser
import org.ascore.executor.ASCExecutor
import org.ascore.executor.ASCExecutorBuilder

val CODE = """
    
    imprimer « Bonjour, Monde! »
    
    
    """.trimIndent()

fun main() {
    val lexer = CodeMdrLexer("/codemdr/grammar_rules/Grammar.yaml");
    val executor = ASCExecutorBuilder<CodeMdrExecutorState>() // create an executor builder
        .withLexer(lexer) // add the lexer to the builder
        .withParser { executorInstance: ASCExecutor<CodeMdrExecutorState> ->
            CodeMdrParser(
                executorInstance
            )
        } // add the parser to the builder
        .withExecutorState(CodeMdrExecutorState()) // add the executor state to the builder
        .withPrecompiler(CodeMdrPreCompiler()) // add the precompiler to the builder
        .build() // build the executor

    val compilationResult = executor.compile(CODE, true) // compile the code

    if (compilationResult.length() != 0) {
        println(compilationResult)
        return
    }
    val executionResult = executor.executerMain(false) // execute the code

    // println(executionResult) // print the result

}
