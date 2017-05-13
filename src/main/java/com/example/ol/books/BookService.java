package com.example.ol.books;

import com.example.ol.core.exceptions.NoSuchBookException;
import com.example.ol.model.hibernate.dao.BookDao;
import com.example.ol.model.hibernate.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@Service
public class BookService {
  @Autowired private BookDao bookDao;

  public Book save(Book book, MultipartFile file) {
    try {
      book.setFilename(file.getOriginalFilename());
      book.setContent(file.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Could not read file content");
    }
    return bookDao.save(book);
  }

  public Page<Book> search(BookSearch search, Pageable pageable) {
    return bookDao.search(search.getKey(),search.getAuthor(), search.getGenre(), pageable);
  }

  Page<String> getAuthors(String search, Pageable pageable) {
    return bookDao.getAuthors(search, pageable);
  }

  Page<String> getGenres(String search, Pageable pageable) {
    return bookDao.getGenres(search, pageable);
  }

  Book getBookOrExit(Long id) {
    Book book = bookDao.findOne(id);
    if (book == null) throw new NoSuchBookException();
    return book;
  }

  void copyToResponse(Book book, HttpServletResponse response) {
    response.setHeader("Content-Disposition", "attachment;filename=\"" +
      book.getFilename() + "\"");
    try (OutputStream out = response.getOutputStream() ){
      copyContentToResponse(book.getContent(), out);
      out.flush();
      out.close();
    } catch (IOException e) {
      log.error("Could not copy book file to response");
      e.printStackTrace();
    }
  }

  void copyContentToResponse(byte[] content, OutputStream out) throws IOException {
    IOUtils.copy(new ByteArrayInputStream(content), out);
  }

  <T extends Book> PagedResources toPagedResource(Page<T> page) {
    List<Resource<T>> books = new ArrayList<>();
    page.getContent().forEach( book -> {
      Resource<T> resource = new Resource<>(book);
      resource.add(getSelfLink(book));
      books.add(resource);
    });
    PagedResources.PageMetadata metadata = new PagedResources.PageMetadata(
      page.getSize(), page.getNumber(), page.getTotalElements());
    return new PagedResources<>(books, metadata);
  }

  Link getSelfLink(Book book) {
    return linkTo(BookController.class).slash(book.getId()).withSelfRel();
  }

}
