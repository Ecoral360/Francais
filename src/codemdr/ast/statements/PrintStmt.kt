package codemdr.ast.statements

import org.ascore.ast.buildingBlocs.Expression
import org.ascore.ast.buildingBlocs.Statement

/**
 * Class representing a print statement. It takes an [Expression] as argument and prints it when executed at runtime.
 */
class PrintStmt(private val expression: Expression<*>) : Statement() {

    /**
     * Method called at runtime that executes the print statement, thus printing the expression.
     */
    override fun execute(): Any? {
        println(expression.eval().value)
        return null
    }
}
