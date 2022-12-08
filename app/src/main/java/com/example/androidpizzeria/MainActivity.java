package com.example.androidpizzeria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button chicagoButton;
    private Button currentOrderButton;

    private static ArrayList<Integer> orderNumberArrayList;
    private static Order myOrder = new Order();
    private static StoreOrder myStoreOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chicagoButton = findViewById(R.id.chicagoButton);
        chicagoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChicago();
            }
        });

        currentOrderButton = findViewById(R.id.currentOrderButton);
        currentOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrentOrder();
            }
        });

        orderNumberArrayList = new ArrayList<>();
        //myOrder = new Order();
        myStoreOrder = new StoreOrder();
    }

    public void openChicago() {
        Intent intent = new Intent(MainActivity.this, ChicagoActivity.class);
        startActivity(intent);
    }

    public void openCurrentOrder() {
        Intent intent = new Intent(MainActivity.this, CurrentOrderActivity.class);
        startActivity(intent);
    }

    public static Order getMyOrder() {
        return myOrder;
    }

    public static StoreOrder getMyStoreOrder() {
        return myStoreOrder;
    }

   public static ArrayList<Integer> getOrderNumArrayList() {
       return orderNumberArrayList;
   }

    public static void addToOrder(Pizza pizza) {
        myOrder.add(pizza);
    }

    public static int orderNumber() {
        int i = 1;
        while(orderNumberArrayList.contains(i)) i++;
        orderNumberArrayList.add(i);
        return i;
    }

    public static void resetMyOrder() {
        myOrder = new Order();
    }

    public static void addToStoreOrder(Order order) {
        myStoreOrder.add(order);
    }

}