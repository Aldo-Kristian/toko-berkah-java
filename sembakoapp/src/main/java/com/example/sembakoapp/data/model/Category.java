package com.example.sembakoapp.data.model;

import java.io.Serializable;

public class Category implements Serializable {
  private static final long serialVersionUID = 1L;

  public long id;
  public String name;
  public String description;

  public Category() {}

  public Category(long id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }
}
