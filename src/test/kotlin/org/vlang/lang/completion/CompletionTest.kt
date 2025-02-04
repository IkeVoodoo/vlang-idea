package org.vlang.lang.completion

class CompletionTest : CompletionTestBase() {
    fun `test simple completion`() = checkIncludes(
        """
        module main
        
        struct Foo {
        }
        
        fn main() {
            <caret>
        }
        """.trimIndent(),
        1, "main", "Foo",
    )

    fun `test struct fields completion`() = checkEquals(
        """
        module main
        
        struct Foo {
        pub:
            name string
            age  int
        }
        
        fn main() {
            foo := Foo{}
            foo.<caret>
        }
        """.trimIndent(),
        1, "name", "age",
    )

    fun `test struct methods completion`() = checkEquals(
        """
        module main
        
        struct Foo {}
        
        fn (f Foo) bar() {}
        fn (f Foo) baz() {}
        
        fn main() {
            foo := Foo{}
            foo.<caret>
        }
        """.trimIndent(),
        1, "bar", "baz",
    )

    fun `test struct method and field with same name completion`() = checkEquals(
        """
        struct Foo {
            name string
        }
        
        fn (f Foo) name() {}
        
        fn main() {
            foo := Foo{}
            foo.<caret>
        }
        """.trimIndent(),
        1, "name", "name",
    )

    fun `test short string template completion`() = checkIncludes(
        """
        module main
        
        struct Foo {
        pub:
            name string
            age  int
        }
        
        fn main() {
            foo := Foo{}
            "{caret}"
        }
        """.trimIndent(),
        1, "foo",
    )

//    fun `test short string template member completion`() = checkEquals(
//        """
//        module main
//
//        struct Foo {
//        pub:
//            name string
//            age  int
//        }
//
//        fn main() {
//            foo := Foo{}
//            "{foo.<caret>}"
//        }
//        """.trimIndent(),
//        1, "name", "age",
//    )

    fun `test long string template completion`() = checkIncludes(
        """
        module main
        
        struct Foo {
        pub:
            name string
            age  int
        }
        
        fn main() {
            foo := Foo{}
            "{<caret>}"
        }
        """.trimIndent(),
        1, "foo",
    )

    fun `test struct name completion`() = doTestCompletion(
        """
        module main
        
        struct Name {}
        
        fn main() {
            Nam<caret>
        }
        """.trimIndent(),
        """
        module main
        
        struct Name {}
        
        fn main() {
            Name{}
        }
        """.trimIndent()
    )

    fun `test struct name before {} completion`() = doTestCompletion(
        """
        module main
        
        struct Name {}
        
        fn main() {
            Nam<caret>{}
        }
        """.trimIndent(),
        """
        module main
        
        struct Name {}
        
        fn main() {
            Name{}
        }
        """.trimIndent()
    )

    fun `test enum fetch`() = checkEquals(
        """
        module main
        
        enum Colors {
            red
            green
        }
        
        fn red() {}
        fn green() {}
        
        fn main() {
            a := Colors.red
            a = .<caret>
        }
        """.trimIndent(), 1, "red", "green"
    )
}
