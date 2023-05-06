package codemdr.objects.function;

import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrType;
import codemdr.objects.CodeMdrTypePrimitif;

public class CodeMdrParam extends CodeMdrObj<CodeMdrParam> {
    private final String nom;

    public CodeMdrParam(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public CodeMdrType getType() {
        return CodeMdrTypePrimitif.OBJET;
    }
}
