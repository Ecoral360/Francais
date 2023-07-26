package francaislang.transpiler;

import francaislang.execution.FrancaisLangExecutorState;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.Coordinate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class TranspileCtx {
    private final Hashtable<String, Hashtable<String, Statement>> compileDict;
    private final ArrayList<Coordinate> coordStack;
    private final Stack<ScopeParens> scopeParens = new Stack<>();
    private final TranspilerObjManager objManager = new TranspilerObjManager(this);
    private final StringBuilder transpiledCode = new StringBuilder();

    TranspileCtx(ASCExecutor<FrancaisLangExecutorState> executor) {
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

    void appendCodeToStart(CharSequence code) {
        transpiledCode.insert(0, code);
    }

    void appendCode(CharSequence... code) {
        for (var c : code) transpiledCode.append(c);
    }

    public TranspilerObjManager getObjManager() {
        return objManager;
    }

    String finalCompiledCode() {
        return transpiledCode.toString();
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
