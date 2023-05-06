package codemdr.objects;

import codemdr.objects.function.CodeMdrFonctionModule;
import codemdr.objects.function.CodeMdrParam;
import org.ascore.tokens.Token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrUnsignedInt extends CodeMdrInt {
    public CodeMdrUnsignedInt(Number value) {
        super(value);
    }

    public CodeMdrUnsignedInt(Token token) {
        this(Integer.parseUnsignedInt(token.value()));
    }

    @Override
    public String toString() {
        return Integer.toUnsignedString(getValue());
    }

    @Override
    public CodeMdrType getType() {
        return CodeMdrTypePrimitif.NOMBRE_ENTIER;
    }
}
