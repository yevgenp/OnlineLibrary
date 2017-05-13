package com.example.ol.registration;

import com.example.ol.model.hibernate.dao.RoleDao;
import com.example.ol.model.hibernate.dao.UserDao;
import com.example.ol.model.hibernate.entity.Role;
import com.example.ol.model.hibernate.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceTest {
  @Mock private UserDao userDao;
  @Mock private RoleDao roleDao;
  @Mock private PasswordEncoder passEncoder;
  @Mock private Errors errors;
  @Spy @InjectMocks private RegistrationService service;
  private User user;

  @Test
  public void shouldReturnTrueWhenValid() throws Exception {
    //given
    User user = User.builder().username("user").build();
    doReturn(false).when(errors).hasErrors();
    //when
    boolean result = service.isValid(user, errors);
    //then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseWhenHasErrors() throws Exception {
    //given
    doReturn(true).when(errors).hasErrors();
    //when
    boolean result = service.isValid(user, errors);
    //then
    assertFalse(result);
  }

  @Test
  public void shouldReturnFalseWhenUsernameIsAlreadyRegistered() throws Exception {
    //given
    doReturn(false).when(errors).hasErrors();
    User user = User.builder().username("user").build();
    doReturn(new User()).when(userDao).findByUsername("user");
    //when
    boolean result = service.isValid(user, errors);
    //then
    verify(errors).rejectValue("username", "user.username.alreadyUsed");
    assertFalse(result);
  }

  @Test
  public void shouldRegister() throws Exception {
    //given
    user = mock(User.class);
    doReturn("password").when(user).getPassword();
    doReturn("encPassword").when(passEncoder).encode(any());
    Role role = Role.builder().id(1L).name("USER").build();
    doReturn(role).when(roleDao).findByName("USER");
    //when
    service.register(user);
    //then
    verify(user).setId(null);
    verify(user).setEnabled(true);
    verify(user).setPassword("encPassword");
    verify(passEncoder).encode(any());
    verify(user).setRoles(anySetOf(Role.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWithNullPassword() throws Exception {
    //given
    //when
    service.register(new User());
    //then exception
  }

}
