package codemdr.objects

import org.ascore.tokens.Token

/**
 * An example of an object for the CodeMdr programming main.language
 */
class CodeMdrFloat(value: Double) : CodeMdrNumber(value) {
    constructor(token: Token) : this(token.value().toDouble()) {}

    override fun getValue(): Double = super.getValue().toDouble()
}
