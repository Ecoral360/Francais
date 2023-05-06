package codemdr.objects.function;

import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrType;
import codemdr.objects.CodeMdrTypePrimitif;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CodeMdrCallable extends CodeMdrObj<Object> {
    protected CodeMdrCallable() {
        super(0);
    }

    public abstract CodeMdrObj<?> appeler(List<CodeMdrObj<?>> args);

    public abstract boolean estProcedure();

    @Override
    public CodeMdrType getType() {
        return CodeMdrTypePrimitif.FONCTION;
    }
}
