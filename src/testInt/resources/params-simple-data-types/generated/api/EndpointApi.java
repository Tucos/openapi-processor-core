/*
 * This class is auto generated by https://github.com/hauner/openapi-generatr-spring.
 * DO NOT EDIT.
 */

package generated.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface EndpointApi {

    @GetMapping(path = "/endpoint")
    ResponseEntity<Void> getEndpoint(@RequestParam(name = "foo") String foo);

    @GetMapping(path = "/endpoint-optional")
    ResponseEntity<Void> getEndpointOptional(
            @RequestParam(name = "foo", required = false, defaultValue = "bar") String foo);

}
