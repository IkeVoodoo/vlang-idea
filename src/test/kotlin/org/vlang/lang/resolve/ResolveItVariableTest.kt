package org.vlang.lang.resolve

open class ResolveItVariableTest : ResolveTestBase() {
    fun `test it in function call without param name for function type`() {
        mainFile("a.v", """
            module main

            fn main() {
                [1, 2, 3].filter(/*caret*/it > 100)
                [1, 2, 3].any(/*caret*/it > 100)
                [1, 2, 3].map(/*caret*/it > 100)
            }
        """.trimIndent())

        setupBuiltin()
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("PARAM_DEFINITION null")
    }

    fun `test it type in array methods`() {
        mainFile("a.v", """
            module main
            
            struct Foo {
                age int
            }
            
            fn main() {
                arr := [Foo{}]
                arr.map(/*caret*/it./*caret*/age)
                arr.filter(/*caret*/it./*caret*/age > 100)
                arr.any(/*caret*/it./*caret*/age > 100)
            }
        """.trimIndent())

        setupBuiltin()

        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("FIELD_DEFINITION age")
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("FIELD_DEFINITION age")
        assertReferencedTo("PARAM_DEFINITION null")
        assertReferencedTo("FIELD_DEFINITION age")
    }
}
