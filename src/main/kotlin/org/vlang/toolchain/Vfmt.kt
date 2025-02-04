package org.vlang.toolchain

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.util.DocumentUtil
import com.intellij.util.execution.ParametersListUtil
import org.vlang.configurations.VlangConfigurationUtil
import org.vlang.configurations.VlangFmtSettingsState.Companion.vfmtSettings
import org.vlang.configurations.VlangProjectSettingsState.Companion.projectSettings
import org.vlang.notifications.VlangErrorNotification
import org.vlang.utils.isNotVlangFile
import org.vlang.utils.runProcessWithGlobalProgress
import org.vlang.utils.virtualFile
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class Vfmt {
    fun reformatDocument(project: Project, document: Document) {
        ApplicationManager.getApplication().assertIsDispatchThread()
        if (!document.isWritable) return
        val formattedText = reformatDocumentTextOrNull(project, document) ?: return
        DocumentUtil.writeInRunUndoTransparentAction { document.setText(formattedText) }
    }

    private fun reformatDocumentTextOrNull(project: Project, document: Document): String? {
        val cmd = createCommandLine(project, document) ?: return null
        return runProcess(cmd, document, project)
    }

    private fun runProcess(
        cmd: GeneralCommandLine,
        document: Document,
        project: Project,
    ): String? {
        val processOutput = StringBuilder()
        try {
            val handler = VlangCapturingProcessHandler(cmd)

            handler.processInput.write(document.text.toByteArray())
            handler.processInput.flush()
            handler.processInput.close()

            val future = ApplicationManager.getApplication().executeOnPooledThread(Callable {
                handler.runProcessWithGlobalProgress(timeoutInMilliseconds = 1000)
            })

            val output = future.get(1, TimeUnit.SECONDS)
            if (output.isCancelled) {
                return null
            }
            if (output.exitCode != 0) {
                VlangErrorNotification(output.stderr)
                    .withTitle("Can't reformat")
                    .show(project)
                return null
            }

            processOutput.append(output.stdout)
        } catch (e: ExecutionException) {
            VlangErrorNotification(e.message ?: "")
                .withTitle("Can't reformat")
                .show(project)
            return null
        }

        return processOutput.toString()
    }

    private fun createCommandLine(project: Project, document: Document): GeneralCommandLine? {
        val file = document.virtualFile ?: return null
        if (file.isNotVlangFile || !file.isValid) return null

        val settingsState = project.vfmtSettings

        val additionalArguments = ParametersListUtil.parse(settingsState.additionalArguments)
        val arguments = mutableListOf<String>()
        arguments.add("fmt")
        arguments.addAll(additionalArguments)

        val exe = project.projectSettings.compilerLocation
        if (exe == null) {
            VlangErrorNotification(VlangConfigurationUtil.TOOLCHAIN_NOT_SETUP)
                .withTitle("Can't reformat")
                .show(project)
            return null
        }

        return GeneralCommandLine()
            .withExePath(exe)
            .withParameters(arguments)
            .withEnvironment(settingsState.envs)
            .withCharset(Charsets.UTF_8)
    }
}
