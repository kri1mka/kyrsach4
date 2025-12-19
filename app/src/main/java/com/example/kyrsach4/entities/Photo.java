package com.example.kyrsach4.entities;


public class Photo {
    public int id;
    public String photoUrl;

    public Photo() {}
    public Photo(int id, String photoUrl) {
        this.id = id;
        this.photoUrl = photoUrl;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
