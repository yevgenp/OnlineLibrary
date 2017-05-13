package com.example.ol.model.hibernate.dao;

import org.aspectj.lang.JoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryAspectTest {
  @Mock private BookDao bookDao;
  @Spy @InjectMocks private RepositoryAspect aspect;

  @Test
  public void shouldRemoveBookFromAllFavoritesBeforeBookDeletion() throws Exception {
    //given
    JoinPoint jp = mock(JoinPoint.class);
    Long[] args = {3L};
    doReturn(args).when(jp).getArgs();
    //when
    aspect.beforeBookDelete(jp);
    //then
    verify(bookDao).deleteFromUserBooks(3L);
  }

}
