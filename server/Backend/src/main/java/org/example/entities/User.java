package org.example.entities;

public class User {
    private int id;
    private String name;
    private String surname;
    private UserInfo info;

    // Конструкторы
    public User() {} // default для DAO
    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
    public User(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }


    public UserInfo getInfo() { return info; }
    public void setInfo(UserInfo info) { this.info = info; }
}
