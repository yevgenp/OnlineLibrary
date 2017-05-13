package com.example.ol.registration;

import com.example.ol.model.hibernate.dao.RoleDao;
import com.example.ol.model.hibernate.dao.UserDao;
import com.example.ol.model.hibernate.entity.Role;
import com.example.ol.model.hibernate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class RegistrationService {
  @Autowired private UserDao userDao;
  @Autowired private RoleDao roleDao;
  @Autowired private PasswordEncoder passEncoder;

  boolean isValid(User user, Errors errors) {
    if (errors.hasErrors()) return false;
    String username = user.getUsername();
    if (userDao.findByUsername(username) != null) {
      errors.rejectValue("username", "user.username.alreadyUsed");
      return false;
    }
    return true;
  }

  User register(User user) {
    user.setId(null);
    user.setEnabled(true);
    user.setPassword(prepareDBPassword(user.getPassword()));
    initializeRoles(user);
    return userDao.save(user);
  }

  private String prepareDBPassword(String base64EncodedPassword) {
    if (base64EncodedPassword == null)
      throw new IllegalArgumentException("Invalid password");
    String password = new String(Base64.getDecoder().decode(base64EncodedPassword));
    return passEncoder.encode(password);
  }

  private void initializeRoles(User user) {
    Set<Role> roles = new HashSet<>();
    roles.add(roleDao.findByName("USER"));
    user.setRoles(roles);
  }

  Link getSelfLink(User user) {
    return linkTo(RegistrationController.class).slash(user.getId()).withSelfRel();
  }

}
