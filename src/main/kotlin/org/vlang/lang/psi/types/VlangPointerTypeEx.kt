package org.vlang.lang.psi.types

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class VlangPointerTypeEx(val inner: VlangTypeEx, anchor: PsiElement) : VlangBaseTypeEx(anchor) {
    override fun toString() = "&".safeAppend(inner)

    override fun qualifiedName(): String = "&".safeAppend(inner.qualifiedName())

    override fun readableName(context: PsiElement) = "&".safeAppend(inner.readableName(context))

    override fun module() = inner.module()

    override fun isAssignableFrom(rhs: VlangTypeEx, project: Project): Boolean {
        return when (rhs) {
            is VlangAnyTypeEx     -> true
            is VlangUnknownTypeEx -> true
            is VlangVoidPtrTypeEx -> true
            is VlangPointerTypeEx -> inner.isAssignableFrom(rhs.inner, project)
            else                  -> inner.isAssignableFrom(rhs, project)
        }
    }

    override fun isEqual(rhs: VlangTypeEx): Boolean {
        return rhs is VlangPointerTypeEx && inner.isEqual(rhs.inner)
    }

    override fun accept(visitor: VlangTypeVisitor) {
        if (!visitor.enter(this)) {
            return
        }

        inner.accept(visitor)
    }

    override fun substituteGenerics(nameMap: Map<String, VlangTypeEx>): VlangTypeEx {
        return VlangPointerTypeEx(inner.substituteGenerics(nameMap), anchor!!)
    }
}
