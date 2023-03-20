package codemdr.transpiler;

import codemdr.ast.expressions.*;
import codemdr.ast.statements.*;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.objects.CodeMdrNumber;
import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrString;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;

import java.util.function.BiFunction;
import java.util.stream.Collectors;

public final class Transpiler {
    private final ASCExecutor<CodeMdrExecutorState> executor;
    private TranspileCtx ctx;


    public Transpiler(ASCExecutor<CodeMdrExecutorState> executor) {
        this.executor = executor;
        executor.setupCompiler();
    }

    public String transpile(String lignes) {
        executor.obtenirCoordCompileDict().clear();
        ctx = new TranspileCtx(executor);

        lignes = executor.getPreCompiler().preCompile(lignes);

        var tokens = executor.getLexer().lex(lignes);

        tokens = executor.getPreCompiler().preCompileBeforeStatementSplit(tokens);

        var codeTokenized = executor.getLexer().splitInStatements(tokens);

        codeTokenized = executor.getPreCompiler().preCompileAfterStatementSplit(codeTokenized);

        // boucle a travers toutes les lignes de l'array "lignes"
        for (int i = 0; i < codeTokenized.size(); i++) {
            var numLigne = i + 1;
            var lineToken = codeTokenized.get(i);

            // produit la liste de Token representant la ligne (voir lexer.lex)

            // obtiens la coordonne ainsi que le scope ou sera enregistree la ligne compilee

            try {
                Statement ligneParsed = executor.getParser().parse(lineToken);
                ligneParsed.setNumLine(numLigne);

                // met ligneParsed dans le dictionnaire de coordonne
                ctx.addStatementToScope(ligneParsed);

                ctx.appendCode(transpileStmt(ligneParsed), "\n");

                // accede a la fonction prochaineCoord du programme trouvee afin de definir la prochaine coordonnee
                ctx.setNextCoord(ligneParsed.getNextCoordinate(ctx.currentCoord().copy(), lineToken));

            } catch (ASCErrors.ASCError err) {
                err.afficher(executor, numLigne);
                throw err;
            }
        }

        // ajoute une ligne null à la fin pour signaler la fin de l'exécution
        Statement fin = new Statement.EndOfProgramStatement();
        fin.setNumLine(codeTokenized.size() + 1);
        ctx.addStatementToScope(fin);

        try {
            if (!ctx.inMain()) {
                throw new ASCErrors.ErreurFermeture(ctx.currentCoord().getBlocActuel());
            }
//            if (!ASFonctionManager.obtenirStructure().isBlank()) {
//                throw new ErreurFermeture(ASFonctionManager.obtenirStructure());
//            }
        } catch (ASCErrors.ASCError err) {
            err.afficher(executor, codeTokenized.size());
            throw err;
        }

        return ctx.finalCompiledCode();
    }

    private String transpileStmt(Statement stmt) {
        return Stmt.call(this, stmt);
    }


    private String transpileImprimer(ImprimerStmt stmt) {
        return "(begin (display %s) (putchar %d))".formatted(transpileExpr(stmt.getExpression()), (int) '\n');
    }

    private String transpileDeclaration(DeclarerStmt stmt) {
        if (ctx.inMain()) {
            return "(define %s %s)".formatted(stmt.getVariable().nom(), transpileExpr(stmt.getValeur()));
        }

        ctx.currScopeParens().pushBloc(1);

        return "(let ((%s %s)) ".formatted(transpileExpr(stmt.getVariable()), transpileExpr(stmt.getValeur()));
    }

    private String transpileAffectation(AffecterStmt stmt) {
        return "(set! %s %s)".formatted(transpileExpr(stmt.getVariableExpr()), transpileExpr(stmt.getValeurExpr()));
    }

    private String transpileFunction(CreerFonctionStmt stmt) {
        StringBuilder function = new StringBuilder("(define (")
                .append(stmt.getNomFonction());

        for (var param : stmt.getArgs()) {
            function.append(" ")
                    .append(param.nom());
        }
        function.append(") ");

        ctx.pushScopeParens().pushBloc(1);

        return function.toString();
    }

    private String transpileReturnFunction(RetournerStmt stmt) {
        return transpileExpr(stmt.getExpression());
    }

    private String transpileEndFunction(FinFonctionStmt stmt) {
        return ")".repeat(ctx.popScopeParens());
    }

    private String transpileExpr(Expression<?> expr) {
        return Expr.call(this, expr);
    }

    private String transpileConst(ConstValueExpr expr) {
        var value = expr.value();
        if (value instanceof CodeMdrNumber codeMdrNum) {
            return ((Integer) codeMdrNum.getValue().intValue()).toString();
        }
        if (value instanceof CodeMdrString codeMdrString) {
            return "\"%s\"".formatted(codeMdrString.getValue());
        }
        return value.toString();
    }

    private String transpileTableau(CreationTableauExpr expr) {
        return transpileEnumeration(expr.valeurs());
    }

    private String transpileEnumeration(EnumerationExpr expr) {
        return "(cons %s '()%s".formatted(expr.getElements()
                        .stream()
                        .map(this::transpileExpr)
                        .collect(Collectors.joining(" (cons ")),
                ")".repeat(expr.getElements().size()));
    }

    private String transpileVar(VarExpr expr) {
        return expr.nom();
    }

    private String transpileCall(AppelerFoncExpr expr) {
        if (expr.fonctionExpr() instanceof GetProprieteExpr getProprieteExpr) {
            // TODO
            return getPropertyCall(getProprieteExpr, expr.args());
        }

        return "(%s %s)".formatted(
                transpileExpr(expr.fonctionExpr()),
                expr.args()
                        .getElements()
                        .stream()
                        .map(this::transpileExpr)
                        .collect(Collectors.joining(" ")));
    }

