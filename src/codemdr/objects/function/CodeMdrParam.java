package codemdr.objects.function;

import codemdr.objects.CodeMdrObj;

public class CodeMdrParam extends CodeMdrObj<Object> {
    private final String nom;

    public CodeMdrParam(String nom) {
        super(0);
        this.nom = nom;
    }
}
