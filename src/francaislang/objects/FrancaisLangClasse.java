package francaislang.objects;

import francaislang.objects.function.FrancaisLangFonctionModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangClasse extends FrancaisLangObj<FrancaisLangClasse> {
    private final String nom;

    public FrancaisLangClasse(String nom, Map<String, FrancaisLangObj<?>> proprietes) {
        super(proprietes);
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    /**
     * Le premier argument de la méthode sera toujours
     *
     * @param methode
     */
    public FrancaisLangClasse ajouterMethode(FrancaisLangFonctionModule methode) {
        methode.before(params -> {
            var p = new ArrayList<FrancaisLangObj<?>>(List.of(this));
            p.addAll(params);
            return p;
        });
        setPropriete(methode.getNom(), methode);
        return this;
    }

    /**
     * Le premier argument de la méthode sera toujours
     *
     * @param methode
     */
    public FrancaisLangClasse ajouterMethodeStatique(FrancaisLangFonctionModule methode) {
        setPropriete(methode.getNom(), methode);
        return this;
    }

    @Override
    public String toString() {
        return "Classe " + nom;
    }

    @Override
    public FrancaisLangType getType() {
        return new FrancaisLangType("un objet de type " + nom);
    }
}
