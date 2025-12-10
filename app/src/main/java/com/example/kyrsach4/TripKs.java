package com.example.kyrsach4;

public class TripKs {
    String place;       // Место поездки
    String dates;       // Даты
    String price;       // Стоимость
    String type;        // Тип отдыха
    String description; // Описание

    public TripKs(String place, String dates, String price, String type, String description) {
        this.place = place;
        this.dates = dates;
        this.price = price;
        this.type = type;
        this.description = description;
    }
}
