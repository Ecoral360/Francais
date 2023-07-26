package francaislang.objects;

import org.ascore.lang.objects.ASCObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class FrancaisLangObj<T> extends ASCObject<T> {
    private final HashMap<String, FrancaisLangObj<?>> proprietes;
    private final HashMap<String, Supplier<FrancaisLangObj<?>>> lazyProprietes = new HashMap<>();

    public static final FrancaisLangObj<?> AUCUNE_VALEUR = new FrancaisLangObj<>() {
        @Override
        public FrancaisLangType getType() {
            return FrancaisLangTypePrimitif.NULLE;
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> FrancaisLangObj<T> aucuneValeur() {
        return (FrancaisLangObj<T>) AUCUNE_VALEUR;
    }

    protected FrancaisLangObj(@NotNull T value, Map<String, FrancaisLangObj<?>> proprietes) {
        super(value);
        this.proprietes = new HashMap<>(proprietes);
        initProprietesCommunes();
    }

    protected FrancaisLangObj(@NotNull T value) {
        this(value, new HashMap<>());
        initProprietesCommunes();
    }

    protected FrancaisLangObj(Map<String, FrancaisLangObj<?>> proprietes) {
        super();
        this.proprietes = new HashMap<>(proprietes);
        initProprietesCommunes();
    }

    protected FrancaisLangObj() {
        super();
        this.proprietes = new HashMap<>();
        initProprietesCommunes();
    }

    public Optional<FrancaisLangObj<?>> getPropriete(String nom) {
        return Optional.ofNullable(proprietes.getOrDefault(nom, lazyProprietes.getOrDefault(nom, () -> null).get()));
    }

    private void initProprietesCommunes() {
        this.setLazyPropriete("Type", () -> new FrancaisLangTexte(this.getType().toString()));
    }

    /**
     * Met à jour ou déclare la propriété et y associe la valeur <code>valeur</code>.
     */
    public void setPropriete(String nom, FrancaisLangObj<?> valeur) {
        proprietes.put(nom, valeur);
    }
    /**
     * Met à jour ou déclare la propriété et y associe la valeur <code>valeur</code>.
     */
    public void setLazyPropriete(String nom, Supplier<FrancaisLangObj<?>> valeur) {
        lazyProprietes.put(nom, valeur);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrancaisLangObj<?> that)) return false;

        return Objects.equals(proprietes, that.proprietes) && getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return proprietes != null ? proprietes.hashCode() : 0;
    }

    public abstract FrancaisLangType getType();
}
