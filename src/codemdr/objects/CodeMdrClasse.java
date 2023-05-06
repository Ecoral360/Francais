package codemdr.objects;

import codemdr.ast.expressions.CreationTableauExpr;
import codemdr.objects.function.CodeMdrFonctionModule;
import org.ascore.errors.ASCErrors;
import org.ascore.tokens.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrClasse extends CodeMdrObj<CodeMdrClasse> {
    private final String nom;

    public CodeMdrClasse(String nom, Map<String, CodeMdrObj<?>> proprietes) {
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
    public CodeMdrClasse ajouterMethode(CodeMdrFonctionModule methode) {
        methode.before(params -> {
            var p = new ArrayList<CodeMdrObj<?>>(List.of(this));
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
    public CodeMdrClasse ajouterMethodeStatique(CodeMdrFonctionModule methode) {
        setPropriete(methode.getNom(), methode);
        return this;
    }

    @Override
    public String toString() {
        return "Classe " + nom;
    }

    @Override
    public CodeMdrType getType() {
        return new CodeMdrType("un objet de type " + nom);
    }
}
