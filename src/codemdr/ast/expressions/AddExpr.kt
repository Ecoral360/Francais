package codemdr.ast.expressions

import codemdr.objects.CodeMdrFloat
import codemdr.objects.CodeMdrInt
import codemdr.objects.CodeMdrNumber
import codemdr.objects.CodeMdrString
import org.ascore.ast.buildingBlocs.Expression
import org.ascore.lang.objects.ASCObject
import org.ascore.lang.objects.containsType
import kotlin.math.floor

private operator fun Number.plus(value: Number): Double = this.toDouble() + value.toDouble()
private fun Double.isInt(): Boolean = floor(this) == this

data class AddExpr(private val left: Expression<*>, private val right: Expression<*>) : Expression<ASCObject<*>> {
    override fun eval(): ASCObject<*> {
        val leftValue = left.eval()
        val rightValue = right.eval()
        return when {
            leftValue.containsType<String>() || rightValue.containsType<String>() -> {
                CodeMdrString("" + leftValue.value + rightValue.value)
            }

            leftValue.containsType<Number>() && rightValue.containsType<Number>() -> {
                val result = (leftValue.value!! as Number) + (rightValue.value!! as Number)
                if (result.isInt()) CodeMdrInt(result.toInt()) else CodeMdrFloat(result)
            }

            else -> throw ArithmeticException("Addition not supported for '${leftValue.javaClass.simpleName}' and '${rightValue.javaClass.simpleName}'")
        }
    }
}
