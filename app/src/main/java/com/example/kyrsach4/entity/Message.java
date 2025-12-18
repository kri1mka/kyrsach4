package com.example.kyrsach4.entity;

import java.util.Date;

public class Message {
    private int id;
    private int fromUserId;
    private int toUserId;
    private String message;
    private String createdAt;

    private String firstName; // имя отправителя
    private String lastName;  // фамилия отправителя
    private String avatarUrl;

    public Message(int id, int fromUserId, int toUserId, String message,
                   String createdAt, String firstName, String lastName, String avatarUrl) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCreatedAt() {
        return createdAt.toString(); // вывод полной даты и времени
    }

}
