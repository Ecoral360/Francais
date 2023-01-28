package codemdr.execution;

import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCPrecompiler;
import org.ascore.tokens.Token;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to precompile the source code. Edit the {@link #preCompile(String)} method to change the precompilation behavior.
 * <br>
 * PreCompilation is <b>guarantied</b> to be executed before the compilation of the source code.
 */
public class CodeMdrPreCompiler extends ASCPrecompiler {

    /**
     * This method is used to precompile the source code.
     *
     * @param program The source code to precompile as an array of strings.
     * @return The precompiled source code as an array of strings. (aka the actual code that will be compiled)
     */
    @Override
    public String preCompile(String program) {
        return Arrays.stream(program.split("\n")).map(String::trim).collect(Collectors.joining("\n"));
    }

    @Override
    public List<Token> preCompileBeforeStatementSplit(List<Token> tokens) {
        return tokens;
    }

    @Override
    public List<List<Token>> preCompileAfterStatementSplit(List<List<Token>> tokens) {
        for (var statement : tokens) {
            if (statement.isEmpty()) continue;
            var firstToken = statement.get(0);
            if (firstToken.value().charAt(0) != firstToken.value().toUpperCase().charAt(0)) {
                throw new ASCErrors.LexingError("Tous les énoncés doivent commencer par une lettre majuscule. Je suis très déçu de toi.");
            }

            statement.set(0, new Token(firstToken.name(),
                    firstToken.value().toLowerCase().charAt(0) + firstToken.value().substring(1),
                    firstToken.category(),
                    firstToken.start(),
                    firstToken.tokenRuleParent(),
                    firstToken.match())
            );
        }
        return tokens;
    }
}
