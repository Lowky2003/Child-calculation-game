package com.example.asm1;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderActivity extends AppCompatActivity {
    private final String TAG = "OrderActivity";
    private final List<Integer> numbers = new ArrayList<>();
    private LinearLayout container;
    private TextView tvInstruction;
    private int firstSelectedIndex = -1;
    private int secondSelectedIndex = -1;
    private boolean isAscendingMode;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        container = findViewById(R.id.containerNumbers);
        tvInstruction = findViewById(R.id.tvInstruction);

        if (container == null || tvInstruction == null) {
            showErrorAndFinish("Layout elements not found");
            return;
        }

        startNewRound();
        findViewById(R.id.btnCheck).setOnClickListener(v -> checkOrder());
    }

    private void startNewRound() {
        setRandomSortingMode();
        generateNumbers();
        setupNumberViews();
        updateInstructionText();
        resetSelection();
    }

    private void setRandomSortingMode() {
        isAscendingMode = random.nextBoolean();
    }

    private void updateInstructionText() {
        String instruction = isAscendingMode ?
                "Arrange numbers from SMALLEST to LARGEST" :
                "Arrange numbers from LARGEST to SMALLEST";
        tvInstruction.setText(instruction);
    }

    private void generateNumbers() {
        numbers.clear();
        while (numbers.size() < 5) {
            int num = random.nextInt(999) + 1;
            if (!numbers.contains(num)) {
                numbers.add(num);
            }
            if (numbers.size() >= 10) break;
        }

        // Create solvable puzzle
        if (isAscendingMode) {
            Collections.sort(numbers);
        } else {
            Collections.sort(numbers, Collections.reverseOrder());
        }
        Collections.shuffle(numbers);
    }

    private void setupNumberViews() {
        try {
            container.removeAllViews();
            for (int i = 0; i < numbers.size(); i++) {
                TextView tv = createNumberView(i);
                container.addView(tv);
            }
        } catch (Exception e) {
            Log.e(TAG, "Setup failed: " + e.getMessage());
            showErrorAndFinish("Failed to setup numbers");
        }
    }

    private TextView createNumberView(final int index) {
        TextView tv = new TextView(this);
        try {
            tv.setText(String.valueOf(numbers.get(index)));
            tv.setTextSize(24);
            tv.setPadding(32, 32, 32, 32);
            tv.setBackground(ContextCompat.getDrawable(this, R.drawable.number_bg));
            tv.setOnClickListener(v -> handleNumberClick(index, tv));
        } catch (Exception e) {
            Log.e(TAG, "View creation failed: " + e.getMessage());
        }
        return tv;
    }

    private void handleNumberClick(int index, TextView tv) {
        if (firstSelectedIndex == -1) {
            firstSelectedIndex = index;
            tv.setBackgroundResource(R.drawable.number_bg_selected);
        } else if (secondSelectedIndex == -1 && index != firstSelectedIndex) {
            secondSelectedIndex = index;
            tv.setBackgroundResource(R.drawable.number_bg_selected);
            swapNumbers();
        } else {
            resetSelection();
            if (index != firstSelectedIndex) {
                firstSelectedIndex = index;
                tv.setBackgroundResource(R.drawable.number_bg_selected);
            }
        }
    }

    private void swapNumbers() {
        if (!isValidIndex(firstSelectedIndex) || !isValidIndex(secondSelectedIndex)) return;

        // Swap in data model
        Collections.swap(numbers, firstSelectedIndex, secondSelectedIndex);

        // Update UI views
        TextView firstView = (TextView) container.getChildAt(firstSelectedIndex);
        TextView secondView = (TextView) container.getChildAt(secondSelectedIndex);
        firstView.setText(String.valueOf(numbers.get(firstSelectedIndex)));
        secondView.setText(String.valueOf(numbers.get(secondSelectedIndex)));

        resetSelection();
    }

    private void resetSelection() {
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            child.setBackgroundResource(R.drawable.number_bg);
        }
        firstSelectedIndex = -1;
        secondSelectedIndex = -1;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < numbers.size();
    }

    private void checkOrder() {
        boolean isCorrect = isAscendingMode ? checkAscending() : checkDescending();

        if (isCorrect) {
            Toast.makeText(this, "Correct! Next round...", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(this::startNewRound, 1500);
        } else {
            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
            resetSelection();
        }
    }

    private boolean checkAscending() {
        for (int i = 0; i < numbers.size() - 1; i++) {
            if (numbers.get(i) > numbers.get(i + 1)) return false;
        }
        return true;
    }

    private boolean checkDescending() {
        for (int i = 0; i < numbers.size() - 1; i++) {
            if (numbers.get(i) < numbers.get(i + 1)) return false;
        }
        return true;
    }

    private void showErrorAndFinish(String message) {
        Log.e(TAG, message);
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
        finish();
    }
}