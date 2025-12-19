package com.example.kyrsach4.api;

import com.example.kyrsach4.entity.Message;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatApi {
    @GET("get_messages.php")
    Call<List<Message>> getMessages(
            @Query("user1") int user1,
            @Query("user2") int user2
    );

    @GET("send_message.php")
    Call<Void> sendMessage(
            @Query("fromUser") int fromUser,
            @Query("toUser") int toUser,
            @Query("message") String message
    );
}
