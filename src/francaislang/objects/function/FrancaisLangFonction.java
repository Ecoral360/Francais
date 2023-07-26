package francaislang.objects.function;

import francaislang.execution.FrancaisLangExecutorState;
import francaislang.objects.FrancaisLangObj;
import francaislang.objects.FrancaisLangType;
import francaislang.objects.FrancaisLangTypePrimitif;
import org.ascore.errors.ASCErrors;
import org.ascore.executor.ASCExecutor;
import org.ascore.lang.objects.ASCObject;
import org.ascore.lang.objects.ASCVariable;
import org.ascore.lang.objects.ASScope;

import java.util.List;

/**
 * An example of an object for the CodeMdr programming main.language
 */
public class FrancaisLangFonction extends FrancaisLangCallable {
    private final String nom;
    private final List<? extends ASCVariable<?>> params;
    private final ASCExecutor<FrancaisLangExecutorState> executor;
    private ASScope scope;
    private final String callingCoord;

    public FrancaisLangFonction(String nom, List<? extends ASCVariable<?>> params, String callingCoord, ASCExecutor<FrancaisLangExecutorState> executor) {
        this.nom = nom;
        this.params = params;
        this.executor = executor;
        this.callingCoord = callingCoord;
    }

    @Override
    public FrancaisLangObj<?> appeler(List<FrancaisLangObj<?>> args) {
        return new FrancaisLangFonctionInstance(this).executer(args);
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

    public static class FrancaisLangFonctionInstance extends FrancaisLangObj<Object> {
        private final FrancaisLangFonction fonctionParent;
        private final ASScope.ScopeInstance scopeInstance;

        protected FrancaisLangFonctionInstance(FrancaisLangFonction fonctionParent) {
            super(0);
            this.fonctionParent = fonctionParent;
            this.scopeInstance = fonctionParent.scope.makeScopeInstanceFromScopeParent();
        }

        @SuppressWarnings("unchecked")
        public FrancaisLangObj<?> executer(List<FrancaisLangObj<?>> args) {
            fonctionParent.executor.getExecutorState().getScopeManager().pushCurrentScopeInstance(scopeInstance);

            for (int i = 0; i < fonctionParent.params.size(); i++) {
                ASCVariable<?> param = fonctionParent.params.get(i);
                ASCObject<Object> valeur = (ASCObject<Object>) args.get(i);
                ASCVariable<Object> variable = (ASCVariable<Object>) scopeInstance.getVariable(param.getName());
                variable.setAscObject(valeur);
            }

            var ancienneCoord = fonctionParent.executor.obtenirCoordRunTime().copy();

            var resultObj = fonctionParent.executor.executerScope(fonctionParent.callingCoord, null, null);
            if (!(resultObj instanceof FrancaisLangObj<?> result)) {
                throw new ASCErrors.ASCError((String) resultObj, "IdkError");
            }


            fonctionParent.executor.setCoordRunTime(ancienneCoord.toString());
            fonctionParent.executor.getExecutorState().getScopeManager().popCurrentScopeInstance();

            return result;
        }

        @Override
        public FrancaisLangType getType() {
            return FrancaisLangTypePrimitif.FONCTION;
        }
    }
}
