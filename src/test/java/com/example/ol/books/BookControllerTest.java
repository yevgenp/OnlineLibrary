package com.example.ol.books;

import com.example.ol.model.hibernate.entity.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {
  @Mock private BookService service;
  @Mock private Book book;
  @Mock private Errors errors;
  @Mock private MultipartFile file;
  @Spy @InjectMocks private BookController controller;

  @Test
  public void shouldAddBook() throws Exception {
    //given
    doReturn(false).when(errors).hasErrors();
    doReturn(book).when(service).save(book, file);
    doReturn(new Link("test")).when(service).getSelfLink(any(Book.class));
    //when
    ResponseEntity<?> result = controller.addBook(book, errors, file);
    //then
    verify(service).save(book, file);
    assertEquals(((Resource<Book>)result.getBody()).getContent(), book);
  }

  @Test
  public void shouldNotAddInvalidBook() throws Exception {
    //given
    doReturn(true).when(errors).hasErrors();
    //when
    ResponseEntity<?> result = controller.addBook(book, errors, file);
    //then
    verify(service, never()).save(book, file);
    assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
  }

  @Test
  public void shouldSearch() throws Exception {
    //given
    BookSearch search = new BookSearch();
    Pageable pageRequest = new PageRequest(0, 10);
    Page bookPage = new PageImpl(Arrays.asList(book), pageRequest, 1);
    doReturn(bookPage).when(service).search(search, pageRequest);
    //when
    ResponseEntity<?> result = controller.search(search, pageRequest);
    //then
    verify(service).search(search, pageRequest);
    verify(service).toPagedResource(bookPage);
    assertEquals(result.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void shouldGetAuthors() throws Exception {
    //given
    Pageable pageRequest = new PageRequest(0, 10);
    List<String> authors = Arrays.asList("author1", "author2");
    Page<String> page = new PageImpl(authors, pageRequest, 2);
    doReturn(page).when(service).getAuthors(anyString(), any(Pageable.class));
    //when
    ResponseEntity<?> result = controller.getAuthors("search", pageRequest);
    //then
    assertEquals(((Page<String>)result.getBody()).getContent(), authors);
  }

  @Test
  public void shouldGetGenres() throws Exception {
    //given
    Pageable pageRequest = new PageRequest(0, 10);
    List<String> genres = Arrays.asList("author1", "author2");
    Page<String> page = new PageImpl(genres, pageRequest, 2);
    doReturn(page).when(service).getGenres(anyString(), any(Pageable.class));
    //when
    ResponseEntity<?> result = controller.getGenres("search", pageRequest);
    //then
    assertEquals(((Page<String>)result.getBody()).getContent(), genres);
  }

  @Test
  public void shouldDownload() throws Exception {
    //given
    HttpServletResponse response = mock(HttpServletResponse.class);
    //when
    controller.download(3L, response);
    //then
    verify(service).getBookOrExit(3L);
    verify(service).copyToResponse(any(Book.class), any(HttpServletResponse.class));
  }

}
