package francaislang.objects.function;

import francaislang.objects.FrancaisLangObj;
import francaislang.objects.FrancaisLangType;
import francaislang.objects.FrancaisLangTypePrimitif;

import java.util.List;

public abstract class FrancaisLangCallable extends FrancaisLangObj<Object> {
    protected FrancaisLangCallable() {
        super(0);
    }

    public abstract FrancaisLangObj<?> appeler(List<FrancaisLangObj<?>> args);

    public abstract boolean estProcedure();

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.FONCTION;
    }
}
