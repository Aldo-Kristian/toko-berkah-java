package com.example.sembakoapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("categories")
public class Category {
  @Id public Long id;
  public String name;
  public String description;
}
