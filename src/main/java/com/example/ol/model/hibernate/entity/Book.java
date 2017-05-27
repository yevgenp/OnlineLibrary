package com.example.ol.model.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;


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

  @NotEmpty @Size(max = 254)
  private String author;

  @NotEmpty @Size(max = 254)
  private String genre;

  @NotEmpty @Size(max = 254)
  private String title;

  @Size(max = 9999)
  @Column(length = 10000)
  private String description;

  private String filename;

  @JsonIgnore
  @Lob @Basic(fetch=FetchType.LAZY)
  private byte[] content;
}
