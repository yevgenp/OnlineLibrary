package com.example.ol.core.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LocaleConfigurationTest {
  @Spy LocaleConfiguration configuration;

  @Test
  public void shouldGetMessageSource() throws Exception {
    //given
    //when
    ReloadableResourceBundleMessageSource messageSource = (ReloadableResourceBundleMessageSource)
      configuration.messageSource();
    //then
    assertTrue(messageSource.getBasenameSet().contains("classpath:/locales/locale"));
  }

  @Test
  public void shouldGetLocaleResolver() throws Exception {
    //given
    //when
    LocaleResolver resolver = configuration.localeResolver();
    //then
    assertEquals(resolver.getClass(),  SessionLocaleResolver.class);
  }

  @Test
  public void shouldAddInterceptor() throws Exception {
    //given
    InterceptorRegistry registry = mock(InterceptorRegistry.class);
    //when
    configuration.addInterceptors(registry);
    //then
    verify(registry).addInterceptor(any(LocaleChangeInterceptor.class));
  }

}
