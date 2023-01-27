package codemdr.objects.function;

import codemdr.objects.CodeMdrObj;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CodeMdrCallable extends CodeMdrObj<Object> {
    protected CodeMdrCallable() {
        super(0);
    }

    public abstract CodeMdrObj<?> appeler(List<CodeMdrObj<?>> args);

    public abstract boolean estProcedure();
}
