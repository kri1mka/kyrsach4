package org.example.entities;


import java.util.Date;

public class TripCard {
    private int id;
    private String location;
    private Date startDate;
    private Date endDate;
    private double price;
    private String type;
    private String description;
    private User user;
    private Photo photo;

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

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

