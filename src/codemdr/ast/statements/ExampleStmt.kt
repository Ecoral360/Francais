package codemdr.ast.statements

import org.ascore.ast.buildingBlocs.Statement
import org.ascore.executor.ASCExecutor
import org.ascore.executor.Coordinate
import org.ascore.tokens.Token

/**
 * Example of the implementation of a statement.
 */
class ExampleStmt : Statement {
    /**
     * If the statement doesn't need access to the executor [ASCExecutor] to work
     */
    constructor() : super() {}

    /**
     * If the statement needs access to the executor [ASCExecutor] to work
     *
     * @param executorInstance the executor instance
     */
    constructor(executorInstance: ASCExecutor<*>) : super(executorInstance) {}

    /**
     * Method that describes the effect of the statement
     *
     * @return Can return several things, which causes several effects:
     *
     *  *
     * `null` -> continues the execution to the next line
     * (most of the statements should return `null`)
     *
     *  *
     * `instance of Data` -> data to add to the list of data kept by the executor
     *
     *  *
     * other -> if the statement is executed inside a function, this value is returned by the function. Else, the value is ignored
     *
     *
     */
    override fun execute(): Any? {
        return null
    }

    /**
     * **This method is called when the statement is compiled (at compile time).**<br></br>
     * The override of this method is not necessary, but is useful when the goal is to change
     * the coordinate of the next line to compile by the [executor][ASCExecutor] (for example, in a loop or an if statement)
     *
     *
     *
     * @param coord the current coordinate
     * @param ligne the current line, tokenized
     * @return the coordinate of the next line to compile
     */
    override fun getNextCoordinate(coord: Coordinate, ligne: List<Token>): Coordinate {
        return super.getNextCoordinate(coord, ligne)
    }
}
