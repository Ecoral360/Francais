package francaislang.objects;

import francaislang.objects.function.FrancaisLangFonctionModule;
import org.ascore.lang.objects.ASCVariable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FrancaisLangModule extends FrancaisLangObj<Object> {
    public FrancaisLangModule(@NotNull Object value, FrancaisLangFonctionModule[] fonctions, ASCVariable<?>[] variables) {
        super(value, creerDictFonction(fonctions, variables));
    }

    private static Map<String, FrancaisLangObj<?>> creerDictFonction(FrancaisLangFonctionModule[] fonctions, ASCVariable<?>[] variables) {
        HashMap<String, FrancaisLangObj<?>> fonctionDict = new HashMap<>();
        for (var fonction : fonctions) {
            fonctionDict.put(fonction.getNom(), fonction);
        }

        for (var variable : variables) {
            fonctionDict.put(variable.getName(), (FrancaisLangObj<?>) variable.getAscObject());
        }
        return fonctionDict;
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.MODULE;
    }
}
