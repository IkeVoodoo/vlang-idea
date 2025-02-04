package org.vlang.lang.search

import com.intellij.openapi.application.QueryExecutorBase
import com.intellij.psi.search.searches.DefinitionsScopedSearch
import com.intellij.util.Processor
import org.vlang.lang.psi.VlangInterfaceDeclaration
import org.vlang.lang.psi.VlangInterfaceMethodDeclaration
import org.vlang.lang.psi.VlangMethodDeclaration
import org.vlang.lang.psi.VlangNamedElement
import org.vlang.lang.psi.VlangStructDeclaration

class VlangSuperMethodSearch : QueryExecutorBase<VlangInterfaceMethodDeclaration, DefinitionsScopedSearch.SearchParameters>(true) {
    override fun processQuery(
        parameters: DefinitionsScopedSearch.SearchParameters,
        consumer: Processor<in VlangInterfaceMethodDeclaration>,
    ) {
        if (!parameters.isCheckDeep) return

        val method = parameters.element as? VlangMethodDeclaration ?: return
        val methodName = method.name ?: return
        val owner = method.receiverType?.typeReferenceExpression?.resolve() as? VlangStructDeclaration ?: return

        val processor = Processor<VlangNamedElement> { interfaceDeclaration ->
            interfaceDeclaration as VlangInterfaceDeclaration
            val interfaceMethod = interfaceDeclaration
                .interfaceType
                .methodList
                .find { it.name == methodName }
                ?.parent as? VlangInterfaceMethodDeclaration

            interfaceMethod == null || method == interfaceMethod || consumer.process(interfaceMethod)
        }
        VlangSuperSearch.processMethodOwners(processor, owner, listOf(method))
    }

    companion object {
        val GO_SUPER_METHOD_SEARCH = VlangSuperMethodSearch()
    }
}
