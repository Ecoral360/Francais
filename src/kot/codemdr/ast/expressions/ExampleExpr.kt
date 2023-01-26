package kot.codemdr.ast.expressions

import org.ascore.ast.buildingBlocs.Expression
import org.ascore.lang.objects.ASCObject

/**
 * Example of the implementation of an expression.
 */
class ExampleExpr : Expression<ASCObject<Any>> {
    /**
     * Often called at runtime, this method returns an ASCObject
     *
     * @return the result of the expression once evaluated
     */
    override fun eval(): ASCObject<Any> {
        return ASCObject.noValue()
    }
}
