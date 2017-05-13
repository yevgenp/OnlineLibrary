package com.example.ol.core.authentication;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
 class ExtendedUser extends org.springframework.security.core.userdetails.User {

   ExtendedUser(String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired, boolean accountNonLocked,
                       Collection<? extends GrantedAuthority> authorities) {
     super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
   }

  private Long id;
  private String firstName;
  private String lastName;
 }
