package codemdr.objects;

import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrString extends CodeMdrObj<String> {
    public CodeMdrString(String value) {
        super(value);
    }

    public CodeMdrString(Token token) {
        this(token.value().substring(2, token.value().length() - 2)); // removing the enclosing `"` from the string
    }
}
