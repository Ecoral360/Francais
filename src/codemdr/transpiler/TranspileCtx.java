package codemdr.transpiler;

import codemdr.ast.expressions.AppelerFoncExpr;
import codemdr.ast.expressions.GetProprieteExpr;
import codemdr.ast.expressions.IndexListeExpr;
import codemdr.ast.expressions.VarExpr;
import codemdr.execution.CodeMdrExecutorState;
import codemdr.objects.CodeMdrInt;
import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrString;
import codemdr.objects.CodeMdrTableau;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.ast.buildingBlocs.Statement;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.executor.Coordinate;
import org.ascore.utils.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class TranspileCtx {
    private final Hashtable<String, Hashtable<String, Statement>> compileDict;
    private final ArrayList<Coordinate> coordStack;
    private final Stack<ScopeParens> scopeParens = new Stack<>();
    private final TranspilerObjManager objManager = new TranspilerObjManager();
    private final StringBuilder transpiledCode = new StringBuilder();

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

    class TranspilerObjManager {
        private static final HashMap<String, String> ALL_PROPERTIES = new HashMap<>();
        private static final Set<String> seenProperties = new HashSet<>();

        static {
            ALL_PROPERTIES.putAll(propertiesOf(CodeMdrString.class,
                    Map.entry("Taille", "@p (string-length s)")
            ));

            ALL_PROPERTIES.putAll(propertiesOf(CodeMdrTableau.class,
                    Map.entry("Taille", "@p (length s)"),
                    Map.entry("Ajouter", "@! (append s (cons p '()))")
            ));
        }

        @SuppressWarnings("unchecked")
        @SafeVarargs
        private static Map<String, String> propertiesOf(Class<? extends CodeMdrObj<?>> mdrClass, Map.Entry<String, String>... properties) {
            return Map.ofEntries(Arrays.stream(properties)
                    .map(prop -> Map.entry("%s-%s".formatted(mdrClass.getSimpleName(), prop.getKey()), prop.getValue()))
                    .toArray(Map.Entry[]::new));
        }

        String registerProperty(String propertyName, Expression<?> obj) {
            try {
                return registerProperty(propertyName, (CodeMdrObj<?>) obj.eval());
            } catch (Exception err) {
                throw new UnsupportedOperationException("Unsupported for now");
            }
        }

        /**
         * Si on connaît le type de l'objet, on crée une fonction spécialisée nommée selon le format:<br> <code>CodeMdrType-NomPropriété</code>.
         * <br>
         * Cette fonction prend comme premier paramètre l'objet. Si la propriété est une <code>méthode</code>,
         * alors la fonction prend aussi une liste d'argument comme deuxième paramètre.
         */
        String registerProperty(String propertyName, CodeMdrObj<?> obj) {
            var formattedName = formatPropertyName(propertyName, obj.getClass());

            if (seenProperties.add(formattedName)) {
                var body = ALL_PROPERTIES.get(formattedName);
                var argPrefix = body.substring(0, 2);

                body = body.substring(3);

                String args = switch (argPrefix) {
                    case "@p" -> "s";
                    case "@m", "@!" -> "s p";
                    default -> throw new IllegalArgumentException("Unknown property prefix: \"" + argPrefix + "\".");
                };

                appendCodeToStart("(define (%s %s) %s)\n".formatted(formattedName, args, body));
            }
            return formattedName;
        }

        /**
         * @param formattedProperty
         * @return <code>@p</code> if property, <code>@m</code> if method, <code>@!</code> if method that changes the caller
         */
        String getPropertyType(String formattedProperty) {
            return ALL_PROPERTIES.get(formattedProperty).substring(0, 2);
        }

        String generateMethodCall(String formattedProperty, GetProprieteExpr expr) {
            return "(%s ".formatted(ALL_PROPERTIES.get(formattedProperty).substring(3));
        }

        @SuppressWarnings("rawtypes")
        public static String formatPropertyName(String propertyName, Class<? extends CodeMdrObj> mdrClass) {
            return "%s-%s".formatted(mdrClass.getSimpleName(), propertyName);
        }
    }
}
