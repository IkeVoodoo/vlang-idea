package org.vlang.lang.completion

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import org.vlang.lang.psi.VlangFile

class VlangEnterHandlerDelegate : EnterHandlerDelegate {
    override fun preprocessEnter(
        file: PsiFile,
        editor: Editor,
        caretOffset: Ref<Int>,
        caretAdvance: Ref<Int>,
        dataContext: DataContext,
        originalHandler: EditorActionHandler?,
    ): EnterHandlerDelegate.Result {
        return EnterHandlerDelegate.Result.Continue
    }

    override fun postProcessEnter(file: PsiFile, editor: Editor, dataContext: DataContext): EnterHandlerDelegate.Result {
        val offset = editor.caretModel.currentCaret.offset
        val element = file.findElementAt(offset - 2) ?: return EnterHandlerDelegate.Result.Continue
        if (element.parent is VlangFile) {
            editor.document.deleteString(offset - 1, offset)
            return EnterHandlerDelegate.Result.Stop
        }
        return EnterHandlerDelegate.Result.Continue
    }
}
