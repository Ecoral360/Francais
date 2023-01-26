package kot.codemdr.objects

import org.ascore.tokens.Token

/**
 * An example of an object for the CodeMdr programming main.language
 */
class CodeMdrInt(value: Int) : CodeMdrNumber(value) {
    constructor(token: Token) : this(token.value().toInt()) {}

    override fun getValue(): Int = super.getValue().toInt()
}
