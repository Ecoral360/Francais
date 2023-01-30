package codemdr.objects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class CodeMdrTableau extends CodeMdrObj<ArrayList<CodeMdrObj<?>>> {

    public CodeMdrTableau(@NotNull ArrayList<CodeMdrObj<?>> value) {
        super(value, Map.ofEntries(
                Map.entry("Taille", new CodeMdrInt(value.size()))
        ));
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
}
