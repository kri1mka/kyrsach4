package org.example.entity;

public class Friend {
    private int id;
    private String firstName;
    private String lastName;
    private String country;
    private String avatarUrl;

    public Friend(int id, String firstName, String lastName, String country, String avatarUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.avatarUrl = avatarUrl;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCountry() { return country; }
    public String getAvatarUrl() { return avatarUrl; }
}
