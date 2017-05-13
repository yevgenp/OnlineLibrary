package com.example.ol.core.authentication;

import com.example.ol.model.hibernate.dao.UserDao;
import com.example.ol.model.hibernate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements UserDetailsService {

  @Autowired private UserDao userDao;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    User user = userDao.findByUsername(username);
    List<GrantedAuthority> authorities = getUserAuthorities(user);
    return buildUserForAuthentication(user, authorities);
  }

  ExtendedUser buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
     ExtendedUser exUser = new ExtendedUser(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities);
     exUser.setId(user.getId());
     exUser.setFirstName(user.getFirstName());
     exUser.setLastName(user.getLastName());
     return exUser;
  }

  List<GrantedAuthority> getUserAuthorities(User user) {
    if (user != null) {
      return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
    } else return new ArrayList<>();

  }
}
