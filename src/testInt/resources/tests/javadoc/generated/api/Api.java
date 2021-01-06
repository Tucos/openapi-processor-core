/*
 * This class is auto generated by https://github.com/hauner/openapi-processor-core.
 * TEST ONLY.
 */

package generated.api;

import annotation.Mapping;
import annotation.Parameter;
import generated.model.Foo;

public interface Api {

    /**
     * a <em>markdown</em> description with <strong>text</strong>
     *
     * <ul>
     *   <li>one list item
     *   <li>second list item
     * </ul>
     *
     * <pre><code>code block
     * </code></pre>
     *
     * more
     *
     * @param foo this is a <em>parameter</em> description
     * @return this is a *response* description
     */
    @Mapping("/foo")
    Foo getFoo(@Parameter Foo foo);

}
