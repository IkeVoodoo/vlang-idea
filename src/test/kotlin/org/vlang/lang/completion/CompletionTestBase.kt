package org.vlang.lang.completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.Lookup.NORMAL_SELECT_CHAR
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class CompletionTestBase : BasePlatformTestCase() {
    override fun getTestDataPath() = "src/test/resources/completion"

    protected open fun doTestCompletion(
        txt: String,
        after: String,
    ) {
        myFixture.configureByText("a.v", txt)
        val variants = myFixture.complete(CompletionType.BASIC)
        if (variants != null) {
            myFixture.finishLookup(NORMAL_SELECT_CHAR)
        }
        myFixture.checkResult(after)
    }

    enum class CheckType {
        EQUALS, INCLUDES, EXCLUDES, ORDERED_EQUALS
    }

    fun checkIncludes(
        txt: String,
        count: Int,
        vararg variants: String,
    ) = doTestVariants(txt, CompletionType.BASIC, count, CheckType.INCLUDES, *variants)

    fun checkEquals(
        txt: String,
        count: Int,
        vararg variants: String,
    ) = doTestVariants(txt, CompletionType.BASIC, count, CheckType.EQUALS, *variants)

    fun checkExcludes(
        txt: String,
        count: Int,
        vararg variants: String,
    ) = doTestVariants(txt, CompletionType.BASIC, count, CheckType.EXCLUDES, *variants)

    protected fun doTestVariants(
        txt: String,
        type: CompletionType,
        count: Int,
        checkType: CheckType,
        vararg variants: String,
    ) {
        myFixture.configureByText("a.v", txt)
        doTestVariantsInner(type, count, checkType, *variants)
    }

    open fun doTestVariantsInner(type: CompletionType, count: Int, checkType: CheckType, vararg variants: String) {
        myFixture.complete(type, count)
        val stringList = myFixture.lookupElementStrings ?: return
        assertNotNull(
            """
            
            Possibly the single variant has been completed.
            File after:
            ${myFixture.file.text}
            """.trimIndent(), stringList
        )
        val varList = mutableListOf<String>()

        when (checkType) {
            CheckType.ORDERED_EQUALS -> {
                assertOrderedEquals(stringList, *variants)
            }
            CheckType.EQUALS         -> {
                assertSameElements(stringList, *variants)
            }
            CheckType.INCLUDES       -> {
                varList.removeAll(stringList.toSet())
                assertTrue("Missing variants: $varList", varList.isEmpty())
            }
            CheckType.EXCLUDES       -> {
                varList.retainAll(stringList.toSet())
                assertTrue("Unexpected variants: $varList", varList.isEmpty())
            }
        }
    }
}
