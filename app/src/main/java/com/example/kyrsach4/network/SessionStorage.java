package com.example.kyrsach4.network;

public class SessionStorage {
    // сохраняем JSESSIONID
    public static String sessionId = null;

    public static boolean isLoggedIn() {
        return sessionId != null;
    }
}
