package codemdr.ast.expressions

import org.ascore.ast.buildingBlocs.Expression
import org.ascore.lang.objects.ASCObject


data class ConstValueExpr(private val value: ASCObject<*>) : Expression<ASCObject<*>> {
    /**
     * Appel\u00E9 durant le Runtime, cette m\u00E9thode retourne un objet de type ASObjet
     *
     * @return le r\u00E9sultat de l'expression
     */
    override fun eval(): ASCObject<*> {
        return value
    }
}
