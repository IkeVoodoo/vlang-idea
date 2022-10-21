package org.vlang.ide.inspections.general

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangAssignmentStatement
import org.vlang.lang.psi.VlangVisitor

// If a variable that does not exist is attempted to be assigned, this will trigger
class VlangUndefinedAssignmentInspection : VlangBaseInspection() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitAssignmentStatement(o: VlangAssignmentStatement) {
                val left = o.leftHandExprList
                val last = left.lastChild
                val ref = last.reference?.resolve()
                if(ref == null) {
                    holder.registerProblem(
                        last,
                        "Assignment to non-existent variable '$last'",
                        ProblemHighlightType.ERROR, DEFINITION_FIX
                    )
                }
            }
        }
    }

    class DefinitionQuickFix : LocalQuickFix {
        override fun getFamilyName() = "Convert to definition"

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            // TODO convert = to :=
        }

    }

    companion object {
        private val DEFINITION_FIX = DefinitionQuickFix()
    }
}