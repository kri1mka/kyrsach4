package org.example.entity;

import java.util.Date;

public class Messages {

    private Integer id;
    private Integer from_user_id;
    private Integer to_user_id;
    private String message;
    private Date created_at;

    public Messages() {}

    public Messages(Integer from_user_id, Integer to_user_id, String message) {
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(Integer from_user_id) {
        this.from_user_id = from_user_id;
    }

    public Integer getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(Integer to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
