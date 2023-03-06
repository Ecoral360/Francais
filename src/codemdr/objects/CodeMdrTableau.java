package codemdr.objects;

import codemdr.objects.function.CodeMdrFonctionModule;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CodeMdrTableau extends CodeMdrObj<List<CodeMdrObj<?>>> {

    public CodeMdrTableau(@NotNull List<CodeMdrObj<?>> value) {
        super(value, Map.ofEntries(
                Map.entry("Taille", new CodeMdrInt(value.size()))
        ));

        setPropriete("Ajouter", new CodeMdrFonctionModule("Ajouter", params -> {
            value.add(params.get(0));
            setPropriete("Taille", new CodeMdrInt(value.size()));
            return AUCUNE_VALEUR;
        }));

        setPropriete("AjouterTout", new CodeMdrFonctionModule("AjouterTout", params -> {
            value.addAll(((CodeMdrTableau) params.get(0)).getValue());
            setPropriete("Taille", new CodeMdrInt(value.size()));
            return AUCUNE_VALEUR;
        }));
    }

    public CodeMdrTableau() {
        this(new ArrayList<>());
    }

    public void setValeur(int position, CodeMdrObj<?> valeur) {
        getValue().set(position, valeur);
    }

    public CodeMdrObj<?> getValeur(int position) {
        return getValue().get(position);
    }

    public int taille() {
        return getValue().size();
    }

    @Override
    public CodeMdrType getType() {
        return CodeMdrTypePrimitif.TABLEAU;
    }
}
