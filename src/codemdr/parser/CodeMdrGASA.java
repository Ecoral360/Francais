package codemdr.parser;


import codemdr.ast.CodeMdrStatement;
import codemdr.ast.expressions.*;
import codemdr.ast.statements.AffecterStmt;
import codemdr.ast.statements.DeclarerStmt;
import codemdr.ast.statements.ExecuterTantQueStmt;
import codemdr.ast.statements.PrintStmt;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.lexer.CodeMdrJetoniseur;
import codemdr.objects.CodeMdrBool;
import codemdr.objects.CodeMdrFloat;
import codemdr.objects.CodeMdrInt;
import codemdr.objects.CodeMdrString;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.generators.ast.AstGenerator;
import org.ascore.tokens.Token;

import java.util.ArrayList;
import java.util.List;
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
        addStatement("FONCTION_DEF", p -> {
            return null;
        });

        addStatement("FONCTION_END", p -> {
            return null;
        });

        addStatement("EXECUTER expression ENONCES TANT_QUE expression", p -> {
            var nbEnoncesExpr = (Expression<?>) p.get(1);
            var conditionExpr = (Expression<?>) p.get(4);
            return new ExecuterTantQueStmt(nbEnoncesExpr, conditionExpr, executorInstance);
        });

        addStatement("DECLARER VARIABLE VAUT L_APPEL_A expression AVEC PARAM expression~" +
                        "DECLARER VARIABLE VAUT L_APPEL_A expression AVEC PARAMS expression~" +
                        "DECLARER VARIABLE VAUT L_APPEL_A expression", (p, variant) -> {

                    EnumerationExpr args = switch (variant) {
                        case 0, 1 -> {
                            var params = (Expression<?>) p.get(7);

                            // s'assure qu'il n'y a qu'un seul paramètre: erreur d'accord sinon
                            // s'assure qu'il y a au moins deux paramètres et que l'énumération est complète (fini par un `et`)
                            if ((variant == 0 && params instanceof EnumerationExpr) ||
                                    (variant == 1 && (!(params instanceof EnumerationExpr)))) {
                                throw new ASCErrors.ErreurSyntaxe("Mauvais accord du mot `paramètre`");
                            }
                            yield EnumerationExpr.getOrWrap(params);
                        }
                        case 2 -> EnumerationExpr.completeEnumeration();
                        default -> throw new UnsupportedOperationException("Unreachable");
                    };
                    var appel = new AppelerFoncExpr((VarExpr) p.get(4), args);

                    return new DeclarerStmt(
                            new VarExpr(((Token) p.get(1)).value(), executorInstance.getExecutorState()),
                            appel, executorInstance
                    );
                }
        );

        addStatement("DECLARER VARIABLE VAUT expression", (p, variant) -> new DeclarerStmt(
                new VarExpr(((Token) p.get(1)).value(), executorInstance.getExecutorState()),
                (Expression<?>) p.get(3), executorInstance
        ));

        addStatement("MAINTENANT VARIABLE VAUT expression", p ->
                new AffecterStmt(
                        new VarExpr(((Token) p.get(1)).value(), executorInstance.getExecutorState()),
                        (Expression<?>) p.get(3), executorInstance)
        );

        addStatement("IMPRIMER expression", p -> new PrintStmt((Expression<?>) p.get(1), executorInstance));
        addStatement("expression", p -> CodeMdrStatement.evalExpression(executorInstance, (Expression<?>) p.get(0)));
        addStatement("", p -> CodeMdrStatement.statementVide(executorInstance));
    }

    /**
     * Defines the rules of the expressions of the language.
     */
    protected void addExpressions() {
        addExpression("EMPHASE #expression EMPHASE", p -> {
            System.out.println(p);
            return evalOneExpr(new ArrayList<>(p.subList(1, p.size() - 1)), null);
        });

        // add your expressions here
        addExpression("{datatypes}~VARIABLE", p -> {
            var token = (Token) p.get(0);
            return switch (token.name()) {
                case "ENTIER" -> new ConstValueExpr(new CodeMdrInt(token));
                case "DECIMAL" -> new ConstValueExpr(new CodeMdrFloat(token));
                case "TEXTE" -> new ConstValueExpr(new CodeMdrString(token));
                case "BOOL" -> new ConstValueExpr(new CodeMdrBool(token));
                case "VARIABLE" -> new VarExpr(token.value(), executorInstance.getExecutorState());
                default -> throw new NoSuchElementException(token.name());
            };
        });

        addExpression("expression PLUS expression", p -> new AddExpr((Expression<?>) p.get(0), (Expression<?>) p.get(2)));
        addExpression("expression PLUS_PETIT expression", p -> new CompExpr((Expression<?>) p.get(0), (Expression<?>) p.get(2)));

        addExpression("TABLEAU_CREATION #expression ET expression~" +
                        "TABLEAU_CREATION_SINGLETON expression",
                (p, variant) -> {
                    // System.out.println(p);
                    if (variant == 1) {
                        return new CreationTableauExpr(EnumerationExpr.completeEnumeration((Expression<?>) p.get(1)));
                    }
                    var contenu = evalOneExpr(new ArrayList<>(p.subList(1, p.size() - 2)), null);
                    if (contenu instanceof EnumerationExpr enumerationExpr) {
                        enumerationExpr.addElement((Expression<?>) p.get(p.size() - 1));
                        enumerationExpr.setComplete(true);
                        return new CreationTableauExpr(enumerationExpr);
                    }
                    return new CreationTableauExpr(EnumerationExpr.completeEnumeration(contenu, (Expression<?>) p.get(p.size() - 1)));
                });

        addExpression("expression VIRGULE expression~" +
                        "expression ET expression",
                (p, variant) -> {
                    if (p.get(0) instanceof EnumerationExpr enumerationExpr) {
                        enumerationExpr.addElement((Expression<?>) p.get(2));
                        enumerationExpr.setComplete(variant == 1);
                        return enumerationExpr;
                    }

                    var enumeration = new EnumerationExpr((Expression<?>) p.get(0), (Expression<?>) p.get(2));
                    enumeration.setComplete(variant == 1);
                    return enumeration;
                });

        addExpression("APPELER expression~" +
                        "APPELER expression AVEC PARAM expression~" +
                        "APPELER expression AVEC PARAMS expression~",
                (p, variant) ->
                        switch (variant) {
                            case 0 -> new AppelerFoncExpr((VarExpr) p.get(1));
                            case 1, 2 -> {
                                var params = (Expression<?>) p.get(4);
                                if ((variant == 1 && params instanceof EnumerationExpr) ||
                                        (variant == 2 && (!(params instanceof EnumerationExpr)))) {
                                    throw new ASCErrors.ErreurSyntaxe("Mauvais accord du mot `paramètre`");
                                }

                                yield new AppelerFoncExpr(
                                        (VarExpr) p.get(1),
                                        EnumerationExpr.getOrWrap(params)
                                );
                            }
                            default -> throw new UnsupportedOperationException("Ne devrait pas arrivé");
                        }
        );
    }
}
