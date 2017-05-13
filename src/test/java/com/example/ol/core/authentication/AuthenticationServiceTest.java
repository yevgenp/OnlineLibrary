package com.example.ol.core.authentication;

import com.example.ol.model.hibernate.dao.UserDao;
import com.example.ol.model.hibernate.entity.Role;
import com.example.ol.model.hibernate.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {
  @Mock private UserDao userDao;
  @Spy @InjectMocks private AuthenticationService service;
  private User user;

  @Before
  public void setUp() {
    Set<Role> roles = new HashSet<>(Arrays.asList(new Role(1L, "USER")));
    user = User.builder().id(1L).enabled(true).username("user").password("")
      .firstName("first").lastName("last").roles(roles).build();
    doReturn(user).when(userDao).findByUsername(user.getUsername());
  }

  @Test
  public void shouldLoadUserByUsername() throws Exception {
    //when
    service.loadUserByUsername("user");
    //then
    verify(userDao).findByUsername("user");
    verify(service).getUserAuthorities(user);
    verify(service).buildUserForAuthentication(any(), any());
  }

  @Test
  public void shouldBuildUserForAuthentication() throws Exception {
    //given
    List<GrantedAuthority> authorities = service.getUserAuthorities(user);
    //when
    ExtendedUser exUser = service.buildUserForAuthentication(user, authorities);
    //then
    assertEquals(exUser.getId(), user.getId());
    assertEquals(exUser.getFirstName(), user.getFirstName());
    assertEquals(exUser.getUsername(), user.getUsername());
    assertEquals(exUser.getPassword(), user.getPassword());
    assertEquals(exUser.isEnabled(), user.isEnabled());
  }

  @Test
  public void shouldGetUserAuthorities() throws Exception {
    //given
    //when
    List<GrantedAuthority> authorities = service.getUserAuthorities(user);
    //then
    assertEquals(authorities.get(0).getAuthority(), "USER");
  }

}
