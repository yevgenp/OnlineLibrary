package com.example.ol.model.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
  @Id
  @GeneratedValue(generator="optimized-sequence")
  @Column(unique = true, nullable = false)
  private Long id;

  @NotEmpty
  private String author;

  @NotEmpty
  private String genre;

  @NotEmpty
  private String title;

  @Column(length = 10000)
  private String description;

  private String filename;

  @JsonIgnore
  @Lob @Basic(fetch=FetchType.LAZY)
  private byte[] content;
}
