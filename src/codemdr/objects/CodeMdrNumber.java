package codemdr.objects;

import org.jetbrains.annotations.NotNull;


public abstract class CodeMdrNumber extends CodeMdrObj<Number> {
    protected CodeMdrNumber(@NotNull Number value) {
        super(value);
    }

    @Override
    public CodeMdrType getType() {
        return CodeMdrTypePrimitif.NOMBRE;
    }
}
