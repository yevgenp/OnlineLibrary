package com.example.ol.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name="authorities")
public class Authority {

	@Id
    @GeneratedValue
    private Long id;

	@Column
	private String authority;
	
	
	@OneToOne
	@JoinColumn(name="username", nullable=false)
	private User user;
	
	public Authority(){};
	
	public Authority(String auth) {
		this.authority=auth;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	 
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "Authority [authority=" + authority + "]";
	}
}
