package org.example.entities;

public class Photo {
    private int id;
    private String photoUrl;

    // Конструкторы
    public Photo() {} // default для DAO
    public Photo(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public Photo(int id, String photoUrl) {
        this.id = id;
        this.photoUrl = photoUrl;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
