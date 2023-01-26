package kot.codemdr.ast.statements

import kot.codemdr.ast.expressions.VarExpr
import kot.codemdr.execution.CodeMdrExecutorState
import org.ascore.ast.buildingBlocs.Statement
import org.ascore.executor.ASCExecutor

class DeclareVarStmt(
    val varExpr: VarExpr,
    executorInstance: ASCExecutor<CodeMdrExecutorState>
) : Statement(executorInstance) {

    override fun execute(): Any {
        TODO("Not yet implemented")
    }
}