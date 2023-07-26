package francaislang.objects;

import org.jetbrains.annotations.NotNull;


public abstract class FrancaisLangNombre extends FrancaisLangObj<Number> {
    protected FrancaisLangNombre(@NotNull Number value) {
        super(value);
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.NOMBRE;
    }
}
