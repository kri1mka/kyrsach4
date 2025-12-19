package com.example.kyrsach4.entities;


public class User {
    public int id;
    public String name;
    public String surname;
    private UserInfo info;

    public User() {}
    public User(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getFullName() {
        return name + " " + surname;
    }


    public UserInfo getInfo() { return info; }
    public void setInfo(UserInfo info) { this.info = info; }
}
