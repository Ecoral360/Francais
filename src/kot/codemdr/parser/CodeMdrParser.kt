package kot.codemdr.parser

import kot.codemdr.ast.expressions.AddExpr
import kot.codemdr.ast.expressions.ConstValueExpr
import kot.codemdr.ast.statements.PrintStmt
import kot.codemdr.execution.CodeMdrExecutorState
import kot.codemdr.lexer.CodeMdrLexer
import kot.codemdr.objects.CodeMdrFloat
import kot.codemdr.objects.CodeMdrInt
import kot.codemdr.objects.CodeMdrString
import org.ascore.ast.buildingBlocs.Expression
import org.ascore.ast.buildingBlocs.Statement
import org.ascore.executor.ASCExecutor
import org.ascore.generators.ast.AstGenerator
import org.ascore.tokens.Token

/**
 * The parser for the CodeMdr language.
 *
 * This parser is responsible for defining the rules for parsing the CodeMdr language. The actual parsing is done by the
 * [AstGenerator] class in accordance with the rules defined in this class.
 *
 *  * Edit the [addExpressions] method to add new expressions to the language.
 *  * Edit the [addStatements] method to add new statements to the language.
 *
 *  @param executorInstance the executor instance to use for executing the AST
 */
class CodeMdrParser(executorInstance: ASCExecutor<CodeMdrExecutorState>) : AstGenerator<CodeMdrAstFrameKind>() {
    private val executorInstance: ASCExecutor<CodeMdrExecutorState>

    init {
        setPatternProccessor(executorInstance.getLexer<CodeMdrLexer>()::remplaceCategoriesByMembers)
        defineAstFrame(CodeMdrAstFrameKind.DEFAULT)
        addStatements()
        addExpressions()
        pushAstFrame(CodeMdrAstFrameKind.DEFAULT)
        this.executorInstance = executorInstance
    }

    /**
     * Defines the rules of the statements of the language.
     */
    private fun addStatements() {
        // add your statements here
        addStatement("DECLARE VARIABLE ASSIGN expression") { p: List<Any> -> PrintStmt(p[1] as Expression<*>) }
        addStatement("PRINT expression") { p: List<Any> -> PrintStmt(p[1] as Expression<*>) }
        addStatement("expression") { _ -> Statement.EMPTY_STATEMENT }
        addStatement("") { _ -> Statement.EMPTY_STATEMENT }
    }

    /**
     * Defines the rules of the expressions of the language.
     */
    private fun addExpressions() {
        // add your expressions here
        addExpression("{datatypes}") { p: List<Any> ->
            val token = p[0] as Token
            when (token.name()) {
                "INT" -> ConstValueExpr(CodeMdrInt(token))
                "FLOAT" -> ConstValueExpr(CodeMdrFloat(token))
                "STRING" -> ConstValueExpr(CodeMdrString(token))
                else -> throw NoSuchElementException(token.name())
            }
        }
        addExpression("expression PLUS expression") { p: List<Any> ->
            AddExpr(
                p[0] as Expression<*>, p[2] as Expression<*>
            )
        }
    }
}
