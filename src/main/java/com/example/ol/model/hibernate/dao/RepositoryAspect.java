package com.example.ol.model.hibernate.dao;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryAspect {
  @Autowired private BookDao bookDao;

  @Before("execution(* com.example.ol.model.hibernate.dao.BookDao.delete(Long))")
  public void beforeBookDelete(JoinPoint joinPoint) {
    Long bookId = (Long)joinPoint.getArgs()[0];
    bookDao.deleteFromUserBooks(bookId);
  }

}
