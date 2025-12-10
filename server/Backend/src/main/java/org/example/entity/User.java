package org.example.entity;

public class User {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone_number;

    public User() {
    }

    public User(String email, String password, String name, String surname, String phone_number) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone_number = phone_number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

}
