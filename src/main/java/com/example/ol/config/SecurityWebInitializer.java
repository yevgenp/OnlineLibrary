package com.example.ol.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

//Intercept security requests, used instead of configuring  DelegatingFilterProxy in web.xml
public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {

}
