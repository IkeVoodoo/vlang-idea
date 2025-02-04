package org.vlang.ide.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.SyntaxTraverser
import org.vlang.ide.documentation.DocumentationGenerator.generateCompileTimeConstantDoc
import org.vlang.ide.documentation.DocumentationGenerator.generateDoc
import org.vlang.lang.completion.VlangCompletionUtil
import org.vlang.lang.doc.psi.VlangDocComment
import org.vlang.lang.psi.*
import java.util.function.Consumer

class VlangDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        if (element is VlangReferenceExpression && VlangCompletionUtil.isCompileTimeIdentifier(element.getIdentifier())) {
            return generateCompileTimeConstantDoc(element)
        }

        return when (element) {
            is VlangFunctionOrMethodDeclaration -> element.generateDoc()
            is VlangStructDeclaration           -> element.generateDoc()
            is VlangInterfaceDeclaration        -> element.generateDoc()
            is VlangEnumDeclaration             -> element.generateDoc()
            is VlangConstDefinition             -> element.generateDoc()
            is VlangVarDefinition               -> element.generateDoc(originalElement)
            is VlangParamDefinition             -> element.generateDoc(originalElement)
            is VlangGlobalVariableDefinition    -> element.generateDoc()
            is VlangFieldDefinition             -> element.generateDoc()
            is VlangInterfaceMethodDefinition   -> element.generateDoc()
            is VlangReceiver                    -> element.generateDoc()
            is VlangEnumFieldDefinition         -> element.generateDoc()
            is VlangGenericParameter            -> element.generateDoc()
            is VlangTypeAliasDeclaration        -> element.generateDoc()
            is VlangModuleClause                -> element.generateDoc()
            else                                -> null
        }
    }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        if (file !is VlangFile) return
        for (element in SyntaxTraverser.psiTraverser(file)) {
            if (element is VlangDocComment && element.owner != null) {
                sink.accept(element)
            }
        }
    }
    
    override fun generateRenderedDoc(comment: PsiDocCommentBase): String? {
        return (comment as? VlangDocComment)?.documentationAsHtml(renderMode = VlangDocRenderMode.INLINE_DOC_COMMENT)
    }
}
