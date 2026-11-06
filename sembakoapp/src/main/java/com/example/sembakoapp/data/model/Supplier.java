package com.example.sembakoapp.data.model;

import java.io.Serializable;

public class Supplier implements Serializable {
  private static final long serialVersionUID = 1L;

  public long id;
  public String name;
  public String phone;
  public String address;

  public Supplier() {}

  public Supplier(long id, String name, String phone, String address) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.address = address;
  }
}
