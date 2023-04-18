package codemdr.objects;

import org.ascore.errors.ASCErrors;
import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrType extends CodeMdrObj<CodeMdrType.CodeMdrBuiltinTypes> {
    public CodeMdrType(CodeMdrBuiltinTypes value) {
        super(value);
    }

    public CodeMdrType(String s) {
        super(CodeMdrBuiltinTypes.fromString(s));
    }

    public CodeMdrType(Token token) {
        this(token.value());
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public CodeMdrType getType() {
        return CodeMdrBuiltinTypes.TYPE.asType();
    }

    public enum CodeMdrBuiltinTypes {
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

        CodeMdrBuiltinTypes(String syntaxe) {
            this.syntax = syntaxe;
        }

        public CodeMdrType asType() {
            return new CodeMdrType(this);
        }

        public static CodeMdrBuiltinTypes fromString(String s) {
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
