package com.example.kyrsach4.reqresp;

public class RegisterRequest {
    public String name;
    public String surname;
    public String email;
    public String password;
    public String sex;
    public String birthdate; // yyyy-MM-dd
    public String about;

    public RegisterRequest(String name, String surname, String email, String password, String sex, String birthdate, String about ){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.birthdate = birthdate;
        this.about = about;

    }
}
