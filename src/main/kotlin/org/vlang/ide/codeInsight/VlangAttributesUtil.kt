package org.vlang.ide.codeInsight

import org.vlang.lang.psi.VlangAttribute
import org.vlang.lang.psi.VlangStructDeclaration

object VlangAttributesUtil {
    fun isTranslated(attribute: VlangAttribute): Boolean {
        val expr = attribute.attributeExpressionList.firstOrNull() ?: return false
        return expr.text == "translated"
    }

    fun isNoReturn(attribute: VlangAttribute): Boolean {
        val expr = attribute.attributeExpressionList.firstOrNull() ?: return false
        return expr.text == "noreturn"
    }

    fun isMinifiedStruct(struct: VlangStructDeclaration): Boolean {
        return struct.attributes?.attributeList?.any {
            it.attributeExpressionList.any { expr -> expr.textMatches("minify") }
        } ?: false
    }

    fun isParamsStruct(struct: VlangStructDeclaration): Boolean {
        return struct.attributes?.attributeList?.any {
            it.attributeExpressionList.any { expr -> expr.textMatches("params") }
        } ?: false
    }
}
