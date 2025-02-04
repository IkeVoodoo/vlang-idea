package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.parentOfType
import org.vlang.ide.codeInsight.VlangCodeInsightUtil
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.*

class VlangAccessingPrivateSymbolInspection : VlangBaseInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitReferenceExpression(o: VlangReferenceExpression) {
                super.visitReferenceExpression(o)

                val containingFile = o.containingFile as? VlangFile ?: return
                if (containingFile.isTestFile() || containingFile.isTranslatedFile()) {
                    return
                }

                val resolved = o.resolve() ?: return
                if (resolved is VlangFieldDefinition && o.parent is VlangFieldName) {
                    return
                }

                val fromC = (resolved.containingFile as? VlangFile)?.isCFile() ?: false
                val fromStructForC = resolved.parentOfType<VlangStructDeclaration>()?.name?.startsWith("C.") ?: false

                if (resolved is VlangNamedElement &&
                    !fromStructForC &&
                    !fromC &&
                    !resolved.isPublic() &&
                    !VlangCodeInsightUtil.sameModule(o, resolved)
                ) {
                    val name = resolved.name
                    val ownerName = VlangCodeInsightUtil.ownerPresentableName(resolved) ?: return

                    holder.registerProblem(
                        o.getIdentifier(),
                        "Cannot access '$name': it is private in $ownerName",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }
}
