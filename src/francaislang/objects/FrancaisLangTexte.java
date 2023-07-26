package francaislang.objects;

import francaislang.objects.function.FrancaisLangFonctionModule;
import francaislang.objects.function.FrancaisLangParam;
import org.ascore.tokens.Token;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangTexte extends FrancaisLangObj<String> {
    public FrancaisLangTexte(String value) {
        super(value, Map.ofEntries(
                Map.entry("Taille", new FrancaisLangEntier(value.length())),

                Map.entry("Minuscule", new FrancaisLangFonctionModule("Minuscule", List.of(),
                        args -> new FrancaisLangTexte(value.toLowerCase()))),

                Map.entry("Majuscule", new FrancaisLangFonctionModule("Majuscule", List.of(),
                        args -> new FrancaisLangTexte(value.toUpperCase()))),

                Map.entry("EstAlphaNumérique", new FrancaisLangFonctionModule("EstAlphaNumérique", List.of(),
                        args -> new FrancaisLangBool(value.matches("[A-Za-z0-9]")))),

                Map.entry("Remplacer", new FrancaisLangFonctionModule("Remplacer", List.of(
                        new FrancaisLangParam("Ancien"), new FrancaisLangParam("Nouveau")
                ), args -> new FrancaisLangTexte(value.replace((String) args.get(0).getValue(), (String) args.get(1).getValue()))))
        ));
        setPropriete("TableauDeBits", new FrancaisLangFonctionModule("TableauDeBits", List.of(),
                args -> new FrancaisLangTableau(getValue().chars().mapToObj(FrancaisLangEntier::new).collect(Collectors.toList()))));
    }

    public FrancaisLangTexte(Token token) {
        this(token.value().substring(2, token.value().length() - 2)); // removing the enclosing `"` from the string
    }

    @Override
    public FrancaisLangType getType() {
        return FrancaisLangTypePrimitif.TEXTE;
    }
}
