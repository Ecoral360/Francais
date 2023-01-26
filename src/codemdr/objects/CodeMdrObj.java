package codemdr.objects;

import org.ascore.lang.objects.ASCObject;
import org.jetbrains.annotations.NotNull;

public class CodeMdrObj<T> extends ASCObject<T> {
    public static final CodeMdrObj<?> AUCUNE_VALEUR = new CodeMdrObj<>(0);

    protected CodeMdrObj(@NotNull T value) {
        super(value);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
