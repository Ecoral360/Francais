package kot.codemdr.lexer

import org.ascore.lang.ASCLexer
import java.util.regex.Pattern

/**
 * This class is used to lex the source code. Override and edit the [lex] method to change the
 * lexing behavior.
 */
class CodeMdrLexer(filePath: String) : ASCLexer(CodeMdrLexer::class.java.getResourceAsStream(filePath)) {
    override fun sortTokenRules() {
        for (rule in tokenRules) {
            rule.flags = Pattern.MULTILINE or Pattern.UNICODE_CHARACTER_CLASS
        }
    }
}
