package kot.codemdr.execution

import org.ascore.errors.ASCErrors.LexingError
import org.ascore.executor.ASCPrecompiler
import org.ascore.tokens.Token

/**
 * This class is used to precompile the source code. Edit the [preCompileBeforeStatementSplit] method to change the precompilation behavior.
 *
 * PreCompilation is **guarantied** to be executed before the compilation of the source code.
 */
class CodeMdrPreCompiler : ASCPrecompiler() {
    /**
     * This method is used to precompile the source code.
     *
     * @param program The source code to precompile as an array of strings.
     * @return The precompiled source code as an array of strings. (aka the actual code that will be compiled)
     */
    override fun preCompile(program: String): String {
        return program
    }

    override fun preCompileBeforeStatementSplit(tokens: MutableList<Token>): MutableList<Token> {
        return tokens
    }

    override fun preCompileAfterStatementSplit(tokens: MutableList<MutableList<Token>>): MutableList<MutableList<Token>> {
        for (statement in tokens) {
            if (statement.isEmpty()) continue
            val firstToken = statement[0]
            if (firstToken.value[0] != firstToken.value.uppercase()[0]) {
                throw LexingError("Every statement should start with an uppercase")
            }
            // statement[0] = Token(
            //     firstToken.name,
            //     firstToken.value.replaceFirstChar { it.lowercase() },
            //     firstToken.category,
            //     firstToken.start,
            //     firstToken.tokenRuleParent,
            //     firstToken.match
            // )
        }
        return tokens
    }
}
