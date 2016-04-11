package com.example.ol.db;

import java.util.List;

import com.example.ol.domain.Book;

public interface BookRepositoryCustom {
	
	List<Book> getBooks();
	List<Book> getBooks(String author, String genre, String title);
}
