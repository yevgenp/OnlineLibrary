package com.example.ol.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ol.domain.User;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
	
	User getUserByUsername(String username);
}
