package francaislang.objects;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public final class FrancaisLangTypePrimitif extends FrancaisLangType {

    public final static FrancaisLangTypePrimitif TYPE = new FrancaisLangTypePrimitif("un type");
    public final static FrancaisLangTypePrimitif MODULE = new FrancaisLangTypePrimitif("un module");
    public final static FrancaisLangTypePrimitif OBJET = new FrancaisLangTypePrimitif("un objet");
    public final static FrancaisLangTypePrimitif NULLE = new FrancaisLangTypePrimitif("une valeur nulle");
    public final static FrancaisLangTypePrimitif BOOLEEN = new FrancaisLangTypePrimitif("un booléen");
    public final static FrancaisLangTypePrimitif TEXTE = new FrancaisLangTypePrimitif("du texte");
    public final static FrancaisLangTypePrimitif NOMBRE = new FrancaisLangTypePrimitif("un nombre");
    public final static FrancaisLangTypePrimitif NOMBRE_DECIMAL = new FrancaisLangTypePrimitif("un nombre décimal");
    public final static FrancaisLangTypePrimitif NOMBRE_ENTIER = new FrancaisLangTypePrimitif("un nombre entier");
    public final static FrancaisLangTypePrimitif GRAND_NOMBRE_ENTIER = new FrancaisLangTypePrimitif("un grand nombre entier");
    public final static FrancaisLangTypePrimitif TABLEAU = new FrancaisLangTypePrimitif("un tableau");
    public final static FrancaisLangTypePrimitif FONCTION = new FrancaisLangTypePrimitif("une fonction");

    // no extenrn instances
    private FrancaisLangTypePrimitif(String value) {
        super(value);
    }

    @Override
    public FrancaisLangTypePrimitif getType() {
        return FrancaisLangTypePrimitif.TYPE;
    }
}
