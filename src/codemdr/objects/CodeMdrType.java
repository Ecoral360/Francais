package codemdr.objects;

import org.ascore.errors.ASCErrors;
import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrType extends CodeMdrObj<CodeMdrType.CodeMdrTypes> {
    public CodeMdrType(CodeMdrTypes value) {
        super(value);
    }

    public CodeMdrType(String s) {
        super(CodeMdrTypes.fromString(s));
    }

    public CodeMdrType(Token token) {
        this(token.value());
    }

    @Override
    public String toString() {
        return getValue().name();
    }

    public enum CodeMdrTypes {
        BOOLEEN("un booléen"),
        TEXTE("du texte"),
        NOMBRE("un nombre"),
        NOMBRE_DECIMAL("un nombre décimal"),
        NOMBRE_ENTIER("un nombre entier"),
        GRAND_NOMBRE_ENTIER("un grand nombre entier"),
        TABLEAU("un tableau"),
        FONCTION("une fonction");

        private final String syntax;

        CodeMdrTypes(String syntaxe) {
            this.syntax = syntaxe;
        }

        public static CodeMdrTypes fromString(String s) {
            for (var type : values()) {
                if (type.syntax.equals(s)) {
                    return type;
                }
            }
            throw new ASCErrors.ErreurType("Type invalide " + s);
        }
    }
}
