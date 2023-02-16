package codemdr.objects.function;

import codemdr.objects.CodeMdrObj;
import org.ascore.lang.objects.ASCVariable;

import java.util.List;
import java.util.function.Function;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrFonctionModule extends CodeMdrCallable {
    private String nom;
    private final List<CodeMdrParam> params;

    private Function<List<CodeMdrObj<?>>, CodeMdrObj<?>> callback;

    public CodeMdrFonctionModule(String nom, List<CodeMdrParam> params, Function<List<CodeMdrObj<?>>, CodeMdrObj<?>> callback) {
        this.nom = nom;
        this.params = params;
        this.callback = callback;
    }

    public CodeMdrFonctionModule(String nom, Function<List<CodeMdrObj<?>>, CodeMdrObj<?>> callback) {
        this(nom, List.of(), callback);
    }

    @Override
    public CodeMdrObj<?> appeler(List<CodeMdrObj<?>> args) {
        return this.callback.apply(args);
    }

    @Override
    public boolean estProcedure() {
        return false;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<CodeMdrParam> getParams() {
        return params;
    }

    public Function<List<CodeMdrObj<?>>, CodeMdrObj<?>> getCallback() {
        return callback;
    }

    public void before(Function<List<CodeMdrObj<?>>, List<CodeMdrObj<?>>> before) {
        this.callback = this.callback.compose(before);
    }

    @Override
    public String toString() {
        return getValue().toString().replace(".", ",");
    }
}
