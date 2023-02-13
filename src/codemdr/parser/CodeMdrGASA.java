package codemdr.parser;


import codemdr.ast.CodeMdrStatement;
import codemdr.ast.expressions.*;
import codemdr.ast.statements.*;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.lexer.CodeMdrJetoniseur;
import codemdr.objects.CodeMdrFloat;
import codemdr.objects.CodeMdrInt;
import codemdr.objects.CodeMdrString;
import codemdr.objects.CodeMdrType;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.generators.ast.AstGenerator;
import org.ascore.tokens.Token;

import java.util.ArrayList;

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
        // Importer des modules
        addStatement("IMPORTER VARIABLE BIB_STD SOUS_LE_NOM VARIABLE~" +
                        "IMPORTER VARIABLE BIB_STD MEME_NOM~" +
                        "IMPORTER VARIABLE BIB_STD TEL_QUEL~",
                (p, variant) -> {
                    if (variant == 2) return new ImporterStmt(executorInstance, ((Token) p.get(1)).value());
                    else
                        return new ImporterStmt(
                                executorInstance, ((Token) p.get(1)).value(),
                                variant == 0 ? ((Token) p.get(4)).value() : null
                        );
                });

        // Définition de fonctions
        addStatement("FONCTION_DEF VARIABLE PARAM expression~" +
                        "FONCTION_DEF VARIABLE PARAMS expression ET expression~",
                (p, variant) -> {
                    var args = EnumerationExpr.getOrWrap((Expression<?>) p.get(3));

                    if (variant == 1) {
                        args.addElement((Expression<?>) p.get(5));
                    }
                    var f = new CreerFonctionStmt(((Token) p.get(1)).value(), args.cast(), executorInstance);
                    // f.execute();
                    return f;
                });

        addStatement("FONCTION_END", p -> new FinFonctionStmt(executorInstance));

        addStatement("RETOURNER LA_VALEUR expression~" +
                        "RETOURNER expression",
                (p, variant) -> {
                    if (variant == 1 && !(p.get(1) instanceof AppelerFoncExpr)) {
                        throw new ASCErrors.ErreurSyntaxe("Tu dois dire `Retourner la valeur`. Je suis très déçu de toi.");
                    }
                    return new RetournerStmt((Expression<?>) p.get(variant == 0 ? 2 : 1), executorInstance);
                });

        addStatement("EXECUTER expression ENONCES TANT_QUE expression~" +
                        "EXECUTER expression ENONCES TANT_QUE expression PUIS SAUTER expression ENONCES",
                (p, variant) -> {
                    var enonces = (Token) p.get(2);
                    var nbEnoncesExpr = (Expression<?>) p.get(1);
                    var conditionExpr = (Expression<?>) p.get(4);

                    var nbEnoncesSauteExpr = variant == 1 ? (Expression<?>) p.get(7) : nbEnoncesExpr;

                    // Si on connait le nombre d'énoncés au "compile time", on regarde si l'accord du mot est correct.
                    if (nbEnoncesExpr instanceof ConstValueExpr constValueExpr) {
                        var nbEnonces = ((CodeMdrInt) constValueExpr.value()).getValue().intValue();
                        if ((nbEnonces > 1 && !enonces.value().endsWith("s"))
                                || (nbEnonces < 1 && enonces.value().endsWith("s"))) {
                            throw new ASCErrors.ErreurSyntaxe("Tu dois donc accorder le mot \"énoncé\" selon combien il y en a! Je suis très déçu de toi.");
                        }
                    }

                    // Si on connait le nombre d'énoncés sautés au "compiler time", on regarde si l'accord du mot est correct.
                    if (variant == 1 && nbEnoncesSauteExpr instanceof ConstValueExpr constValueExpr) {
                        var nbEnonces = ((CodeMdrInt) constValueExpr.value()).getValue().intValue();
                        if ((nbEnonces > 1 && !enonces.value().endsWith("s"))
                                || (nbEnonces < 1 && enonces.value().endsWith("s"))) {
                            throw new ASCErrors.ErreurSyntaxe("Tu dois donc accorder le mot \"énoncé\" selon combien il y en a! Je suis très déçu de toi.");
                        }
                    }

                    return new ExecuterTantQueStmt(nbEnoncesExpr, conditionExpr, nbEnoncesSauteExpr, executorInstance);
                });

        // Les si
        addStatement("EXECUTER expression ENONCES SI expression~" +
                        "EXECUTER expression ENONCES SI expression PUIS SAUTER expression ENONCES~" +
                        "EXECUTER expression ENONCES SI expression POINT_VIRGULE SINON EXECUTER expression ENONCES~" +
                        "EXECUTER expression ENONCES SI expression PUIS SAUTER expression ENONCES POINT_VIRGULE SINON EXECUTER expression ENONCES~" +
                        "EXECUTER expression ENONCES SI expression POINT_VIRGULE SINON SAUTER expression ENONCES PUIS EXECUTER expression ENONCES~" +
                        "EXECUTER expression ENONCES SI expression PUIS SAUTER expression ENONCES POINT_VIRGULE SINON SAUTER expression ENONCES PUIS EXECUTER expression ENONCES",
                (p, variant) -> {
                    var nbEnoncesSiExpr = (Expression<?>) p.get(1);
                    var conditionExpr = (Expression<?>) p.get(4);

                    var nbEnoncesSauteApresSiExpr = variant == 1 || variant == 3 || variant == 5 ? (Expression<?>) p.get(7) : null;

                    Expression<?> nbEnoncesSinonExpr = switch (variant) {
                        case 2 -> (Expression<?>) p.get(8);
                        case 3, 4 -> (Expression<?>) p.get(12);
                        case 5 -> (Expression<?>) p.get(16);
                        default -> null;
                    };

                    var nbEnoncesSauteAvantSinonExpr = switch (variant) {
                        case 4 -> (Expression<?>) p.get(8);
                        case 5 -> (Expression<?>) p.get(12);
                        default -> null;
                    };

                    return new ExecuterSiStmt(nbEnoncesSiExpr, nbEnoncesSauteApresSiExpr, nbEnoncesSauteAvantSinonExpr, nbEnoncesSinonExpr, conditionExpr, executorInstance);
                });

        addStatement("DECLARER VARIABLE VAUT L_APPEL_A expression AVEC ARG expression~" +
                        "DECLARER VARIABLE VAUT L_APPEL_A expression AVEC ARGS expression~" +
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
                    var appel = new AppelerFoncExpr((Expression<?>) p.get(4), args);

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

        addStatement("MAINTENANT expression VAUT expression",
                p -> new AffecterStmt((Expression<?>) p.get(1), (Expression<?>) p.get(3), executorInstance));

        addStatement("IMPRIMER expression", p -> new ImprimerStmt((Expression<?>) p.get(1), executorInstance));
        addStatement("expression", p -> CodeMdrStatement.evalExpression(executorInstance, (Expression<?>) p.get(0)));
        addStatement("", p -> CodeMdrStatement.statementVide(executorInstance));
    }

    /**
     * Defines the rules of the expressions of the language.
     */
    protected void addExpressions() {
        addExpression("EMPHASE #expression EMPHASE", p -> {
            return evalOneExpr(new ArrayList<>(p.subList(1, p.size() - 1)), null);
        });

        // add your expressions here
        addExpression("{datatypes}~VARIABLE~{datatypes_name}", p -> {
            var token = (Token) p.get(0);
            return switch (token.name()) {
                case "ENTIER" -> new ConstValueExpr(new CodeMdrInt(token));
                case "DECIMAL" -> new ConstValueExpr(new CodeMdrFloat(token));
                case "TEXTE" -> new ConstValueExpr(new CodeMdrString(token));
                case "VARIABLE" -> new VarExpr(token.value(), executorInstance.getExecutorState());
                default -> new ConstValueExpr(new CodeMdrType(token));
            };
        });

        addExpression("expression {op} expression",
                p -> new OpExpr((Expression<?>) p.get(0), (Expression<?>) p.get(2), ((Token) p.get(1)).value()));

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

        addExpression("expression DE expression",
                p -> new GetProprieteExpr((Expression<?>) p.get(2), (VarExpr) p.get(0))
        );

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

        addExpression("L_APPEL_A expression AVEC ARG expression~" +
                        "L_APPEL_A expression AVEC ARGS expression~" +
                        "L_APPEL_A expression",
                (p, variant) ->
                        switch (variant) {
                            case 2 -> new AppelerFoncExpr((Expression<?>) p.get(1));
                            case 0, 1 -> {
                                var params = (Expression<?>) p.get(4);
                                if ((variant == 0 && params instanceof EnumerationExpr) ||
                                        (variant == 1 && (!(params instanceof EnumerationExpr)))) {
                                    throw new ASCErrors.ErreurSyntaxe("Mauvais accord du mot `argument`. Je suis très déçu de toi.");
                                }

                                yield new AppelerFoncExpr(
                                        (Expression<?>) p.get(1),
                                        EnumerationExpr.getOrWrap(params)
                                );
                            }
                            default -> throw new UnsupportedOperationException("Ne devrait pas arrivé");
                        }
        );

        addExpression("CAR_DE expression A_LA_POS expression~" +
                        "CAR_DE expression A_INDEX expression",
                (p, variant) -> new IndexTexteExpr(
                        (Expression<?>) p.get(1), (Expression<?>) p.get(3), variant == 0 ? 1 : 0)
        );


        addExpression("ELEMENT_DE expression A_LA_POS expression~" +
                        "ELEMENT_DE expression A_INDEX expression",
                (p, variant) -> new IndexListeExpr(
                        (Expression<?>) p.get(1), (Expression<?>) p.get(3), variant == 0 ? 1 : 0)
        );

        addExpression("expression {comp} expression",
                p -> new CompExpr((Expression<?>) p.get(0), (Expression<?>) p.get(2), ((Token) p.get(1)).value())
        );
    }
}
