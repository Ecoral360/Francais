package codemdr.objects

import org.ascore.lang.objects.ASCObject
import org.ascore.tokens.Token

/**
 * An example of an object for the CodeMdr programming main.language
 */
class CodeMdrString(value: String) : ASCObject<String>(value) {
    constructor(token: Token) : this(
        token.value().substring(1, token.value().length - 1) // removing the enclosing `"` from the string
    )
}
