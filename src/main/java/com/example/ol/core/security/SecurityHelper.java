package com.example.ol.core.security;


import com.example.ol.model.hibernate.dao.UserDao;
import com.example.ol.model.hibernate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class SecurityHelper {
	@Autowired private UserDao userDao;

	public boolean isSameUser(@NotNull String principalUsername, String username, Long id) {
		if (principalUsername == null || (id == null && username == null))
			throw new IllegalArgumentException("Username/ID is NULL. Incorrect annotation use.");
		User user = userDao.findByUsername(principalUsername);
    return principalUsername.equals(username)
      || user.getId().equals(id);
	}
}
