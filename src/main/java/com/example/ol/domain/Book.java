package com.example.ol.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.validator.constraints.NotEmpty;

@Entity(name="books")
public class Book {
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String author;
	
	@Column
	private String genre;
	
	@Column
	@NotEmpty(message="Title/File must not be empty.")
	private String title;
	
	@Column
	private String description;
	
	@Column
	private String filename;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	private byte[] content;
	
	
	public Book(){}
	
	public Book(Book b){
		this.id = b.getId();
		this.author = b.getAuthor();
		this.genre = b.getGenre();
		this.title = b.getTitle();
		this.description = b.getDescription();
		this.filename = b.getFilename();
		this.content = b.getContent();
	}
	
	public Book(String author, String genre, String title){
		this.author = author;
		this.genre = genre;
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	};
	
	
	@Override
	public String toString() {
		return "Book [id=" + id + ", author=" + author + ", genre=" + genre + ", title=" + title + ", description="
				+ description + ", filename=" + filename + "]";
	}

}
