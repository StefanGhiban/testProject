package com.example.scratch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class OrderConfirmationActivity extends AppCompatActivity {

    private MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        toolbar.setLogo(R.drawable.logo);

        button = findViewById(R.id.processButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderConfirmationActivity.this, ConfirmedOrderActivity.class);
                startActivity(i);
            }
        });
    }
}