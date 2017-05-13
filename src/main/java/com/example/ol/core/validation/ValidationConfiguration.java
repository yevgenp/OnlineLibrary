package com.example.ol.core.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ValidationConfiguration extends WebMvcConfigurerAdapter {
  @Autowired MessageSource messageSource;

  @Bean
  public LocalValidatorFactoryBean validator()
  {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }

  @Override
  public Validator getValidator()
  {
    return validator();
  }
}
