package org.vlang.lang.psi.types

import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangCompositeElement
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.VlangEnumType

class VlangEnumTypeEx(raw: VlangEnumType): VlangBaseTypeEx<VlangEnumType>(raw), VlangImportableType {
    private val decl = raw.parent as VlangEnumDeclaration
    private val name = decl.getQualifiedName() ?: ANON

    override fun toString() = name

    override fun qualifiedName() = name

    override fun readableName(context: VlangCompositeElement) = VlangCodeInsightUtil.getQualifiedName(context, decl)

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }
}