    private String getProperty(GetProprieteExpr expr) {
        var objManager = ctx.getObjManager();
        CodeMdrObj<?> value;
        if (expr.objExpr() instanceof ConstValueExpr constValueExpr) {
            value = (CodeMdrObj<?>) constValueExpr.value();
        } else if (expr.objExpr() instanceof VarExpr varExpr) {
            var x = varExpr.getCompileTimeVar().getAscObject();
            if (x == null || x.isNoValue()) {
                throw new UnsupportedOperationException("Unsupported for now");
            }
            value = (CodeMdrObj<?>) x;
        } else {
            value = (CodeMdrObj<?>) expr.objExpr().eval();
        }

        var functionName = objManager.registerProperty(expr.nomProprieteExpr().nom(), value);
        var propType = objManager.getPropertyType(expr.nomProprieteExpr().nom(), value.getClass());
        return switch (propType) {
            case "@p" -> "(%s %s)".formatted(functionName, transpileExpr(expr.objExpr()));
            case "@!" -> "(set! (%s %s))".formatted(functionName, transpileExpr(expr.objExpr()));
            default -> throw new IllegalArgumentException("Invalid propType: '" + propType + "'.");
        };
    }

    private String getPropertyCall(GetProprieteExpr expr, EnumerationExpr args) {
        var objManager = ctx.getObjManager();
        CodeMdrObj<?> value;
        if (expr.objExpr() instanceof ConstValueExpr constValueExpr) {
            value = (CodeMdrObj<?>) constValueExpr.value();
        } else if (expr.objExpr() instanceof VarExpr varExpr) {
            var x = varExpr.getCompileTimeVar().getAscObject();
            if (x == null || x.isNoValue()) {
                throw new UnsupportedOperationException("Unsupported for now");
            }
            value = (CodeMdrObj<?>) x;
        } else {
            value = (CodeMdrObj<?>) expr.objExpr().eval();
        }

        var functionName = objManager.registerProperty(expr.nomProprieteExpr().nom(), value);
        var propType = objManager.getPropertyType(expr.nomProprieteExpr().nom(), value.getClass());
        return switch (propType) {
            case "@m" ->
                    "(%s %s %s)".formatted(functionName, transpileExpr(expr.objExpr()), transpileExpr(args));
            case "@!" ->
                    "(set! %2$s (%s %s %s))".formatted(functionName, transpileExpr(expr.objExpr()), transpileExpr(args));
            default -> throw new IllegalArgumentException("Invalid propType: '" + propType + "'.");
        };
    }

    enum Stmt {
        Imprimer((t, s) -> t.transpileImprimer((ImprimerStmt) s), ImprimerStmt.class),
        Declarer((t, s) -> t.transpileDeclaration((DeclarerStmt) s), DeclarerStmt.class),
        Affecter((t, s) -> t.transpileAffectation((AffecterStmt) s), AffecterStmt.class),
        CreerFonc((t, s) -> t.transpileFunction((CreerFonctionStmt) s), CreerFonctionStmt.class),
        RetournerFonc((t, s) -> t.transpileReturnFunction((RetournerStmt) s), RetournerStmt.class),
        FinFonc((t, s) -> t.transpileEndFunction((FinFonctionStmt) s), FinFonctionStmt.class),
        EvalExpr((t, s) -> t.transpileExpr(((EvalExprStmt) s).getExpression()), EvalExprStmt.class);

        private final BiFunction<Transpiler, Statement, String> transpile;
        private final Class<? extends Statement> compatibleWith;

        Stmt(BiFunction<Transpiler, Statement, String> transpile, Class<? extends Statement> compatibleWith) {
            this.transpile = transpile;
            this.compatibleWith = compatibleWith;
        }

        static String call(Transpiler transpiler, Statement statement) {
            for (var stmt : values()) {
                if (stmt.compatibleWith.isInstance(statement)) {
                    return stmt.transpile.apply(transpiler, statement);
                }
            }
            throw new UnsupportedOperationException("Unsupported statement '" + statement + "'.");
        }
    }

    enum Expr {
        Const((t, e) -> t.transpileConst((ConstValueExpr) e), ConstValueExpr.class),
        Var((t, e) -> t.transpileVar((VarExpr) e), VarExpr.class),
        Tableau((t, e) -> t.transpileTableau((CreationTableauExpr) e), CreationTableauExpr.class),
        Enumeration((t, e) -> t.transpileEnumeration((EnumerationExpr) e), EnumerationExpr.class),
        GetPropriete((t, e) -> t.getProperty((GetProprieteExpr) e), GetProprieteExpr.class),
        Appel((t, e) -> t.transpileCall((AppelerFoncExpr) e), AppelerFoncExpr.class);

        private final BiFunction<Transpiler, Expression<?>, String> transpile;
        private final Class<? extends Expression<?>> compatibleWith;

        Expr(BiFunction<Transpiler, Expression<?>, String> transpile, Class<? extends Expression<?>> compatibleWith) {
            this.transpile = transpile;
            this.compatibleWith = compatibleWith;
        }

        static String call(Transpiler transpiler, Expression<?> expression) {
            for (var expr : values()) {
                if (expr.compatibleWith.isInstance(expression)) {
                    return expr.transpile.apply(transpiler, expression);
                }
            }
            throw new UnsupportedOperationException("Unsupported expression '" + expression + "'.");
        }
    }
}



