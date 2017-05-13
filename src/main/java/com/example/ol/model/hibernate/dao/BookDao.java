package com.example.ol.model.hibernate.dao;

import com.example.ol.model.hibernate.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource
public interface BookDao extends PagingAndSortingRepository<Book, Long> {
  @Override
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  <S extends Book> S save(S var1);

  @Modifying
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  @Query(value = "DELETE FROM USERS_BOOKS WHERE BOOKS_ID = ?1", nativeQuery = true)
  void deleteFromUserBooks(Long id);

  @Override
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  void delete(Long id);

  @Override
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  Iterable<Book> findAll();

  @Override
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  Page<Book> findAll(Pageable page);

  @Query("SELECT b FROM Book AS b" +
    " WHERE (:key IS NULL" +
    " OR (lower(b.description) LIKE concat('%', lower(:key), '%'))" +
    " OR (lower(b.title) LIKE concat('%', lower(:key), '%')))" +
    " AND (:author IS NULL OR b.author = :author) " +
    " AND (:genre IS NULL OR b.genre = :genre) ")
  Page<Book> search(@Param("key") String key, @Param("author") String author, @Param("genre") String genre, Pageable pageRequest);

  @Query("SELECT DISTINCT b.author FROM Book AS b" +
    " WHERE :key IS NULL OR (lower(b.author) LIKE concat(lower(:key), '%'))")
  Page<String> getAuthors(@Param("key") String search, Pageable pageRequest);

  @Query("SELECT DISTINCT b.genre FROM Book AS b" +
    " WHERE :key IS NULL OR (lower(b.genre) LIKE concat(lower(:key), '%'))")
  Page<String> getGenres(@Param("key") String search, Pageable pageRequest);

}
