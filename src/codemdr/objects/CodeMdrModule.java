package codemdr.objects;

import codemdr.objects.function.CodeMdrFonctionModule;
import org.ascore.lang.objects.ASCVariable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CodeMdrModule extends CodeMdrObj<Object> {
    public CodeMdrModule(@NotNull Object value, CodeMdrFonctionModule[] fonctions, ASCVariable<?>[] variables) {
        super(value, creerDictFonction(fonctions, variables));
    }

    private static Map<String, CodeMdrObj<?>> creerDictFonction(CodeMdrFonctionModule[] fonctions, ASCVariable<?>[] variables) {
        HashMap<String, CodeMdrObj<?>> fonctionDict = new HashMap<>();
        for (var fonction : fonctions) {
            fonctionDict.put(fonction.getNom(), fonction);
        }

        for (var variable : variables) {
            fonctionDict.put(variable.getName(), (CodeMdrObj<?>) variable.getAscObject());
        }
        return fonctionDict;
    }
}
