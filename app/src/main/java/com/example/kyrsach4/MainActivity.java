package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View c1 = findViewById(R.id.circle1);
        View c2 = findViewById(R.id.circle2);
        View c3 = findViewById(R.id.circle3);
        View c4 = findViewById(R.id.circle4);
        View c5 = findViewById(R.id.circle5);
        View c6 = findViewById(R.id.circle6);
        View c7 = findViewById(R.id.circle7);
        View c8 = findViewById(R.id.circle8);
        Button btnStart = findViewById(R.id.btnStart);

        c1.post(() -> swimLeftToRight(c1));
        c2.post(() -> swimLeftToRight(c2));
        c3.post(() -> swimLeftToRight(c3));
        c4.post(() -> swimLeftToRight(c4));
        c5.post(() -> swimLeftToRight(c5));
        c6.post(() -> swimLeftToRight(c6));
        c7.post(() -> swimLeftToRight(c7));
        c8.post(() -> swimLeftToRight(c8));


        btnStart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    });

    }

    private void applyRandomSize(View view) {
        int min = 100;
        int max = 300;

        int size = min + (int) (Math.random() * (max - min));

        view.getLayoutParams().width = size;
        view.getLayoutParams().height = size;
        view.requestLayout();
    }


    private long randomSpeed() {
        return (long) (5000 + Math.random() * 5000);
    }

    private void addFloatingEffect(View view) {
        float wiggle = (float) (Math.random() * 40 - 20);

        view.animate()
                .translationYBy(wiggle)
                .setDuration(1200)
                .withEndAction(() -> addFloatingEffect(view))
                .start();
    }

    private void swimLeftToRight(View view) {

        applyRandomSize(view);

        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        float randomY = -400 + (float) (Math.random() * 800);
        long speed = randomSpeed();

        view.setVisibility(View.INVISIBLE);
        view.setAlpha(0f);

        view.setTranslationX(-300f);
        view.setTranslationY(randomY);

        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(600).start();

        view.animate()
                .translationX(screenWidth + 300)
                .setDuration(speed)
                .withEndAction(() -> swimLeftToRight(view))
                .start();

        addFloatingEffect(view);
    }
}