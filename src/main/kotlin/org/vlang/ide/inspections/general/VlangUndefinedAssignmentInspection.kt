package org.vlang.ide.inspections.general

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.vlang.ide.inspections.VlangBaseInspection
import org.vlang.lang.psi.VlangAssignmentStatement
import org.vlang.lang.psi.VlangVisitor

// If a variable that does not exist is attempted to be assigned, this will trigger
class VlangUndefinedAssignmentInspection : VlangBaseInspection() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : VlangVisitor() {
            override fun visitAssignmentStatement(o: VlangAssignmentStatement) {
                holder.registerProblem(
                    o.assignOp,
                    "Testing",
                    ProblemHighlightType.ERROR
                )
            }
        }
    }

}