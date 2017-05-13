package com.example.ol.core.configuration;

import com.example.ol.model.hibernate.entity.Book;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
@EnableAspectJAutoProxy
public class RepositoryConfiguration extends RepositoryRestConfigurerAdapter {
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.exposeIdsFor(Book.class);
  }
}
