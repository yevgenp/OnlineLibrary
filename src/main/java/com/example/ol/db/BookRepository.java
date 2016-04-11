package com.example.ol.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ol.domain.Book;

public interface BookRepository extends JpaRepository<Book,Long>, BookRepositoryCustom{

	/*Generate exception on implementation
	List<Book> findAllOrderByTitle();
	*/
}
