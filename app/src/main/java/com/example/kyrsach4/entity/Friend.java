package com.example.kyrsach4.entity;

public class Friend {
    private int id;
    private String name;      // вместо firstName
    private String surname;   // вместо lastName
    private String country;
    private String avatarUrl; // имя файла, например "1.png"

    // Конструктор
    public Friend(int id, String name, String surname, String country, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.country = country;
        this.avatarUrl = avatarUrl;
    }

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }         // вместо getFirstName()
    public String getSurname() { return surname; }   // вместо getLastName()
    public String getCountry() { return country; }
    public String getAvatarUrl() { return avatarUrl; }

    // Для совместимости с FriendsActivity, можно добавить методы-алиасы:
    public String getFirstName() { return name; }
    public String getLastName() { return surname; }
}
