package com.example.kyrsach4.network;

public class SessionStorage {

    // JSESSIONID для сервера
    public static String sessionId = null;

    // ID пользователя для приложения
    public static Integer userId = null;

    public static boolean isLoggedIn() {
        return sessionId != null && userId != null;
    }

    public static void clear() {
        sessionId = null;
        userId = null;
    }
}
