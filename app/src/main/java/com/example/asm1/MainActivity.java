package com.example.asm1;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnCompare).setOnClickListener(v ->
                startActivity(new Intent(this, CompareActivity.class)));

        findViewById(R.id.btnOrder).setOnClickListener(v ->
                startActivity(new Intent(this, OrderActivity.class)));

        findViewById(R.id.btnCompose).setOnClickListener(v ->
                startActivity(new Intent(this, ComposeActivity.class)));
    }
}