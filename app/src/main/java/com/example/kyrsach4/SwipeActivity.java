package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kyrsach4.Adapter.CardAdapter;
import com.example.kyrsach4.api.ApiClient;
import com.example.kyrsach4.api.ApiService;
import com.example.kyrsach4.entities.TripCard;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwipeActivity extends AppCompatActivity {

    private CardStackView cardStackView;
    private CardStackLayoutManager manager;
    private CardAdapter adapter;
    private List<TripCard> cards = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        cardStackView = findViewById(R.id.card_stack);

        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardSwiped(Direction direction) {
                int position = manager.getTopPosition() - 1;
                if (position < 0 || position >= cards.size()) return;

                TripCard card = cards.get(position);
                if (direction == Direction.Right) sendSwipe(card.getId(), true);
                else if (direction == Direction.Left) sendSwipe(card.getId(), false);
            }

            @Override public void onCardDragging(Direction direction, float ratio) {}
            @Override public void onCardRewound() {}
            @Override public void onCardCanceled() {}
            @Override public void onCardAppeared(View view, int position) {}
            @Override public void onCardDisappeared(View view, int position) {}
        });

        cardStackView.setLayoutManager(manager);

        adapter = new CardAdapter(cards, this);
        cardStackView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        findViewById(R.id.btnLike).setOnClickListener(v -> swipeRight());
        findViewById(R.id.btnDislike).setOnClickListener(v -> swipeLeft());

        ImageView btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(SwipeActivity.this, FiltersActivity.class);
            startActivityForResult(intent, 100);
        });

        loadCardsFromServer();
    }

    private void swipeRight() {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right).build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    private void swipeLeft() {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left).build();
        manager.setSwipeAnimationSetting(setting);
        cardStackView.swipe();
    }

    private void loadCardsFromServer() {
        apiService.getTripCards().enqueue(new Callback<List<TripCard>>() {
            @Override
            public void onResponse(Call<List<TripCard>> call, Response<List<TripCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cards.clear();
                    cards.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SwipeActivity.this, "Ошибка загрузки карточек", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TripCard>> call, Throwable t) {
                Toast.makeText(SwipeActivity.this, "Ошибка сервера: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendSwipe(int cardId, boolean liked) {
        apiService.sendSwipe(cardId, liked).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {}
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SwipeActivity.this, "Не удалось отправить свайп", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String gender = data.getStringExtra("gender");
            String country = data.getStringExtra("country");
            String direction = data.getStringExtra("direction");
            String tripType = data.getStringExtra("tripType");
            int ageFrom = data.getIntExtra("ageFrom", -1);
            int ageTo = data.getIntExtra("ageTo", -1);

            filterCards(gender, country, direction, tripType, ageFrom, ageTo);
        }
    }

    private void filterCards(String gender, String country, String direction, String tripType, int ageFrom, int ageTo) {
        List<TripCard> filtered = new ArrayList<>();
        for (TripCard card : cards) {
            boolean match = true;

            // Фильтры по стране, типу поездки и направлению
            if (!country.equals("Любая") && !card.getLocation().equals(country)) match = false;
            if (!direction.equals("Любое") && !card.getType().equals(direction)) match = false;
            if (!tripType.equals("Любой") && !card.getType().equals(tripType)) match = false;

            // Фильтр по полу
            if (!gender.isEmpty() && card.getUser() != null && card.getUser().getInfo() != null) {
                String userGender = card.getUser().getInfo().getSex();
                if (!userGender.equalsIgnoreCase(gender)) match = false;
            }

// Фильтр по возрасту
            if (card.getUser() != null && card.getUser().getInfo() != null) {
                int userAge = card.getUser().getInfo().getAge();
                if (ageFrom != -1 && userAge < ageFrom) match = false;
                if (ageTo != -1 && userAge > ageTo) match = false;
            }



            if (match) filtered.add(card);
        }
        adapter.updateList(filtered);
    }
}
