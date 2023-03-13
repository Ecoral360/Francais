package codemdr.objects;

import codemdr.objects.function.CodeMdrFonctionModule;
import codemdr.objects.function.CodeMdrParam;
import org.ascore.tokens.Token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrInt extends CodeMdrNumber {
    public CodeMdrInt(Number value) {
        super(value);

        setPropriete("DécalageÀGauche",
                new CodeMdrFonctionModule("DécalageÀGauche", List.of(new CodeMdrParam("BitsDécalées")),
                        args -> new CodeMdrInt(
                                getValue() << ((CodeMdrNumber) args.get(0)).getValue().intValue()
                        )));
        setPropriete("DécalageÀDroite",
                new CodeMdrFonctionModule("DécalageÀDroite", List.of(new CodeMdrParam("BitsDécalées")),
                        args -> new CodeMdrInt(
                                getValue() >> ((CodeMdrNumber) args.get(0)).getValue().intValue()
                        )));

        setPropriete("InverseBinaire",
                new CodeMdrFonctionModule("InverseBinaire", List.of(), args -> new CodeMdrInt(~getValue())));

        setPropriete("TableauBinaire",
                new CodeMdrFonctionModule("TableauBinaire", List.of(),
                        args -> new CodeMdrTableau(Integer.toBinaryString(getValue()).chars().mapToObj(
                                i -> new CodeMdrInt(Integer.parseInt((char) i + ""))
                        ).collect(Collectors.toList()))));
    }

    public CodeMdrInt(Token token) {
        this(Integer.parseInt(token.value()));
    }

    @Override
    public Integer getValue() {
        return super.getValue().intValue();
    }

    @Override
    public CodeMdrType getType() {
        return CodeMdrTypePrimitif.NOMBRE_ENTIER;
    }
}
