package com.example.kyrsach4.reqresp;

public class ResetPasswordRequest {
    public String email;
    public String newPassword;

    public ResetPasswordRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }
}
