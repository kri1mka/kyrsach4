package org.example.services;

import com.google.gson.Gson;
import org.example.entity.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserService {
    private ConcurrentMap<Integer, User> users;

    private AtomicInteger key;

    public UserService(){
        this.users = new ConcurrentHashMap<>();
        key = new AtomicInteger();

        this.addUser(new User("kristinarekes@gmail.com", "123456", "Krystsina", "Rekesh", "293182328"));
    }

    private boolean createUser(String jsonPayload) {
        if (jsonPayload == null) return false;
        Gson gson = new Gson();
        try{
            User user = (User) gson.fromJson(jsonPayload, User.class);
            if (user != null) {
                return this.addUser(user);
            }
        }catch (Exception e){}
        return false;
    }

    private String toJson(Object list){
        if (list == null) return null;
        Gson gson = new Gson();
        String json = null;
        try{
            json=gson.toJson(list);
        }catch (Exception e){}
        return json;
    }

    private boolean addUser(User user) {
        Integer id=key.incrementAndGet();
        user.setId(id);
        this.users.put(id, user);
        return true;
    }
}
