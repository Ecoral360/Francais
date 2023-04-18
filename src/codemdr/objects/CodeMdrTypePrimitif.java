package codemdr.objects;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public final class CodeMdrTypePrimitif extends CodeMdrType {

    public final static CodeMdrTypePrimitif TYPE = new CodeMdrTypePrimitif("un type");
    public final static CodeMdrTypePrimitif MODULE = new CodeMdrTypePrimitif("un module");
    public final static CodeMdrTypePrimitif OBJET = new CodeMdrTypePrimitif("un objet");
    public final static CodeMdrTypePrimitif NULLE = new CodeMdrTypePrimitif("une valeur nulle");
    public final static CodeMdrTypePrimitif BOOLEEN = new CodeMdrTypePrimitif("un booléen");
    public final static CodeMdrTypePrimitif TEXTE = new CodeMdrTypePrimitif("du texte");
    public final static CodeMdrTypePrimitif NOMBRE = new CodeMdrTypePrimitif("un nombre");
    public final static CodeMdrTypePrimitif NOMBRE_DECIMAL = new CodeMdrTypePrimitif("un nombre décimal");
    public final static CodeMdrTypePrimitif NOMBRE_ENTIER = new CodeMdrTypePrimitif("un nombre entier");
    public final static CodeMdrTypePrimitif GRAND_NOMBRE_ENTIER = new CodeMdrTypePrimitif("un grand nombre entier");
    public final static CodeMdrTypePrimitif TABLEAU = new CodeMdrTypePrimitif("un tableau");
    public final static CodeMdrTypePrimitif FONCTION = new CodeMdrTypePrimitif("une fonction");

    // no extenrn instances
    private CodeMdrTypePrimitif(String value) {
        super(value);
    }

    @Override
    public CodeMdrTypePrimitif getType() {
        return CodeMdrTypePrimitif.TYPE;
    }
}
