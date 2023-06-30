package com.example.moodmaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moodmaster.feelingScale_MoodShow.FeelingScaleActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        final TextView moodMasterTextView = findViewById(R.id.loadingText);
        final Handler handler = new Handler();

        setAnimation(moodMasterTextView);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, FeelingScaleActivity.class);
                startActivity(intent);
            }
        }, 5000);
    }

    /**
     * Sets a scale animation on the specified TextView.
     *
     * @param textView The TextView to apply the animation to.
     */
    private void setAnimation(TextView textView){
        Animation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        textView.startAnimation(scaleAnimation);
    }
}

