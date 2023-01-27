package codemdr.ast.expressions;

import codemdr.objects.CodeMdrObj;
import codemdr.objects.CodeMdrTableau;
import org.ascore.ast.buildingBlocs.Expression;
import org.ascore.errors.ASCErrors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumerationExpr implements Expression<CodeMdrTableau> {
    private final ArrayList<Expression<?>> elements = new ArrayList<>();
    private boolean isComplete = false;

    public EnumerationExpr(Expression<?>... valeurs) {
        this.elements.addAll(List.of(valeurs));
    }

    public static EnumerationExpr completeEnumeration(Expression<?>... valeurs) {
        var enumeration = new EnumerationExpr(valeurs);
        enumeration.setComplete(true);
        return enumeration;
    }

    public void addElement(Expression<?> valeur) {
        elements.add(valeur);
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public ArrayList<CodeMdrObj<?>> evalValeurs() {
        if (!isComplete) {
            throw new ASCErrors.ASCError("L'énumération n'a pas été fermé par un `et`", "ErreurEnumération");
        }
        return elements.stream().map(e -> (CodeMdrObj<?>) e.eval())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public CodeMdrTableau eval() {
        return null;
    }
}
