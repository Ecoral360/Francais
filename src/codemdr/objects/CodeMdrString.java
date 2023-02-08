package codemdr.objects;

import codemdr.objects.function.CodeMdrFonctionModule;
import codemdr.objects.function.CodeMdrParam;
import org.ascore.tokens.Token;

import java.util.List;
import java.util.Map;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrString extends CodeMdrObj<String> {
    public CodeMdrString(String value) {
        super(value, Map.ofEntries(
                Map.entry("Taille", new CodeMdrInt(value.length())),

                Map.entry("Minuscule", new CodeMdrFonctionModule("Minuscule", List.of(),
                        args -> new CodeMdrString(value.toLowerCase()))),

                Map.entry("Majuscule", new CodeMdrFonctionModule("Majuscule", List.of(),
                        args -> new CodeMdrString(value.toUpperCase()))),

                Map.entry("EstAlphaNumérique", new CodeMdrFonctionModule("EstAlphaNumérique", List.of(),
                        args -> new CodeMdrBool(value.matches("[A-Za-z0-9]")))),

                Map.entry("Remplacer", new CodeMdrFonctionModule("Remplacer", List.of(
                        new CodeMdrParam("Ancien"), new CodeMdrParam("Nouveau")
                ), args -> new CodeMdrString(value.replace((String) args.get(0).getValue(), (String) args.get(1).getValue()))))
        ));
    }

    public CodeMdrString(Token token) {
        this(token.value().substring(2, token.value().length() - 2)); // removing the enclosing `"` from the string
    }
}
