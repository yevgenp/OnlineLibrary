package com.example.ol.core.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerTest {

  @Mock Principal principal;
  @Spy @InjectMocks private AuthenticationController controller;

  @Test
  public void shouldReturnPrincipal() throws Exception {
    assertEquals(principal, controller.user(principal));
  }

  @Test
  public void shouldReturnNoContentAfterLogout() throws Exception {
    //given
    HttpServletRequest request = mock(HttpServletRequest.class);
    assertEquals(controller.logout(request).getStatusCode(), HttpStatus.NO_CONTENT);
  }
}
