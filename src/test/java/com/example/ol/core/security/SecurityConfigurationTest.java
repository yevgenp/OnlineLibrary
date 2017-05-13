package com.example.ol.core.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SecurityConfigurationTest {

  @Mock private UserDetailsService userDetailsService;
  @Spy @InjectMocks private SecurityConfiguration securityConfiguration;

  private AuthenticationManagerBuilder auth;
  private DaoAuthenticationConfigurer daoAuthConfigurer;

  @Before
  public void setUp() throws Exception {
    auth = mock(AuthenticationManagerBuilder.class);
    daoAuthConfigurer = mock(DaoAuthenticationConfigurer.class);
    doReturn(daoAuthConfigurer).when(auth).userDetailsService(userDetailsService);
  }

  @Test
  public void shouldConfigAuthentication() throws Exception {
    //when
    securityConfiguration.configAuthentication(auth);
    //then
    verify(auth).userDetailsService(userDetailsService);
  }

  @Test
  public void shouldSetPasswordEncoder() throws Exception {
    assertEquals(BCryptPasswordEncoder.class, securityConfiguration.passwordEncoder().getClass());
  }

}
