package com.example.ol.core.security;

import com.example.ol.model.hibernate.dao.UserDao;
import com.example.ol.model.hibernate.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class SecurityHelperTest {
  @Mock private UserDao userDao;
  @Spy @InjectMocks private SecurityHelper helper;
  private User user;

  @Before
  public void setUp() throws Exception {
    user = User.builder().username("user").build();
    doReturn(user).when(userDao).findByUsername("user");
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionWhenNotEnoughArguments() throws Exception {
    //given
    //when
    boolean result = helper.isSameUser("user", null, null);
    //then exception
  }

  @Test
  public void shouldReturnTrueForSameUser() throws Exception {
    //given
    //when
    boolean result = helper.isSameUser("user", "user", 1L);
    //then
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseForOtherUser() throws Exception {
    //given
    user.setId(2L);
    doReturn(user).when(userDao).findByUsername("user");
    //when
    boolean result = helper.isSameUser("user", null, 1L);
    //then
    assertFalse(result);
  }

}
