package francaislang.objects.function;

import francaislang.objects.FrancaisLangObj;

import java.util.List;
import java.util.function.Function;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangFonctionModule extends FrancaisLangCallable {
    private String nom;
    private final List<FrancaisLangParam> params;

    private Function<List<FrancaisLangObj<?>>, FrancaisLangObj<?>> callback;

    public FrancaisLangFonctionModule(String nom, List<FrancaisLangParam> params, Function<List<FrancaisLangObj<?>>, FrancaisLangObj<?>> callback) {
        this.nom = nom;
        this.params = params;
        this.callback = callback;
    }

    public FrancaisLangFonctionModule(String nom, Function<List<FrancaisLangObj<?>>, FrancaisLangObj<?>> callback) {
        this(nom, List.of(), callback);
    }

    @Override
    public FrancaisLangObj<?> appeler(List<FrancaisLangObj<?>> args) {
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

    public List<FrancaisLangParam> getParams() {
        return params;
    }

    public Function<List<FrancaisLangObj<?>>, FrancaisLangObj<?>> getCallback() {
        return callback;
    }

    public void before(Function<List<FrancaisLangObj<?>>, List<FrancaisLangObj<?>>> before) {
        this.callback = this.callback.compose(before);
    }

    @Override
    public String toString() {
        return getValue().toString().replace(".", ",");
    }
}
