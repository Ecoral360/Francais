package codemdr.objects;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CodeMdrTableau extends CodeMdrObj<ArrayList<CodeMdrObj<?>>> {

    public CodeMdrTableau(@NotNull ArrayList<CodeMdrObj<?>> value) {
        super(value);
    }


}
