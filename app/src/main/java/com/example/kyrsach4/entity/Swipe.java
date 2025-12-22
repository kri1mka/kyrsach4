package com.example.kyrsach4.entity;



public class Swipe {
    private int id;
    private int swiperId;   // кто свайпнул
    private int swipedId;   // на кого свайпнули
    private String direction; // "left" или "right"

    public Swipe() {}

    public Swipe(int swiperId, int swipedId, String direction) {
        this.swiperId = swiperId;
        this.swipedId = swipedId;
        this.direction = direction;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSwiperId() { return swiperId; }
    public void setSwiperId(int swiperId) { this.swiperId = swiperId; }

    public int getSwipedId() { return swipedId; }
    public void setSwipedId(int swipedId) { this.swipedId = swipedId; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}
