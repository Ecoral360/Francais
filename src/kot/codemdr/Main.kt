package kot.codemdr

import kot.codemdr.execution.CodeMdrExecutorState
import kot.codemdr.execution.CodeMdrPreCompiler
import kot.codemdr.lexer.CodeMdrLexer
import kot.codemdr.parser.CodeMdrParser
import org.ascore.executor.ASCExecutor
import org.ascore.executor.ASCExecutorBuilder

val CODE = """
    
    Imprimer 4,3.
    
    """.trimIndent()

fun main() {
    val lexer = CodeMdrLexer("/kot/codemdr/grammar_rules/Grammar.yaml");
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
