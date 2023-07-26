package francaislang.objects;

import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangEntierNonSigne extends FrancaisLangEntier {
    public FrancaisLangEntierNonSigne(Number value) {
        super(value);
    }

    public FrancaisLangEntierNonSigne(Token token) {
        this(Integer.parseUnsignedInt(token.value()));
    }

    @Override
    public String toString() {
        return Integer.toUnsignedString(getValue());
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.NOMBRE_ENTIER;
    }
}
