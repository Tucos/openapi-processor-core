/*
 * This class is auto generated by https://github.com/hauner/openapi-generatr-spring.
 * DO NOT EDIT.
 */

package generated.api;

import generated.model.Book;
import generated.model.BookInlineResponse200;
import generated.model.BookNested;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface Api {

    @GetMapping(path = "/book-inline", produces = {"application/json"})
    ResponseEntity<BookInlineResponse200> getBookInline();

    @GetMapping(path = "/book", produces = {"application/json"})
    ResponseEntity<Book> getBook();

    @GetMapping(path = "/book-nested", produces = {"application/json"})
    ResponseEntity<BookNested> getBookNested();

}
