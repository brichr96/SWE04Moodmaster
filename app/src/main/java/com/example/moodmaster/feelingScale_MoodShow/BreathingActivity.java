package com.example.moodmaster.feelingScale_MoodShow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moodmaster.R;
import com.example.moodmaster.fragments.LightTabFragment;

public class BreathingActivity extends AppCompatActivity {

    private static final long ANIMATION_DURATION = 5000; // Animation duration in milliseconds

    private View blueCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);

        Button stopButton = findViewById(R.id.stopButton);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( BreathingActivity.this, moods_tabbed.class);
                startActivity(intent);
            }
        });

        blueCircle = findViewById(R.id.blueCircle);

        // Start the animation
        animateCircle();
    }

    private void animateCircle() {
        // Create an AnimatorSet to hold the growing and shrinking animations
        AnimatorSet animatorSet = new AnimatorSet();

        // Create the growing animation
        ObjectAnimator growAnimator = ObjectAnimator.ofPropertyValuesHolder(
                blueCircle,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 2f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 2f)
        );
        //growAnimator.setRepeatCount(1); // Repeat once
        growAnimator.setRepeatMode(ValueAnimator.REVERSE); // Reverse the animation
        growAnimator.setDuration(ANIMATION_DURATION);

        // Create the shrinking animation
        ObjectAnimator shrinkAnimator = ObjectAnimator.ofPropertyValuesHolder(
                blueCircle,
                PropertyValuesHolder.ofFloat("scaleX", 2f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 2f, 1f)
        );
        //shrinkAnimator.setRepeatCount(1); // Repeat once
        shrinkAnimator.setRepeatMode(ValueAnimator.REVERSE); // Reverse the animation
        shrinkAnimator.setDuration(ANIMATION_DURATION);

        // Add the animations to the AnimatorSet
        animatorSet.playSequentially(growAnimator, shrinkAnimator);

        // Set up a listener to restart the animation when it ends
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Animation ended, restart the animation
                animateCircle();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Animation cancelled
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Animation repeated
            }
        });

        // Start the AnimatorSet
        animatorSet.start();
    }

}
