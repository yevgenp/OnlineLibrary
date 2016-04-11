package com.example.ol.db;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.ol.domain.Book;

@Repository
@Transactional
public class BookRepositoryImpl implements BookRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Book> getBooks() {
		
		return (List<Book>) em.createQuery("SELECT b FROM books b ORDER BY b.title ASC")
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Book> getBooks(String a, String g, String t) {
		
		return (List<Book>) em.createQuery("SELECT b FROM books b "
				+ "WHERE b.author LIKE :a "
				+ "AND b.genre LIKE :g "
				+ "AND b.title LIKE :t "
				+ "ORDER BY b.title ASC")
				.setParameter("a", "%"+a+"%" )
				.setParameter("g", "%"+g+"%")
				.setParameter("t", "%"+t+"%")
				.getResultList();
	}
	
	

}
