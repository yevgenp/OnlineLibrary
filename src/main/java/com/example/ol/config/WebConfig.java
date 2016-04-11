package com.example.ol.config;

import java.io.IOException;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class WebConfig  extends WebMvcConfigurerAdapter{
	@Bean
	  public ViewResolver viewResolver() {
	    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	    resolver.setPrefix("/WEB-INF/views/");
	    resolver.setSuffix(".jsp");
	    resolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
	    return resolver;
	  }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
	
	@Bean
	public MultipartResolver multipartResolver() throws IOException {
	    return new StandardServletMultipartResolver();
	  }
	
	/*File upload limits configuration
	Can be done in application.properties instead:
	multipart.maxFileSize: 10MB
	multipart.maxRequestSize: 10MB
	*/
	@Bean
	MultipartConfigElement multipartConfigElement() {
	    MultipartConfigFactory factory = new MultipartConfigFactory();
	    factory.setMaxFileSize("10MB");
	    factory.setMaxRequestSize("10MB");
	    return factory.createMultipartConfig();
	}
}
