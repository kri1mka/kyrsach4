package org.example.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TripCard {

    private Integer id;
    private Integer userId;
    private String location;
    private Date startDate;
    private Date endDate;
    private BigDecimal price;
    private String type;
    private String description;
    private Date createdAt;
    private String photoIt;

    public TripCard() {
    }

    public TripCard(Integer userId,
                    String location,
                    Date startDate,
                    Date endDate,
                    BigDecimal price,
                    String type,
                    String description,
                    String photoIt) {
        this.userId = userId;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.type = type;
        this.description = description;
        this.photoIt = photoIt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPhotoIt() {
        return photoIt;
    }

    public void setPhotoIt(String photoIt) {
        this.photoIt = photoIt;
    }

    @Override
    public String toString() {
        return "TripCard{" +
                "id=" + id +
                ", userId=" + userId +
                ", location='" + location + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", photoIt='" + photoIt + '\'' +
                '}';
    }
}
