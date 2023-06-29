package com.example.moodmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moodmaster.feelingScale_MoodShow.FeelingScaleActivity;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences emergencyNumber;

    private final String KEY = "number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView currentNr = findViewById(R.id.currentPhoneNumber);


        emergencyNumber = getSharedPreferences("tel", Context.MODE_PRIVATE);
        String nr = emergencyNumber.getString(KEY, "");
        currentNr.setText(nr);

        Button saveBtn = findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.phoneNumberEditText);
                String telNr = editText.getText().toString();
                saveNumber(telNr);
                Intent intent = new Intent(SettingsActivity.this, FeelingScaleActivity.class);
                startActivity(intent);
            }
        });

    }

    private void saveNumber(String number){
        SharedPreferences.Editor editor = emergencyNumber.edit();
        editor.putString(KEY, number);
        System.out.println("NR------------------" + number);
        editor.apply();
    }
}