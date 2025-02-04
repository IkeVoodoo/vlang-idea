package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.lang.psi.VlangEnumDeclaration
import org.vlang.lang.psi.VlangStructDeclaration
import org.vlang.lang.stubs.index.VlangClassLikeIndex

class VlangEnumTypeEx(val name: String, anchor: PsiElement) :
    VlangBaseTypeEx(anchor), VlangImportableTypeEx, VlangResolvableTypeEx<VlangEnumDeclaration> {

    override fun toString() = name

    override fun qualifiedName(): String {
        if (moduleName.isEmpty()) {
            return name
        }
        return "$moduleName.$name"
    }

    override fun readableName(context: PsiElement) = VlangCodeInsightUtil.getQualifiedName(context, anchor!!, name)

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangEnumTypeEx    -> {
                // Temp approach?
                return name == rhs.name
            }

            else                  -> false
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangEnumTypeEx && name == rhs.name
    }

    override fun accept(visitor: VlangTypeVisitor) {
        visitor.enter(this)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx = this

    override fun resolve(project: Project): VlangEnumDeclaration? {
        val variants = VlangClassLikeIndex.find(qualifiedName(), project, null, null)
        return variants.firstOrNull() as? VlangEnumDeclaration
    }
}
