package com.example.kyrsach4.entity;

public class Message {
    private int id;
    private int fromUserId;
    private int toUserId;
    private String message;
    private String createdAt;

    private String firstName; // имя отправителя
    private String lastName;  // фамилия отправителя
    private String avatarUrl; // имя файла, например "avatar1.png"

    public int getId() { return id; }
    public int getFromUserId() { return fromUserId; }
    public int getToUserId() { return toUserId; }
    public String getMessage() { return message; }
    public String getCreatedAt() { return createdAt; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAvatarUrl() { return avatarUrl; }
}
