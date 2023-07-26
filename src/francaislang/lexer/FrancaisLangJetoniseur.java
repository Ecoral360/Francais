package francaislang.lexer;

import org.ascore.lang.ASCLexer;
import org.ascore.tokens.Token;

import java.util.List;
import java.util.regex.Pattern;

/**
 * This class is used to lex the source code. Override and edit the {@link #lex(String)} method to change the
 * lexing behavior.
 */
public class FrancaisLangJetoniseur extends ASCLexer {
    public FrancaisLangJetoniseur(String filePath) {
        super(FrancaisLangJetoniseur.class.getResourceAsStream(filePath));
    }

    @Override
    protected void sortTokenRules() {
        for (var rule : getTokenRules()) {
            rule.setFlags(Pattern.MULTILINE | Pattern.UNICODE_CHARACTER_CLASS);
        }
        for (var rule : getIgnoredTokenRules()) {
            rule.setFlags(Pattern.MULTILINE | Pattern.UNICODE_CHARACTER_CLASS);
        }
    }

    @Override
    protected void handleLexingError(int idx, String s, List<Token> tokenList) {
        super.handleLexingError(idx, s, tokenList);
    }
}
