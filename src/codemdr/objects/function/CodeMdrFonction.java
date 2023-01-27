package codemdr.objects.function;

import codemdr.objects.CodeMdrObj;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrFonction extends CodeMdrObj<Object> implements CodeMdrCallable {
    private String nom;
    private List<ASCVariable<?>> params;

    public CodeMdrFonction(String nom, List<ASCVariable<?>> params) {
        super(0);
        this.nom = nom;
        this.params = params;

    }

    @Override
    public String toString() {
        return getValue().toString().replace(".", ",");
    }

    public static class CodeMdrFonctionInstance extends CodeMdrObj<Object> {
        private final CodeMdrFonction fonctionParent;

        protected CodeMdrFonctionInstance(CodeMdrFonction fonctionParent) {
            super(0);
            this.fonctionParent = fonctionParent;
        }

    }
}
