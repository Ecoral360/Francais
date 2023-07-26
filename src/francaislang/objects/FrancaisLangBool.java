package francaislang.objects;

import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangBool extends FrancaisLangObj<Boolean> {
    public FrancaisLangBool(Number value) {
        super(value.doubleValue() == 1);
    }

    public FrancaisLangBool(Boolean bool) {
        super(bool);
    }

    public FrancaisLangBool(Token token) {
        super(token.value().equals("VRAI"));
    }

    @Override
    public String toString() {
        return getValue() ? "VRAI" : "FAUX";
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.BOOLEEN;
    }
}
