package com.github.hauner.openapi.processor.core

class TestSets {

    static def ALL = [
        'bean-validation',
        'deprecated',
        'endpoint-exclude',
        'endpoint-http-mapping',                    // framework specific
        'method-operation-id',
        'params-additional',
        'params-complex-data-types',                // framework specific
        'params-enum',
        'params-path-simple-data-types',            // framework specific
        'params-request-body',                      // framework specific
        'params-request-body-multipart-form-data',  // framework specific
        'params-simple-data-types',                 // framework specific
        'ref-into-another-file',
        'ref-into-another-file-path',
        'ref-is-relative-to-current-file',
        'ref-loop',
        'ref-loop-array',
        'ref-parameter',
        'ref-parameter-with-primitive-mapping',
        'response-array-data-type-mapping',
        'response-complex-data-types',
        'response-content-multiple',
        'response-content-multiple-no-content',
        'response-content-single',
        'response-result-mapping',
        'response-simple-data-types',
        'response-single-multi-mapping',
        'schema-composed',
        'schema-composed-allof',
        'schema-composed-allof-notype'
    ]

}
