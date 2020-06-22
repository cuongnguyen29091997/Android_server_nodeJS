package com.example.cs_k40.model;

public class User extends  BaseModel {
    private  int id;
    private  String email;
    private String fullname;
    private  String address;
    private  String token;
    private  String password;

    public User(String em, String fn, String ad, String pass) {
        email = em;
        fullname = fn;
        password = pass;
        address = ad;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullName) {
        this.fullname = fullName;
    }
}
