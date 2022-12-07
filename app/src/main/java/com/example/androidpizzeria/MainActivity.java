package com.example.androidpizzeria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button chicagoButton;
    private Button currentOrderButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chicagoButton = (Button) findViewById(R.id.chicagoButton);
        chicagoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChicago();
            }
        });

        currentOrderButton = (Button) findViewById(R.id.currentOrderButton);
        currentOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrentOrder();
            }
        });
    }

    public void openChicago() {
        Intent intent = new Intent(MainActivity.this, ChicagoActivity.class);
        startActivity(intent);
    }

    public void openCurrentOrder() {
        Intent intent = new Intent(MainActivity.this, CurrentOrderActivity.class);
        startActivity(intent);
    }

}