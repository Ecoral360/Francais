package francaislang.objects;

import francaislang.objects.function.FrancaisLangFonctionModule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FrancaisLangTableau extends FrancaisLangObj<List<FrancaisLangObj<?>>> {

    public FrancaisLangTableau(@NotNull List<FrancaisLangObj<?>> value) {
        super(value, Map.ofEntries(
                Map.entry("Taille", new FrancaisLangEntier(value.size()))
        ));

        setPropriete("Ajouter", new FrancaisLangFonctionModule("Ajouter", params -> {
            value.add(params.get(0));
            setPropriete("Taille", new FrancaisLangEntier(value.size()));
            return AUCUNE_VALEUR;
        }));

        setPropriete("AjouterTout", new FrancaisLangFonctionModule("AjouterTout", params -> {
            value.addAll(((FrancaisLangTableau) params.get(0)).getValue());
            setPropriete("Taille", new FrancaisLangEntier(value.size()));
            return AUCUNE_VALEUR;
        }));

        setPropriete("SousSection", new FrancaisLangFonctionModule("SousSection", params -> {
            int indexDebut = ((FrancaisLangEntier) params.get(0)).getValue();
            int indexFin = ((FrancaisLangEntier) params.get(1)).getValue();
            return new FrancaisLangTableau(getValue().subList(indexDebut, indexFin));
        }));
    }

    public FrancaisLangTableau() {
        this(new ArrayList<>());
    }

    public void setValeur(int position, FrancaisLangObj<?> valeur) {
        getValue().set(position, valeur);
    }

    public FrancaisLangObj<?> getValeur(int position) {
        return getValue().get(position);
    }

    public int taille() {
        return getValue().size();
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.TABLEAU;
    }
}
