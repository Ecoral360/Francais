package codemdr.transpiler;

import codemdr.ast.expressions.AppelerFoncExpr;
import codemdr.ast.expressions.ConstValueExpr;
import codemdr.ast.expressions.CreationTableauExpr;
import codemdr.ast.expressions.VarExpr;
import codemdr.ast.statements.*;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.objects.CodeMdrNumber;
import codemdr.objects.CodeMdrString;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.Coordinate;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class Transpiler {
    private final ASCExecutor<CodeMdrExecutorState> executor;
    private TranspileCtx ctx;

    public Transpiler(ASCExecutor<CodeMdrExecutorState> executor) {
        this.executor = executor;
        executor.setupCompiler();
    }

    public String transpile(String lignes) {
        StringBuilder transpiledCode = new StringBuilder();
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

                transpiledCode.append(transpileStmt(ligneParsed)).append("\n");

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

        return transpiledCode.toString();
    }

    private String transpileStmt(Statement stmt) {
        if (stmt instanceof ImprimerStmt imprimerStmt)
            return transpileImprimer(imprimerStmt);

        if (stmt instanceof DeclarerStmt declarerStmt)
            return transpileDeclaration(declarerStmt);

        if (stmt instanceof AffecterStmt affecterStmt)
            return transpileAffectation(affecterStmt);

        if (stmt instanceof CreerFonctionStmt creerFonctionStmt)
            return transpileFunction(creerFonctionStmt);

        if (stmt instanceof FinFonctionStmt finFonctionStmt)
            return transpileEndFunction(finFonctionStmt);

        if (stmt instanceof RetournerStmt retournerStmt)
            return transpileReturnFunction(retournerStmt);

        return null;
    }


    private String transpileImprimer(ImprimerStmt stmt) {
        return "(begin (display %s) (display \"\\n\"))".formatted(transpileExpr(stmt.getExpression()));
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
        if (expr instanceof ConstValueExpr constValueExpr)
            return transpileConst(constValueExpr);
        if (expr instanceof CreationTableauExpr tableauExpr)
            return transpileTableau(tableauExpr);
        if (expr instanceof VarExpr varExpr)
            return transpileVar(varExpr);
        if (expr instanceof AppelerFoncExpr appelerFoncExpr)
            return transpileCall(appelerFoncExpr);

        return null;
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
        var elements = expr.valeurs().getElements();
        return "(cons %s '()%s".formatted(elements
                        .stream()
                        .map(this::transpileExpr)
                        .collect(Collectors.joining(" (cons ")),
                ")".repeat(elements.size()));
    }

    private String transpileVar(VarExpr expr) {
        return expr.nom();
    }

    private String transpileCall(AppelerFoncExpr expr) {
        return "(%s %s)".formatted(
                transpileExpr(expr.fonctionExpr()),
                expr.args()
                        .getElements()
                        .stream()
                        .map(this::transpileExpr)
                        .collect(Collectors.joining(" ")));
    }
}


class TranspileCtx {
    private final Hashtable<String, Hashtable<String, Statement>> compileDict;
    private final ArrayList<Coordinate> coordStack;
    private final Stack<ScopeParens> scopeParens = new Stack<>();

    TranspileCtx(ASCExecutor<CodeMdrExecutorState> executor) {
        this.compileDict = executor.obtenirCoordCompileDict();
        this.coordStack = executor.getCoordCompileTime();
        coordStack.add(new Coordinate("<0>main"));
        this.compileDict.put("main", new Hashtable<>());
    }

    Coordinate currentCoord() {
        return coordStack.get(coordStack.size() - 1);
    }

    String currentScope() {
        return currentCoord().getScope();
    }

    ScopeParens pushScopeParens() {
        return scopeParens.push(new ScopeParens());
    }

    ScopeParens currScopeParens() {
        return scopeParens.peek();
    }

    int popScopeParens() {
        return scopeParens.pop().totalSum();
    }

    void setNextCoord(Coordinate coord) {
        var nextCoord = coord.toString();
        if (nextCoord.length() == 0) {
            throw new ASCErrors.ErreurFermeture(currentScope());
        }
        currentCoord().setCoord(nextCoord);
        currentCoord().plusUn();
    }

    void addStatementToScope(Statement stmt) {
        compileDict.get(currentScope()).put(currentCoord().toString(), stmt);
    }

    boolean inMain() {
        return currentCoord().getScope().equals("main");
    }

    static class ScopeParens {
        private final Stack<AtomicInteger> openParenStack = new Stack<>();

        void pushBloc(int initialOpenedParens) {
            openParenStack.push(new AtomicInteger(initialOpenedParens));
        }

        void openParens(int openedParens) {
            openParenStack.peek().addAndGet(openedParens);
        }

        void closeParens(int closedParens) {
            openParenStack.peek().addAndGet(-closedParens);
        }

        int popBloc() {
            return openParenStack.pop().get();
        }

        int totalSum() {
            return openParenStack.stream().mapToInt(AtomicInteger::get).sum();
        }
    }
}