package com.github.hauner.openapi.spring.writer

import com.github.hauner.openapi.spring.ApiOptions
import com.github.hauner.openapi.spring.model.Api
import com.github.hauner.openapi.spring.model.ApiInterface
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ApiWriterSpec extends Specification {

    @Rule TemporaryFolder target

    List<String> apiPkgPath = ['com', 'github', 'hauner', 'openapi', 'api']

    void "creates package structure in target folder"() {
        def opts = new ApiOptions(
            packageName: 'com.github.hauner.openapi',
            targetFolder: [target.root.toString (), 'java', 'src'].join (File.separator)
        )

        when:
        new ApiWriter (opts, Stub (InterfaceWriter)).write (new Api())

        then:
        def api = new File([opts.targetFolder, 'com', 'github', 'hauner', 'openapi', 'api'].join(File.separator))
        def model = new File([opts.targetFolder, 'com', 'github', 'hauner', 'openapi', 'model'].join(File.separator))
        api.exists ()
        api.isDirectory ()
        model.exists ()
        model.isDirectory ()
    }

    void "generates interface sources in api target folder"() {
        def interfaceWriter = Stub (InterfaceWriter) {
            write (_ as Writer, _ as ApiInterface) >> {
                Writer writer = it.get(0)
                writer.write ('Foo interface!\n')
            } >> {
                Writer writer = it.get(0)
                writer.write ('Bar interface!\n')
            }
        }

        def opts = new ApiOptions(
            packageName: 'com.github.hauner.openapi',
            targetFolder: [target.root.toString (), 'java', 'src'].join (File.separator)
        )

        def api = new Api(interfaces: [
            new ApiInterface(pkg: "${opts.packageName}.api", name: 'Foo'),
            new ApiInterface(pkg: "${opts.packageName}.api", name: 'Bar')
        ])

        when:
        new ApiWriter (opts, interfaceWriter).write (api)

        then:
        def fooSource = new File(getApiPath (opts.targetFolder, 'FooApi.java'))
        fooSource.text == """\
Foo interface!
"""
        def barSource = new File(getApiPath (opts.targetFolder, 'BarApi.java'))
        barSource.text == """\
Bar interface!
"""
    }

    String getApiPath(String targetFolder, String clazzName) {
        ([targetFolder] + apiPkgPath + [clazzName]).join(File.separator)
    }
}
