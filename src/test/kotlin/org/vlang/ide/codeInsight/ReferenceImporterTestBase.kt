package org.vlang.ide.codeInsight

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzerSettings
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import org.vlang.ide.inspections.general.VlangUnresolvedReferenceInspection

abstract class ReferenceImporterTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/codeInsight/imports"

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(VlangUnresolvedReferenceInspection::class.java)
        (myFixture as CodeInsightTestFixtureImpl).canChangeDocumentDuringHighlighting(true)
    }

    protected fun doTest(before: String, after: String) {
        DaemonCodeAnalyzerSettings.getInstance().isImportHintEnabled = true
        myFixture.copyDirectoryToProject("vlib", "")
        myFixture.copyDirectoryToProject("other", "")
        myFixture.configureByText("a.v", before)
        myFixture.getAllQuickFixes().forEach { myFixture.launchAction(it) }
        myFixture.checkResult(after)
    }
}
