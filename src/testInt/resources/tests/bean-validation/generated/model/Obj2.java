/*
 * This class is auto generated by https://github.com/hauner/openapi-processor-core.
 * TEST ONLY.
 */

package generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Size;

public class Obj2 {

    @JsonProperty("prop4")
    @Size(max = 10)
    private String prop4;

    public String getProp4() {
        return prop4;
    }

    public void setProp4(String prop4) {
        this.prop4 = prop4;
    }

}
