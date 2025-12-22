package com.example.kyrsach4.entity;

public class Message {
    private int id;
    private int fromUserId;
    private int toUserId;
    private String message;
    private String createdAt;

    private String name;     // имя отправителя
    private String surname;  // фамилия отправителя
    private String avatarUrl;

    public Message(int id, int fromUserId, int toUserId, String message,
                   String createdAt, String name, String surname, String avatarUrl) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.message = message;
        this.createdAt = createdAt;
        this.name = name;
        this.surname = surname;
        this.avatarUrl = avatarUrl;
    }

    // Геттеры и сеттеры
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFromUserId() { return fromUserId; }
    public void setFromUserId(int fromUserId) { this.fromUserId = fromUserId; }

    public int getToUserId() { return toUserId; }
    public void setToUserId(int toUserId) { this.toUserId = toUserId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getCreatedAt() {
        return createdAt; // уже строка
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}