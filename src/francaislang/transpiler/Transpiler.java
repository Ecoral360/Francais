package francaislang.transpiler;

import francaislang.ast.expressions.*;
import francaislang.ast.statements.*;
import francaislang.execution.FrancaisLangExecutorState;
import francaislang.execution.FrancaisLangPreCompiler;
import francaislang.lexer.FrancaisLangJetoniseur;
import francaislang.objects.FrancaisLangEntier;
import francaislang.objects.FrancaisLangNombre;
import francaislang.objects.FrancaisLangObj;
import francaislang.objects.FrancaisLangTexte;
import francaislang.parser.FrancaisLangGASA;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.ASCExecutorBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class Transpiler {
    private final ASCExecutor<FrancaisLangExecutorState> executor;
    private TranspileCtx ctx;
    private int uniqueId = 0;

    private Stack<Map.Entry<Integer, Supplier<String>>> codeBlocStack = new Stack<>();

    public static Transpiler defaultTranspiler() {
        var executor = new ASCExecutorBuilder<FrancaisLangExecutorState>() // create an executor builder
                .withLexer(new FrancaisLangJetoniseur("/francaislang/grammar_rules/Grammar.yaml")) // add the lexer to the builder
                .withParser(FrancaisLangGASA::new) // add the parser to the builder
                .withExecutorState(new FrancaisLangExecutorState()) // add the executor state to the builder
                .withPrecompiler(new FrancaisLangPreCompiler()) // add the precompiler to the builder
                .build(); // build the executor

        return new Transpiler(executor);
    }

    public Transpiler(ASCExecutor<FrancaisLangExecutorState> executor) {
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

                while (!codeBlocStack.isEmpty()) {
                    var top = codeBlocStack.pop();
                    if (top.getKey() > 0) {
                        codeBlocStack.push(Map.entry(top.getKey() - 1, top.getValue()));
                        break;
                    }
                    ctx.appendCode(
                            top.getValue().get(),
                            ")".repeat(ctx.popScopeParens()) + "\n"
                    );
                }

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

    public static String getCodeDepart() {
        var file = new File("./src/francais-lang.scm");
        var starterCode = new StringBuilder();
        try (var reader = new Scanner(file)) {
            while (reader.hasNextLine()) starterCode.append(reader.nextLine()).append('\n');
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot add startup code to output.");
        }

        return starterCode.append("\n\n").toString();
    }

    private int nextId() {
        return uniqueId++;
    }

    private String transpileStmt(Statement stmt) {
        return Stmt.call(this, stmt);
    }


    private String transpileImprimer(ImprimerStmt stmt) {
        return "(begin (display (FrancaisLangObj->string %s)) (##putchar %d))".formatted(transpileExpr(stmt.getExpression()), (int) '\n');
    }

    private String transpileInclure(InclureStmt stmt) {
        if (stmt.getNomModule() != null) {
            throw new UnsupportedOperationException("Les modules ne sont pas encore supportés.");
        }
        var lignes = new StringBuilder();
        var fichier = new File(stmt.getCheminFichier());
        try (var reader = new Scanner(fichier)) {
            while (reader.hasNextLine()) lignes.append(reader.nextLine()).append('\n');
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        var transpiler = Transpiler.defaultTranspiler();
        return transpiler.transpile(lignes.toString());
    }

    private String transpileSi(ExecuterSiStmt stmt) {
        ctx.pushScopeParens().openParens(2);

        codeBlocStack.push(Map.entry(
                ((FrancaisLangEntier) stmt.getNbEnoncesSiExpr().eval()).getValue(),
                () -> ""));

        codeBlocStack.push(Map.entry(
                ((FrancaisLangEntier) stmt.getNbEnoncesSinonExpr().eval()).getValue(),
                () -> ""));
        return "";
    }

    private String transpileTantQue(ExecuterTantQueStmt stmt) {
        ctx.pushScopeParens().openParens(3);
        var loopName = "tant-que-%d".formatted(nextId());
        var end = "(%s)".formatted(loopName);

        codeBlocStack.push(Map.entry(
                ((FrancaisLangEntier) stmt.getNbEnoncesExpr().eval()).getValue(),
                () -> end));

        return ("""
                (let %s ()
                  (if %s
                    (begin
                      """).formatted(loopName, transpileExpr(stmt.getConditionExpr()));
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

    private String transpileEndFunction(FinFonctionStmt ignored) {
        return ")".repeat(ctx.popScopeParens());
    }

    private String transpileExpr(Expression<?> expr) {
        return Expr.call(this, expr);
    }

    private String transpileConst(ConstValueExpr expr) {
        var value = expr.value();
        if (value instanceof FrancaisLangNombre codeMdrNum) {
            return ((Integer) codeMdrNum.getValue().intValue()).toString();
        }
        if (value instanceof FrancaisLangTexte codeMdrString) {
            return "\"%s\"".formatted(codeMdrString.getValue());
        }
        return value.toString();
    }

    private String transpileTableau(CreationTableauExpr expr) {
        return "(##rib %s %d FrancaisLangTableau-type)".formatted(transpileEnumeration(expr.valeurs()), expr.valeurs().getElements().size());
    }

    private String transpileEnumeration(EnumerationExpr expr) {
        if (expr.getElements().isEmpty()) return "'()";

        return "(##rib %s '()%s".formatted(expr.getElements()
                        .stream()
                        .map(this::transpileExpr)
                        .collect(Collectors.joining(" (##rib ")),
                " pair-type)".repeat(expr.getElements().size()));
    }

    private String transpileIndexTableau(IndexListeExpr expr) {
        return "(list-ref (##field0 %s) (##- %s %d))".formatted(
                transpileExpr(expr.listeExpr()),
                transpileExpr(expr.indexExpr()),
                expr.indexDepart()
        );
    }

    private String transpileIndexTexte(IndexTexteExpr expr) {
        return "(##rib (string (string-ref (##field0 %s) (##- %s %d))) 1 FrancaisLangTexte-type)".formatted(
                transpileExpr(expr.listeExpr()),
                transpileExpr(expr.indexExpr()),
                expr.indexDepart()
        );
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
        FrancaisLangObj<?> value;
        if (expr.objExpr() instanceof ConstValueExpr constValueExpr) {
            value = (FrancaisLangObj<?>) constValueExpr.value();
        } else if (expr.objExpr() instanceof VarExpr varExpr) {
            var x = varExpr.getCompileTimeVar();
            if (x == null || x.getAscObject() == null || x.getAscObject().isNoValue() || x.getAscObject().getValue() == FrancaisLangObj.AUCUNE_VALEUR) {
                return getPropertyDispatch(expr);
            }
            value = (FrancaisLangObj<?>) x.getAscObject();;
        } else {
            value = (FrancaisLangObj<?>) expr.objExpr().eval();
        }

        var functionName = objManager.registerProperty(expr.nomProprieteExpr().nom(), value);
        var propType = objManager.getPropertyType(expr.nomProprieteExpr().nom(), value.getClass());
        return switch (propType) {
            case "@p" -> "(%s %s)".formatted(functionName, transpileExpr(expr.objExpr()));
            case "@!" -> "(set! (%s %s))".formatted(functionName, transpileExpr(expr.objExpr()));
            default -> throw new IllegalArgumentException("Invalid propType: '" + propType + "'.");
        };
    }

    private String getPropertyDispatch(GetProprieteExpr expr) {
        var objManager = ctx.getObjManager();
        var clauses = new StringBuilder();

        var obj = transpileExpr(expr.objExpr());
        clauses.append(("""
                (let ((temp-var %s))
                    (let ((temp-type (FrancaisLang/Fonc/TypeDe temp-var)))
                        (cond""").formatted(obj));

        var properties = objManager.registerProperties(expr.nomProprieteExpr().nom(), expr);
        for (var property : properties) {
            var functionName = property.getKey();
            var propType = objManager.getPropertyType(expr.nomProprieteExpr().nom(), property.getValue());
            var clauseBody = switch (propType) {
                case "@p" -> "(%s %s)".formatted(functionName, obj);
                case "@!" -> "(set! (%s %s))".formatted(functionName, obj);
                default -> throw new IllegalArgumentException("Invalid propType: '" + propType + "'.");
            };

            var clause = "((##eqv? temp-type '%s) %s)"
                    .formatted(property.getValue().getSimpleName(), clauseBody);

            clauses.append("\n").append(clause);
        }
        clauses.append("\n(else (error \"Appel invalide.\")))))");

        return clauses.toString();
    }

    private String getPropertyCall(GetProprieteExpr expr, EnumerationExpr args) {
        var objManager = ctx.getObjManager();
        FrancaisLangObj<?> value;
        if (expr.objExpr() instanceof ConstValueExpr constValueExpr) {
            value = (FrancaisLangObj<?>) constValueExpr.value();
        } else if (expr.objExpr() instanceof VarExpr varExpr) {
            var x = varExpr.getCompileTimeVar().getAscObject();
            if (x == null || x.isNoValue() || x.getValue() == FrancaisLangObj.AUCUNE_VALEUR) {
                return getPropertyCallDispatch(expr, args);
            }
            value = (FrancaisLangObj<?>) x;
        } else {
            value = (FrancaisLangObj<?>) expr.objExpr().eval();
        }

        var functionName = objManager.registerProperty(expr.nomProprieteExpr().nom(), value);
        var propType = objManager.getPropertyType(expr.nomProprieteExpr().nom(), value.getClass());
        return switch (propType) {
            case "@m" -> "(%s %s %s)".formatted(functionName, transpileExpr(expr.objExpr()), transpileExpr(args));
            case "@!" ->
                    "(set! %2$s (%s %s %s))".formatted(functionName, transpileExpr(expr.objExpr()), transpileExpr(args));
            default -> throw new IllegalArgumentException("Invalid propType: '" + propType + "'.");
        };
    }

    private String getPropertyCallDispatch(GetProprieteExpr expr, EnumerationExpr args) {
        var objManager = ctx.getObjManager();
        var clauses = new StringBuilder();

        var obj = transpileExpr(expr.objExpr());
        clauses.append(("""
                (let ((temp-var %s))
                    (let ((temp-type (FrancaisLang/Fonc/TypeDe temp-var)))
                        (cond""").formatted(obj));

        var properties = objManager.registerProperties(expr.nomProprieteExpr().nom(), expr);
        for (var property : properties) {
            var functionName = property.getKey();
            var propType = objManager.getPropertyType(expr.nomProprieteExpr().nom(), property.getValue());
            var clauseBody = switch (propType) {
                case "@m" -> "(%s %s %s)".formatted(functionName, transpileExpr(expr.objExpr()), transpileExpr(args));
                case "@!" ->
                        "(set! %2$s (%s %s %s))".formatted(functionName, transpileExpr(expr.objExpr()), transpileExpr(args));
                default -> throw new IllegalArgumentException("Invalid propType: '" + propType + "'.");
            };

            var clause = "((##eqv? temp-type '%s) %s)"
                    .formatted(property.getValue().getSimpleName(), clauseBody);

            clauses.append("\n").append(clause);
        }
        clauses.append("\n(else (error \"Appel invalide.\")))))");

        return clauses.toString();
    }

    enum Stmt {
        Imprimer((t, s) -> t.transpileImprimer((ImprimerStmt) s), ImprimerStmt.class),
        Inclure((t, s) -> t.transpileInclure((InclureStmt) s), InclureStmt.class),
        Declarer((t, s) -> t.transpileDeclaration((DeclarerStmt) s), DeclarerStmt.class),
        Affecter((t, s) -> t.transpileAffectation((AffecterStmt) s), AffecterStmt.class),
        CreerFonc((t, s) -> t.transpileFunction((CreerFonctionStmt) s), CreerFonctionStmt.class),
        RetournerFonc((t, s) -> t.transpileReturnFunction((RetournerStmt) s), RetournerStmt.class),
        FinFonc((t, s) -> t.transpileEndFunction((FinFonctionStmt) s), FinFonctionStmt.class),
        TantQue((t, s) -> t.transpileTantQue((ExecuterTantQueStmt) s), ExecuterTantQueStmt.class),
        Si((t, s) -> t.transpileSi((ExecuterSiStmt) s), ExecuterSiStmt.class),
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
        IndexTableau((t, e) -> t.transpileIndexTableau((IndexListeExpr) e), IndexListeExpr.class),
        IndexTexte((t, e) -> t.transpileIndexTexte((IndexTexteExpr) e), IndexTexteExpr.class),
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



