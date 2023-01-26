package codemdr.objects;

import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrFloat extends CodeMdrNumber {
    public CodeMdrFloat(Number value) {
        super(value.doubleValue());
    }

    public CodeMdrFloat(Token token) {
        this(Double.parseDouble(token.value()));
    }

}
