package org.example.entity;

public class Friend {
    private int id;
    private String name;       // имя
    private String surname;    // фамилия
    private String country;
    private String avatarUrl;

    public Friend(int id, String name, String surname, String country, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.avatarUrl = avatarUrl;
    }

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getCountry() { return country; }
    public String getAvatarUrl() { return avatarUrl; }
}
