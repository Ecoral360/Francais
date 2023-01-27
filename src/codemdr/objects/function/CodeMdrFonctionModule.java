package codemdr.objects.function;

import codemdr.objects.CodeMdrObj;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;
import java.util.function.Function;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrFonctionModule extends CodeMdrObj<Object> implements CodeMdrCallable {
    private String nom;
    private List<CodeMdrParam> params;

    private Function<List<CodeMdrObj<?>>, CodeMdrObj<?>> callback;

    public CodeMdrFonctionModule(String nom, List<CodeMdrParam> params, Function<List<CodeMdrObj<?>>, CodeMdrObj<?>> callback) {
        super(0);
        this.nom = nom;
        this.params = params;
        this.callback = callback;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return getValue().toString().replace(".", ",");
    }
}
