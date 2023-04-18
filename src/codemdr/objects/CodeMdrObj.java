package codemdr.objects;

import org.ascore.lang.objects.ASCObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class CodeMdrObj<T> extends ASCObject<T> {
    private final HashMap<String, CodeMdrObj<?>> proprietes;
    private final HashMap<String, Supplier<CodeMdrObj<?>>> lazyProprietes = new HashMap<>();

    public static final CodeMdrObj<?> AUCUNE_VALEUR = new CodeMdrObj<>() {
        @Override
        public CodeMdrType getType() {
            return CodeMdrTypePrimitif.NULLE;
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> CodeMdrObj<T> aucuneValeur() {
        return (CodeMdrObj<T>) AUCUNE_VALEUR;
    }

    protected CodeMdrObj(@NotNull T value, Map<String, CodeMdrObj<?>> proprietes) {
        super(value);
        this.proprietes = new HashMap<>(proprietes);
        initProprietesCommunes();
    }

    protected CodeMdrObj(@NotNull T value) {
        this(value, new HashMap<>());
        initProprietesCommunes();
    }

    protected CodeMdrObj(Map<String, CodeMdrObj<?>> proprietes) {
        super();
        this.proprietes = new HashMap<>(proprietes);
        initProprietesCommunes();
    }

    protected CodeMdrObj() {
        super();
        this.proprietes = new HashMap<>();
        initProprietesCommunes();
    }

    public Optional<CodeMdrObj<?>> getPropriete(String nom) {
        return Optional.ofNullable(proprietes.getOrDefault(nom, lazyProprietes.getOrDefault(nom, () -> null).get()));
    }

    private void initProprietesCommunes() {
        this.setLazyPropriete("Type", () -> new CodeMdrString(this.getType().toString()));
    }

    /**
     * Met à jour ou déclare la propriété et y associe la valeur <code>valeur</code>.
     */
    public void setPropriete(String nom, CodeMdrObj<?> valeur) {
        proprietes.put(nom, valeur);
    }
    /**
     * Met à jour ou déclare la propriété et y associe la valeur <code>valeur</code>.
     */
    public void setLazyPropriete(String nom, Supplier<CodeMdrObj<?>> valeur) {
        lazyProprietes.put(nom, valeur);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeMdrObj<?> that)) return false;

        return Objects.equals(proprietes, that.proprietes) && getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return proprietes != null ? proprietes.hashCode() : 0;
    }

    public abstract CodeMdrType getType();
}
