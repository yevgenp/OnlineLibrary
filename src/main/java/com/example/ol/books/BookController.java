package com.example.ol.books;

import com.example.ol.model.hibernate.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/api/books")
@RestController
public class BookController {
  @Autowired private BookService service;

  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  @RequestMapping(method = RequestMethod.POST, consumes = {"multipart/form-data"})
  public ResponseEntity<?> addBook(@RequestPart("book") @Valid Book book, Errors errors,
                                   @RequestPart("file") MultipartFile file) {
    if (errors.hasErrors())
      return new ResponseEntity<>(errors.getFieldErrors(), HttpStatus.BAD_REQUEST);
    book = service.save(book, file);
    Resource<Book> resource = new Resource<>(book);
    resource.add(service.getSelfLink(book));
    return new ResponseEntity<>(resource, HttpStatus.OK);
  }

  @RequestMapping(path = "find", method = RequestMethod.POST)
  public ResponseEntity<?> search(@RequestBody BookSearch search,
                                  Pageable pageable) {
    Page<Book> bookPage = service.search(search, pageable);
    return new ResponseEntity<>(service.toPagedResource(bookPage), HttpStatus.OK);
  }

  @RequestMapping(path = "authors", method = RequestMethod.POST)
  public ResponseEntity<Page<String>> getAuthors(
                        @RequestBody(required = false) String search,
                        Pageable pageable) {
    Page<String> authors = service.getAuthors(search, pageable);
    return new ResponseEntity<>(authors, HttpStatus.OK);
  }

  @RequestMapping(path = "genres", method = RequestMethod.POST)
  public ResponseEntity<Page<String>> getGenres(
                        @RequestBody(required = false) String search,
                        Pageable pageable) {
    Page<String> genres = service.getGenres(search, pageable);
    return new ResponseEntity<>(genres, HttpStatus.OK);
  }

  @PreAuthorize("hasAuthority('USER')")
  @RequestMapping(path = "{id}/download", method = RequestMethod.GET)
  public void download(@PathVariable Long id, HttpServletResponse response) {
    Book book = service.getBookOrExit(id);
    service.copyToResponse(book, response);
  }

}
