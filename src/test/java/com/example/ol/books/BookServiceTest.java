package com.example.ol.books;

import com.example.ol.core.exceptions.NoSuchBookException;
import com.example.ol.model.hibernate.dao.BookDao;
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
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {
  @Mock private BookDao bookDao;
  @Mock private Book book;
  @Spy @InjectMocks private BookService service;

  @Test
  public void shouldSaveBook() throws Exception {
    //given
    MultipartFile file = mock(MultipartFile.class);
    //when
    service.save(book, file);
    //then
    verify(book).setFilename(anyString());
    verify(book).setContent(any());
    verify(bookDao).save(book);
  }

  @Test
  public void shouldSearch() throws Exception {
    //given
    BookSearch search = BookSearch.builder()
      .key("key").author("author").genre("genre").build();
    PageRequest pageRequest = new PageRequest(1, 33);
    //when
    service.search(search, pageRequest);
    //then
    verify(bookDao).search(search.getKey(),search.getAuthor(),
                           search.getGenre(), pageRequest);
  }

  @Test
  public void shouldGetAuthors() throws Exception {
    //given
    PageRequest pageRequest = new PageRequest(1, 33);
    //when
    service.getAuthors("search", pageRequest);
    //then
    verify(bookDao).getAuthors("search", pageRequest);
  }

  @Test
  public void shouldGetGenres() throws Exception {
    //given
    PageRequest pageRequest = new PageRequest(1, 33);
    //when
    service.getGenres("search", pageRequest);
    //then
    verify(bookDao).getGenres("search", pageRequest);
  }

  @Test
  public void shouldGetBook() throws Exception {
    //given
    doReturn(book).when(bookDao).findOne(2L);
    //when
    Book result = service.getBookOrExit(2L);
    //then
    assertEquals(book, result);
  }

  @Test(expected = NoSuchBookException.class)
  public void shouldThrowExceptionWhenBookNotFound() throws Exception {
    //given
    //when
    service.getBookOrExit(2L);
    //then exception
  }

  @Test
  public void shouldCopyToResponse() throws Exception {
    //given
    HttpServletResponse response = mock(HttpServletResponse.class);
    OutputStream out = mock(ServletOutputStream.class);
    doReturn(out).when(response).getOutputStream();
    doNothing().when(service).copyContentToResponse(any(), any());
    //when
    service.copyToResponse(book, response);
    //then
    verify(response).setHeader(anyString(), anyString());
    verify(response).getOutputStream();
    verify(service).copyContentToResponse(any(), any());
  }

  @Test
  public void shouldTranformPageToPagedResource() throws Exception {
    //given
    Pageable pageable = new PageRequest(0, 40);
    Page<Book> page = new PageImpl<>(Arrays.asList(book), pageable, 1);
    doReturn(new Link("test")).when(service).getSelfLink(any(Book.class));
    //when
    PagedResources<Resource<Book>> resources = service.toPagedResource(page);
    //then
    assertEquals(resources.getMetadata().getTotalElements(), 1);
    assertEquals(resources.getMetadata().getNumber(), pageable.getPageNumber());
    assertEquals(resources.getMetadata().getSize(), pageable.getPageSize());
    assertTrue(((Resource<Book>)resources.getContent().toArray()[0]).getContent().equals(book));
  }

}
