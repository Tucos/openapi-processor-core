/*
 * This class is auto generated by https://github.com/hauner/openapi-generatr-spring.
 * DO NOT EDIT.
 */

package generated.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface Api {

    @GetMapping(
            path = "/string",
            produces = {"text/plain"})
    ResponseEntity<String> getString();

    @GetMapping(
            path = "/integer",
            produces = {"application/vnd.integer"})
    ResponseEntity<Integer> getInteger();

    @GetMapping(
            path = "/long",
            produces = {"application/vnd.long"})
    ResponseEntity<Long> getLong();

    @GetMapping(
            path = "/float",
            produces = {"application/vnd.float"})
    ResponseEntity<Float> getFloat();

    @GetMapping(
            path = "/double",
            produces = {"application/vnd.double"})
    ResponseEntity<Double> getDouble();

    @GetMapping(
            path = "/boolean",
            produces = {"application/vnd.boolean"})
    ResponseEntity<Boolean> getBoolean();

    @GetMapping(
            path = "/array-string",
            produces = {"application/vnd.array"})
    ResponseEntity<String[]> getArrayString();

}
