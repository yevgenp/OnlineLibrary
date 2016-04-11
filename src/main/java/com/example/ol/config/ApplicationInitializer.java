package com.example.ol.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/*
 * Spring DispatcherServlet configuration (file upload)
 * Unnecessary for Spring Boot (see MultipartConfigElement bean in WebConfig) 
 **/
public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
    @Override
    protected void customizeRegistration(Dynamic registration) {
      registration.setMultipartConfig(
          new MultipartConfigElement("", 10485760, 10485760, 10485760));
    }

}