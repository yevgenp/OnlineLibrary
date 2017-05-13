package com.example.ol.model.hibernate.dao;

import com.example.ol.model.hibernate.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource
public interface RoleDao extends CrudRepository<Role, Long> {
  @Override
  @RestResource(exported = false)
  <S extends Role> S save(S role);

  @Override
  @RestResource(exported = false)
  void delete(Long roleId);

  Role findByName(String name);
}
