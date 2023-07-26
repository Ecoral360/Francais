package francaislang.objects.function;

import francaislang.objects.FrancaisLangObj;
import francaislang.objects.FrancaisLangType;
import francaislang.objects.FrancaisLangTypePrimitif;

public class FrancaisLangParam extends FrancaisLangObj<FrancaisLangParam> {
    private final String nom;

    public FrancaisLangParam(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.OBJET;
    }
}
