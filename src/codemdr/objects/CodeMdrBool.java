package codemdr.objects;

import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrBool extends CodeMdrObj<Boolean> {
    public CodeMdrBool(Number value) {
        super(value.doubleValue() == 1);
    }

    public CodeMdrBool(Boolean bool) {
        super(bool);
    }

    public CodeMdrBool(Token token) {
        super(token.value().equals("VRAI"));
    }

    @Override
    public String toString() {
        return getValue() ? "VRAI" : "FAUX";
    }
}
