package com.example.asm1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ComposeActivity extends AppCompatActivity {
    private int target;
    private final List<Integer> selected = new ArrayList<>();
    private final Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        generateNewProblem();
        setupNumberGrid();
    }

    private void generateNewProblem() {
        target = rand.nextInt(98) + 2; // Minimum target 2
        ((TextView) findViewById(R.id.tvTarget)).setText("Make: " + target);
    }

    private void setupNumberGrid() {
        List<Integer> numbers = new ArrayList<>();

        // Add guaranteed valid pairs
        int pair1 = target / 2;
        int pair2 = target - pair1;
        numbers.add(pair1);
        numbers.add(pair2);

        // Add secondary valid pair (different combination)
        int altPair1 = Math.max(0, pair1 - 1);
        int altPair2 = target - altPair1;
        numbers.add(altPair1);
        numbers.add(altPair2);

        // Fill remaining slots with random numbers (0 to target)
        while (numbers.size() < 6) {
            int num = rand.nextInt(target + 1);
            numbers.add(num);
        }
        Collections.shuffle(numbers);

        GridView grid = findViewById(R.id.gridNumbers);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                this,
                R.layout.grid_item,  // Use custom layout
                R.id.numberText,     // ID of TextView in grid_item.xml
                numbers
        );
        grid.setAdapter(adapter);
        grid.setOnItemClickListener((parent, view, position, id) ->
                onNumberSelected(numbers.get(position)));
    }

    private void onNumberSelected(int number) {
        selected.add(number);
        if (selected.size() == 2) {
            checkCombination();
        }
        updateSelectedDisplay();
    }

    private void checkCombination() {
        int sum = selected.get(0) + selected.get(1);
        if (sum == target) {
            showSuccess();
            generateNewProblem();
            setupNumberGrid();
        } else {
            showError();
        }
        selected.clear();
    }

    private void showSuccess() {
        Toast.makeText(this, "Correct! 😊", Toast.LENGTH_SHORT).show();
    }

    private void showError() {
        Toast.makeText(this, "Try Again! 😟", Toast.LENGTH_SHORT).show();
    }

    private void updateSelectedDisplay() {
        String display = selected.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + " + " + b)
                .orElse("");
        ((TextView) findViewById(R.id.tvSelected)).setText(display);
    }
}