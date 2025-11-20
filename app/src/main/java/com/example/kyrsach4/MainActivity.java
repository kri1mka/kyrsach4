package com.example.kyrsach4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View c1 = findViewById(R.id.circle1);
        View c2 = findViewById(R.id.circle2);
        View c3 = findViewById(R.id.circle3);
        View c4 = findViewById(R.id.circle4);
        Button btnStart = findViewById(R.id.btnStart);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_scale);

        c1.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade_scale);
        anim.setStartOffset(150);
        c2.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade_scale);
        anim.setStartOffset(300);
        c3.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade_scale);
        anim.setStartOffset(450);
        c4.startAnimation(anim);


    btnStart.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    });

    }

}
