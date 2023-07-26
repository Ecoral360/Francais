package francaislang.objects;

import francaislang.objects.function.FrancaisLangFonctionModule;
import francaislang.objects.function.FrancaisLangParam;
import org.ascore.tokens.Token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangEntier extends FrancaisLangNombre {
    public FrancaisLangEntier(Number value) {
        super(value);

        setPropriete("DécalageÀGauche",
                new FrancaisLangFonctionModule("DécalageÀGauche", List.of(new FrancaisLangParam("BitsDécalées")),
                        args -> new FrancaisLangEntier(
                                getValue() << ((FrancaisLangNombre) args.get(0)).getValue().intValue()
                        )));
        setPropriete("DécalageÀDroite",
                new FrancaisLangFonctionModule("DécalageÀDroite", List.of(new FrancaisLangParam("BitsDécalées")),
                        args -> new FrancaisLangEntier(
                                getValue() >> ((FrancaisLangNombre) args.get(0)).getValue().intValue()
                        )));

        setPropriete("InverseBinaire",
                new FrancaisLangFonctionModule("InverseBinaire", List.of(), args -> new FrancaisLangEntier(~getValue())));

        setPropriete("TableauBinaire",
                new FrancaisLangFonctionModule("TableauBinaire", List.of(),
                        args -> new FrancaisLangTableau(Integer.toBinaryString(getValue()).chars().mapToObj(
                                i -> new FrancaisLangEntier(Integer.parseInt((char) i + ""))
                        ).collect(Collectors.toList()))));
    }

    public FrancaisLangEntier(Token token) {
        this(Integer.parseInt(token.value()));
    }

    @Override
    public Integer getValue() {
        return super.getValue().intValue();
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.NOMBRE_ENTIER;
    }
}
