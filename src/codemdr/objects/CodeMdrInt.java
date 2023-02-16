package codemdr.objects;

import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrInt extends CodeMdrNumber {
    public CodeMdrInt(Number value) {
        super(value);
    }

    public CodeMdrInt(Token token) {
        this(Integer.parseInt(token.value()));
    }


    @Override
    public CodeMdrType getType() {
        return CodeMdrTypePrimitif.NOMBRE_ENTIER;
    }
}
