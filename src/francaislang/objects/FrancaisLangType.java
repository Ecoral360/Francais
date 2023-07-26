package francaislang.objects;

import org.ascore.errors.ASCErrors;
import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangType extends FrancaisLangObj<FrancaisLangType.FrancaisLangBuiltinTypes> {
    public FrancaisLangType(FrancaisLangBuiltinTypes value) {
        super(value);
    }

    public FrancaisLangType(String s) {
        super(FrancaisLangBuiltinTypes.fromString(s));
    }

    public FrancaisLangType(Token token) {
        this(token.value());
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangBuiltinTypes.TYPE.asType();
    }

    public enum FrancaisLangBuiltinTypes {
        TYPE("un type"),
        MODULE("un module"),
        OBJET("un objet"),
        NULLE("une valeur nulle"),
        BOOLEEN("un booléen"),
        TEXTE("du texte"),
        NOMBRE("un nombre"),
        NOMBRE_DECIMAL("un nombre décimal"),
        NOMBRE_ENTIER("un nombre entier"),
        GRAND_NOMBRE_ENTIER("un grand nombre entier"),
        TABLEAU("un tableau"),
        FONCTION("une fonction");

        private final String syntax;

        FrancaisLangBuiltinTypes(String syntaxe) {
            this.syntax = syntaxe;
        }

        public FrancaisLangType asType() {
            return new FrancaisLangType(this);
        }

        public static FrancaisLangBuiltinTypes fromString(String s) {
            for (var type : values()) {
                if (type.syntax.equals(s)) {
                    return type;
                }
            }
            throw new ASCErrors.ErreurType("Type invalide " + s);
        }

        @Override
        public String toString() {
            return syntax;
        }
    }
}
