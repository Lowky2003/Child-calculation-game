package com.example.asm1;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.Random;

public class CompareActivity extends AppCompatActivity {
    private int num1, num2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        generateNewQuestion();

        // Set up click listeners
        findViewById(R.id.btnGreater).setOnClickListener(v -> checkAnswer(true));
        findViewById(R.id.btnLesser).setOnClickListener(v -> checkAnswer(false));
    }

    private void generateNewQuestion() {
        Random rand = new Random();
        num1 = rand.nextInt(999) + 1;
        num2 = rand.nextInt(999) + 1;

        TextView tvQuestion = findViewById(R.id.tvQuestion);
        tvQuestion.setText(num1 + "   vs   " + num2);
    }

    private void checkAnswer(boolean isGreater) {
        boolean correct = (isGreater && num1 > num2) || (!isGreater && num1 < num2);

        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setText(correct ? "Correct! 🎉" : "Try Again! 💪");

        // Set text color
        int colorRes = correct ? R.color.green : R.color.red;
        tvResult.setTextColor(ContextCompat.getColor(this, colorRes));

        // Apply animations
        if(correct) {
            tvResult.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce));
        } else {
            tvResult.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        }

        // Generate new question after delay
        new Handler().postDelayed(this::generateNewQuestion, 1500);
    }
}