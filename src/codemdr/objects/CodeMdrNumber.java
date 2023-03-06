package codemdr.objects;

import codemdr.objects.function.CodeMdrFonctionModule;
import codemdr.objects.function.CodeMdrParam;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CodeMdrNumber extends CodeMdrObj<Number> {
    protected CodeMdrNumber(@NotNull Number value) {
        super(value);


    }

    @Override
    public CodeMdrType getType() {
        return CodeMdrTypePrimitif.NOMBRE;
    }
}
