package com.example.ol.registration;

import com.example.ol.model.hibernate.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {
  @Mock private RegistrationService service;
  @Mock private Errors errors;
  @Spy @InjectMocks private RegistrationController controller;
  private User user;

  @Before
  public void setUp() throws Exception {
    user = new User();
    doReturn(new Link("test")).when(service).getSelfLink(any(User.class));
  }

  @Test
  public void shouldRegisterUser() throws Exception {
    //given
    doReturn(true).when(service).isValid(user, errors);
    doReturn(user).when(service).register(user);
    //when
    ResponseEntity<?> result = controller.registerUser(user, errors);
    //then
    verify(service).isValid(user, errors);
    verify(service).register(user);
    assertEquals(((Resource<User>)result.getBody()).getContent(), user);
  }

  @Test
  public void shouldReturnErrors() throws Exception {
    //given
    doReturn(false).when(service).isValid(user, errors);
    //when
    ResponseEntity<?> result = controller.registerUser(user, errors);
    //then
    verify(service).isValid(user, errors);
    verify(service, never()).register(user);
    assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
  }

}
