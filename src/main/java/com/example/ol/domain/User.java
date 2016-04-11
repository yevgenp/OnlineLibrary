package com.example.ol.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity(name="users")
public class User{
	
	@Id
	@NotEmpty(message="Login must not be empty.")
	private String username;
	
	@Column
	@NotEmpty(message="Password must not be empty.")
	private String password;
	
	@Column
	private boolean enabled;
	
	@Column
	private String fullname;
	
	@Column
	@Email
	private String email;

	@OneToOne(mappedBy="user", cascade=CascadeType.ALL)
	private Authority authority;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Book> books = new HashSet<Book>();
	
	public User(){}
	
	public User(String name, String pass){
		
		this.username = name;
		this.password = pass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	Authority getAuthority(){
		return this.authority;
	};
	
	public void setAuthority(Authority auth){
		this.authority = auth;
	};

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", authority=" + authority + "]";
	}
}
