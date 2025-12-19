package com.example.kyrsach4.entity;

public class Friend {
    private int id;
    private String firstName;
    private String lastName;
    private String country;
    private String avatarUrl; // имя файла, например "1.png"

    // Геттеры
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCountry() { return country; }
    public String getAvatarUrl() { return avatarUrl; }
}

