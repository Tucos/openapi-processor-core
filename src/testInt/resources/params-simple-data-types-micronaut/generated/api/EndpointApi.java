/*
 * This class is auto generated by https://github.com/hauner/openapi-processor-micronaut.
 * DO NOT EDIT.
 */

package generated.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

public interface EndpointApi {

    @Get(uri = "/endpoint")
    HttpResponse<Void> getEndpoint(@QueryValue(value = "foo") String foo);

    @Get(uri = "/endpoint-optional")
    HttpResponse<Void> getEndpointOptional(
            @QueryValue(value = "foo", defaultValue = "bar") String foo);

}