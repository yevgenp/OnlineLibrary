package com.example.ol.model.hibernate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(generator="optimized-sequence")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(unique = true, nullable = false)
  private String name;
}
