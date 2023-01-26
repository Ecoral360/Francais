package codemdr.parser;


import codemdr.ast.expressions.AddExpr;
import codemdr.ast.expressions.ConstValueExpr;
import codemdr.ast.expressions.VarExpr;
import codemdr.ast.statements.AffecterStmt;
import codemdr.ast.statements.DeclarerStmt;
import codemdr.ast.statements.PrintStmt;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.lexer.CodeMdrJetoniseur;
import codemdr.objects.CodeMdrFloat;
import codemdr.objects.CodeMdrInt;
import codemdr.objects.CodeMdrString;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.executor.ASCExecutor;
import org.ascore.generators.ast.AstGenerator;
import org.ascore.tokens.Token;

import java.util.NoSuchElementException;

/**
 * The parser for the CodeMdr language.
 * <br>
 * This parser is responsible for defining the rules for parsing the CodeMdr language. The actual parsing is done by the
 * {@link AstGenerator} class in accordance with the rules defined in this class.
 * <ul>
 * <li>Edit the {@link #addExpressions()} method to add new expressions to the language.</li>
 * <li>Edit the {@link #addStatements()} method to add new statements to the language.</li>
 * </ul>
 */
public class CodeMdrGASA extends AstGenerator<CodeMdrAstFrameKind> {
    private ASCExecutor<CodeMdrExecutorState> executorInstance;

    /**
     * Constructor for the parser.
     *
     * @param executorInstance the executor instance to use for executing the AST
     */
    public CodeMdrGASA(ASCExecutor<CodeMdrExecutorState> executorInstance) {
        setPatternProccessor(executorInstance.<CodeMdrJetoniseur>getLexer()::remplaceCategoriesByMembers);
        defineAstFrame(CodeMdrAstFrameKind.DEFAULT);
        addStatements();
        addExpressions();
        pushAstFrame(CodeMdrAstFrameKind.DEFAULT);
        this.executorInstance = executorInstance;
    }

    public ASCExecutor<CodeMdrExecutorState> getExecutorInstance() {
        return executorInstance;
    }

    /**
     * Sets the executor instance to use for executing the AST. If it was previously set, an exception will be thrown.
     *
     * @param executorInstance the executor instance to use for executing the AST
     * @throws IllegalStateException if the executor instance was already set
     */
    public void setExecutorInstance(ASCExecutor<CodeMdrExecutorState> executorInstance) {
        if (this.executorInstance != null) {
            throw new IllegalStateException("executorInstance was already assigned");
        }
        this.executorInstance = executorInstance;
    }

    /**
     * Defines the rules of the statements of the language.
     */
    protected void addStatements() {
        // add your statements here

        addStatement("DECLARER VARIABLE AFFECTER expression", p ->
                new DeclarerStmt(
                        new VarExpr(((Token) p.get(1)).value(), executorInstance.getExecutorState()),
                        (Expression<?>) p.get(3), executorInstance)
        );

        addStatement("MAINTENANT VARIABLE AFFECTER expression", p ->
                new AffecterStmt(
                        new VarExpr(((Token) p.get(1)).value(), executorInstance.getExecutorState()),
                        (Expression<?>) p.get(3), executorInstance)
        );

        addStatement("IMPRIMER expression", p -> new PrintStmt((Expression<?>) p.get(1)));
        addStatement("", p -> Statement.EMPTY_STATEMENT);
    }

    /**
     * Defines the rules of the expressions of the language.
     */
    protected void addExpressions() {
        // add your expressions here
        addExpression("{datatypes}~VARIABLE", p -> {
            var token = (Token) p.get(0);
            return switch (token.name()) {
                case "ENTIER" -> new ConstValueExpr(new CodeMdrInt(token));
                case "DECIMAL" -> new ConstValueExpr(new CodeMdrFloat(token));
                case "TEXTE" -> new ConstValueExpr(new CodeMdrString(token));
                case "VARIABLE" -> new VarExpr(token.value(), executorInstance.getExecutorState());
                default -> throw new NoSuchElementException(token.name());
            };
        });

        addExpression("expression PLUS expression", p -> new AddExpr((Expression<?>) p.get(0), (Expression<?>) p.get(2)));

    }
}
