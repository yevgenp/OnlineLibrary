package com.example.ol.core.configuration;

import com.example.ol.model.hibernate.entity.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryConfigurationTest {
  @Spy private RepositoryConfiguration configuration;

  @Test
  public void shouldConfigureRepositoryRestConfiguration() throws Exception {
    //given
    RepositoryRestConfiguration config = mock(RepositoryRestConfiguration.class);
    //when
    configuration.configureRepositoryRestConfiguration(config);
    //then
    verify(config).exposeIdsFor(Book.class);
  }

}
