package com.example.moodmaster.feelingScale_MoodShow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moodmaster.R;

public class Overview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Button mood_button = findViewById(R.id.button_moodshow);
        Button walk_button = findViewById(R.id.button_walk);

        mood_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Overview.this, Mood_Show.class );
                startActivity(intent);
            }
        });

        walk_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Overview.this, Walk.class);
                startActivity(intent);
            }
        });

    }
}