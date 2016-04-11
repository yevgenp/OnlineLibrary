package com.example.ol.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
  
  @Override
  protected void configure(HttpSecurity http) throws Exception {
	  http
      .formLogin()
        .loginPage("/login")
      .and()
        .logout()
        	//Enable GET requests for logout to work with CSRF enabled
        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
        		.logoutSuccessUrl("/")
      .and()
      .rememberMe()
        .tokenRepository(new InMemoryTokenRepositoryImpl())
        .tokenValiditySeconds(600)
        .key("olKey")
      .and()
      .authorizeRequests()
        .antMatchers("/home", "/register", "/book/**").permitAll()
      	.antMatchers("/download/**", "/users/**", "/favorites/**").authenticated()
        .antMatchers("/add**", "/edit/**", "/delete/**").hasRole("ADMIN")
        .anyRequest().permitAll();
  }
  
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	  auth.jdbcAuthentication().dataSource(dataSource);
  }

  
}
