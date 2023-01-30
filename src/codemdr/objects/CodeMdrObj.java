package codemdr.objects;

import org.ascore.lang.objects.ASCObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CodeMdrObj<T> extends ASCObject<T> {
    private final HashMap<String, CodeMdrObj<?>> proprietes;
    public static final CodeMdrObj<?> AUCUNE_VALEUR = new CodeMdrObj<>(0) {
    };

    protected CodeMdrObj(@NotNull T value, Map<String, CodeMdrObj<?>> proprietes) {
        super(value);
        this.proprietes = new HashMap<>(proprietes);
    }

    protected CodeMdrObj(@NotNull T value) {
        this(value, new HashMap<>());
    }

    public Optional<CodeMdrObj<?>> getPropriete(String nom) {
        return Optional.ofNullable(proprietes.getOrDefault(nom, null));
    }

    // Met à jour ou déclare la propriété et y associe la valeur `valeur`.
    public void setPropriete(String nom, CodeMdrObj<?> valeur) {
        proprietes.put(nom, valeur);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
