package com.example.ol.model.hibernate.dao;

import com.example.ol.core.security.annotations.SameUserOrAdminAccess;
import com.example.ol.model.hibernate.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource
public interface UserDao extends PagingAndSortingRepository<User, Long> {
  @RestResource(exported = false)
  User findByUsername(String username);

  @Override
  @SameUserOrAdminAccess
  User findOne(@Param("id") Long id);

  @Override
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  Iterable<User> findAll();

  @Override
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  Iterable<User> findAll(Sort sort);

  @Override
  @PreAuthorize("hasAuthority('ADMINISTRATOR')")
  Page<User> findAll(Pageable page);

  @Override
  <S extends User> S save(S var1);

  @Override
  @RestResource(exported = false)
  void delete(Long id);

}
