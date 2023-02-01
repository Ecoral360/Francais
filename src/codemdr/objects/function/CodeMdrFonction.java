package codemdr.objects.function;

import codemdr.execution.CodeMdrExecutorState;
import codemdr.objects.CodeMdrObj;
import org.ascore.executor.ASCExecutor;
import org.ascore.lang.objects.ASCObject;
import org.ascore.lang.objects.ASCVariable;
import org.ascore.lang.objects.ASScope;

import java.util.List;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class CodeMdrFonction extends CodeMdrCallable {
    private final String nom;
    private final List<? extends ASCVariable<?>> params;
    private final ASCExecutor<CodeMdrExecutorState> executor;
    private ASScope scope;
    private final String callingCoord;

    public CodeMdrFonction(String nom, List<? extends ASCVariable<?>> params, String callingCoord, ASCExecutor<CodeMdrExecutorState> executor) {
        this.nom = nom;
        this.params = params;
        this.executor = executor;
        this.callingCoord = callingCoord;
    }

    @Override
    public CodeMdrObj<?> appeler(List<CodeMdrObj<?>> args) {
        return new CodeMdrFonctionInstance(this).executer(args);
    }

    public ASScope getScope() {
        return scope;
    }

    public String getCallingCoord() {
        return callingCoord;
    }

    public void setScope(ASScope scope) {
        this.scope = scope;
    }

    @Override
    public boolean estProcedure() {
        return false;
    }

    @Override
    public String toString() {
        return getValue().toString().replace(".", ",");
    }

    public static class CodeMdrFonctionInstance extends CodeMdrObj<Object> {
        private final CodeMdrFonction fonctionParent;
        private final ASScope.ScopeInstance scopeInstance;

        protected CodeMdrFonctionInstance(CodeMdrFonction fonctionParent) {
            super(0);
            this.fonctionParent = fonctionParent;
            this.scopeInstance = fonctionParent.scope.makeScopeInstanceFromScopeParent();
        }

        @SuppressWarnings("unchecked")
        public CodeMdrObj<?> executer(List<CodeMdrObj<?>> args) {
            fonctionParent.executor.getExecutorState().getScopeManager().pushCurrentScopeInstance(scopeInstance);

            for (int i = 0; i < fonctionParent.params.size(); i++) {
                ASCVariable<?> param = fonctionParent.params.get(i);
                ASCObject<Object> valeur = (ASCObject<Object>) args.get(i);
                ASCVariable<Object> variable = (ASCVariable<Object>) scopeInstance.getVariable(param.getName());
                variable.setAscObject(valeur);
            }

            var ancienneCoord = fonctionParent.executor.obtenirCoordRunTime().copy();

            var result = (CodeMdrObj<?>) fonctionParent.executor.executerScope(fonctionParent.callingCoord, null, null);

            fonctionParent.executor.setCoordRunTime(ancienneCoord.toString());
            fonctionParent.executor.getExecutorState().getScopeManager().popCurrentScopeInstance();

            return result;
        }
    }
}
