package codemdr.objects;

import org.ascore.errors.ASCErrors;
import org.ascore.tokens.Token;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrClasse extends CodeMdrObj<CodeMdrClasse> {
    private final String nom;
    private final String type;

    public CodeMdrClasse(String nom, String type) {
        this.nom = nom;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "Classe " + nom;
    }

    @Override
    public CodeMdrType getType() {
        return new CodeMdrType(type);
    }
}
