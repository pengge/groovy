package com.xseagullx.groovy.gsoc
import com.xseagullx.groovy.gsoc.util.ASTComparatorCategory
import org.codehaus.groovy.control.ErrorCollector
import spock.lang.Specification

class MainTest extends Specification {
    def "test arguments handling"() {
        // TODO ask someone for help. IDK how to test arguments without global mocking as described here.
        // http://spock-framework.readthedocs.org/en/latest/interaction_based_testing.html#mocking-constructors
    }

    def "test process method"() {
        setup:
        def sourceFile = new File("test_res/com/xseagullx/groovy/gsoc/TestClass1.groovy")

        def moduleNodeNew = new Main(Configuration.NEW).process(sourceFile)
        def moduleNodeOld = new Main(Configuration.OLD).process(sourceFile)
        def moduleNodeOld2 = new Main(Configuration.OLD).process(sourceFile)

        expect:
        use(ASTComparatorCategory) {
            assert moduleNodeOld == moduleNodeOld2;
            assert moduleNodeNew == moduleNodeOld
            true
        }
    }

    def "test class modifiers"() {
        setup:
        def sourceFile = new File("test_res/com/xseagullx/groovy/gsoc/ClassModifiers_Issue_2.groovy")

        def moduleNodeNew = new Main(Configuration.NEW).process(sourceFile)
        def moduleNodeOld = new Main(Configuration.OLD).process(sourceFile)
        def moduleNodeOld2 = new Main(Configuration.OLD).process(sourceFile)

        expect:
        use(ASTComparatorCategory) {
            assert moduleNodeOld == moduleNodeOld2;
            assert moduleNodeNew == moduleNodeOld
            true
        }
    }

    def "test invalid class modifiers"() {
        expect:
        def sourceFile = new File(file)

        def errorCollectorNew = new Main(Configuration.NEW).process(sourceFile).context.errorCollector
        def errorCollectorOld = new Main(Configuration.OLD).process(sourceFile).context.errorCollector
        def errorCollectorOld2 = new Main(Configuration.OLD).process(sourceFile).context.errorCollector


        def cl = { ErrorCollector errorCollector, int it -> def s = new StringWriter(); errorCollector.getError(it).write(new PrintWriter(s)); s.toString() }
        def errOld1 = (0..<errorCollectorOld.errorCount).collect cl.curry(errorCollectorOld)
        def errOld2 = (0..<errorCollectorOld2.errorCount).collect cl.curry(errorCollectorOld2)
        def errNew = (0..<errorCollectorNew.errorCount).collect cl.curry(errorCollectorNew)

        assert errOld1 == errOld2
        assert errOld1 == errNew

        where:
        file | output
        "test_res/com/xseagullx/groovy/gsoc/ClassModifiersInvalid_Issue1_2.groovy" | _
        "test_res/com/xseagullx/groovy/gsoc/ClassModifiersInvalid_Issue2_2.groovy" | _
    }

    def "test class file creation"() {
        expect:
        def sourceFile = new File("test_res/com/xseagullx/groovy/gsoc/TestClass1.groovy")

        def main = new Main(Configuration.NEW)
        main.compile(sourceFile)
    }
}
