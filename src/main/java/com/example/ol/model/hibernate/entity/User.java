package com.example.ol.model.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
  @Id
  @JsonProperty(access = READ_ONLY)
  @GeneratedValue(generator="optimized-sequence")
  @Column(unique = true, nullable = false)
  private Long id;

  @NotEmpty @Pattern(regexp = "\\w+")
  @Column(unique = true)
  private String username;

  @Column(nullable = false, columnDefinition = "boolean default false")
  private boolean enabled;

  @NotEmpty
  @JsonProperty(access = WRITE_ONLY)
  private String password;

  @Email
  @NotEmpty
  private String email;

  private String firstName;
  private String lastName;

  @ManyToMany
  private Set<Role> roles;

  @ManyToMany
  private Set<Book> books;
}
