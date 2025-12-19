package com.example.kyrsach4.entities;

import java.util.Date;

public class TripCard {

    private int id;
    private String location;
    private String type;
    private String description;
    private double price;
    private String startDate;  // проще строкой для JSON
    private String endDate;    // иначе нужна Gson конвертация для java.util.Date
    private User user;
    private Photo photo;

    // Конструкторы
    public TripCard() {}

    public TripCard(int id, String location, String type, String description, double price,
                    String startDate, String endDate, User user, Photo photo) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.description = description;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.photo = photo;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Photo getPhoto() { return photo; }
    public void setPhoto(Photo photo) { this.photo = photo; }

    // Удобные методы для доступа к полу и возрасту
    public String getUserSex() {
        return user != null && user.getInfo() != null ? user.getInfo().getSex() : null;
    }

    public Integer getUserAge() {
        return user != null && user.getInfo() != null ? user.getInfo().getAge() : null;
    }
}
