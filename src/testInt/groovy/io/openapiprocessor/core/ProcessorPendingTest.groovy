/*
 * Copyright 2019 https://github.com/openapi-processor/openapi-processor-core
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.core

import io.openapiprocessor.core.parser.ParserType
import io.openapiprocessor.core.test.TestProcessor
import io.openapiprocessor.test.TestSet
import io.openapiprocessor.test.TestSetRunner
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Unroll

/**
 * helper to run selected integration tests.
 */
@Ignore
class ProcessorPendingTest extends Specification {

    static Collection<TestSet> sources () {
        return [
            new TestSet(name: 'javadoc', processor: new TestProcessor(), parser: ParserType.SWAGGER),
            new TestSet(name: 'javadoc', processor: new TestProcessor(), parser: ParserType.OPENAPI4J)
        ]
    }

    @TempDir
    public File folder

    @Unroll
    void "native - #testSet"() {
        def runner = new TestSetRunner (testSet)
        def success = runner.runOnNativeFileSystem (folder)

        expect:
        assert success: "** found differences! **"

        where:
        testSet << sources ()
    }

}
