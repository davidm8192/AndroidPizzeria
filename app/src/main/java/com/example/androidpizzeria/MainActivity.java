package com.example.androidpizzeria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Main Activity Class. Represents the Main Screen that processes user button presses in the UI fields and calls the
 * corresponding methods that opens the corresponding screens. Maintains necessary information for the transfer of
 * information between classes.
 *
 * @author David Ma, Ethan Kwok
 */
public class MainActivity extends AppCompatActivity {
    private Button chicagoButton, newYorkButton, currentOrderButton, storeOrderButton;

    private static ArrayList<Integer> orderNumberArrayList = new ArrayList<Integer>();
    private static Order myOrder = new Order();
    private static StoreOrder myStoreOrder = new StoreOrder();

    /**
     * Runs on creation. Defines the four buttons that opens the other screens and the corresponding click actions.
     * @param savedInstanceState Bundle containing the saved instance when MainActivity is created
     */
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

        newYorkButton = findViewById(R.id.newYorkButton);
        newYorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewYork();
            }
        });

        currentOrderButton = findViewById(R.id.currentOrderButton);
        currentOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCurrentOrder();
            }
        });

        storeOrderButton = findViewById(R.id.storeOrderButton);
        storeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStoreOrders();
            }
        });
    }

    /**
     * Loads and opens the ChicagoStyle pizza screen
     */
    public void openChicago() {
        Intent intent = new Intent(MainActivity.this, ChicagoActivity.class);
        startActivity(intent);
    }

    /**
     * Loads and opens the NewYorkStyle pizza screen
     */
    public void openNewYork() {
        Intent intent = new Intent(MainActivity.this, NewYorkActivity.class);
        startActivity(intent);
    }

    /**
     * Loads and opens the CurrentOrder screen
     */
    public void openCurrentOrder() {
        Intent intent = new Intent(MainActivity.this, CurrentOrderActivity.class);
        startActivity(intent);
    }

    /**
     * Loads and opens the StoreOrders screen
     */
    public void openStoreOrders() {
        Intent intent = new Intent(MainActivity.this, StoreOrdersActivity.class);
        startActivity(intent);
    }

    /**
     * Getter method to get the current order.
     * @return Order object representing the current order
     */
    public static Order getMyOrder() {
        return myOrder;
    }

    /**
     * Getter method to get the store order.
     * @return Order object representing the store order
     */
    public static StoreOrder getMyStoreOrder() {
        return myStoreOrder;
    }

    /**
     * Getter method that returns the total number of orders in the store order. Keeps track through the size of the
     * orderNumberArrayList.
     * @return int representing the number of orders in the store order.
     */
    public static ArrayList<Integer> getOrderNumArrayList() {
       return orderNumberArrayList;
   }

    /**
     * Adds a pizza to the current order.
     * @param pizza Pizza object to be added to the current order.
     */
    public static void addToOrder(Pizza pizza) {
        myOrder.add(pizza);
    }

    /**
     * Creates the order number such that it is a unique number. Keeps track of this number in orderNumberArrayList.
     * @return int representing the order number.
     */
    public static int orderNumber() {
        int i = 1;
        while(orderNumberArrayList.contains(i)) i++;
        orderNumberArrayList.add(i);
        return i;
    }

    /**
     * Creates and sets a new Order object for when an order is completed and a new one needs to be set.
     */
    public static void resetMyOrder() {
        myOrder = new Order();
    }

    /**
     * Adds an order to the list of store orders for when the order is completed.
     * @param order Order object to be added to the store orders.
     */
    public static void addToStoreOrder(Order order) {
        myStoreOrder.add(order);
    }

}