package org.example.entity;

import java.util.Date;

public class TripCard {
    private Integer id;
    private Integer user_id;
    private String location;
    private Date start_date;
    private Date end_date;
    private Double price;
    private String type;
    private String description;
    private Date created_at;
    private String photo_it;

    public TripCard() {}

    public TripCard(Integer user_id, String location, Date start_date, Date end_date, Double price,
                    String type, String description, String photo_it) {
        this.user_id = user_id;
        this.location = location;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
        this.type = type;
        this.description = description;
        this.photo_it = photo_it;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getPhoto_it() {
        return photo_it;
    }

    public void setPhoto_it(String photo_it) {
        this.photo_it = photo_it;
    }
}
