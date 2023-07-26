package francaislang.objects;

import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangDecimal extends FrancaisLangNombre {
    public FrancaisLangDecimal(Number value) {
        super(value.doubleValue());
    }

    public FrancaisLangDecimal(Token token) {
        super(Double.parseDouble(token.value().replace(",", ".")));
    }

    @Override
    public String toString() {
        return getValue().toString().replace(".", ",");
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.NOMBRE_DECIMAL;
    }
}
