package com.example.moodmaster.feelingScale_MoodShow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moodmaster.R;

public class BreathingActivity extends AppCompatActivity {

    private static final long ANIMATION_DURATION = 4000;
    private View blueCircle;
    private final String breatheIn = "BREATHE IN";
    private final String breatheOut = "BREATHE OUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);

        Button stopButton = findViewById(R.id.stopButton);

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( BreathingActivity.this, Tabs.class);
                startActivity(intent);
            }
        });

        blueCircle = findViewById(R.id.blueCircle);

        animateCircle();
    }

    private void animateCircle() {
        AnimatorSet animatorSet = new AnimatorSet();

        TextView breathingTV = findViewById(R.id.tv_breathing);

        ObjectAnimator growAnimator = ObjectAnimator.ofPropertyValuesHolder(
                blueCircle,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 3f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 3f)
        );

        growAnimator.setRepeatMode(ValueAnimator.REVERSE); // Reverse the animation
        growAnimator.setDuration(ANIMATION_DURATION);

        ObjectAnimator shrinkAnimator = ObjectAnimator.ofPropertyValuesHolder(
                blueCircle,
                PropertyValuesHolder.ofFloat("scaleX", 3f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 3f, 1f)
        );

        shrinkAnimator.setRepeatMode(ValueAnimator.REVERSE); // Reverse the animation
        shrinkAnimator.setDuration(ANIMATION_DURATION);

        animatorSet.playSequentially(growAnimator, shrinkAnimator);

        ValueAnimator textAnimator = ValueAnimator.ofInt(0, 1);
        textAnimator.setDuration(ANIMATION_DURATION);
        textAnimator.setInterpolator(new DecelerateInterpolator());
        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value == 0) {
                    breathingTV.setText(breatheIn);
                } else {
                    breathingTV.setText(breatheOut);
                }
            }
        });

        AnimatorSet synchronizedAnimatorSet = new AnimatorSet();
        synchronizedAnimatorSet.playTogether(animatorSet, textAnimator);

        synchronizedAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animateCircle();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        synchronizedAnimatorSet.start();
    }
}
