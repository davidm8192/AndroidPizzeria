package com.example.androidpizzeria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * StoreOrderActivity class is the screen for seeing placed orders. Allows the user to cancel orders or view
 * different orders based on order number.
 *
 * @author David Ma, Ethan Kwok
 */
public class StoreOrdersActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Button backButton, removeOrderButton;
    private Spinner orderSpinner;
    private TextView priceTextView;
    private Toast toast;
    private ListView storeOrderListView;
    private AlertDialog.Builder removeOrderAlert;

    private ArrayAdapter<String> listAdapter, spinAdapter;
    private ArrayList<String> pizzaList;
    private ArrayList<String> orderList;

    private StoreOrder myStoreOrder;
    private int orderNum;

    private static final int STARTING_ORDER_NUMBER = 1;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final Crust[] ChicagoStyleCrusts = {Crust.valueOf("DEEP_DISH"), Crust.valueOf("PAN"),
            Crust.valueOf("STUFFED")};

    /**
     * Creation function used to set up the screen. Defines the default order to be displayed in the listView,
     * defines the list adapter, creates the price textView, and calls the methods to build the order number
     * spinner and buttons.
     * @param savedInstanceState Bundle containing the saved instance when ChicagoActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_orders);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        myStoreOrder = MainActivity.getMyStoreOrder();

        storeOrderListView = findViewById(R.id.storeOrderListView);
        priceTextView = findViewById(R.id.storeOrderTotalTextView);
        pizzaList = new ArrayList<String>();
        orderList = new ArrayList<String>();

        createOrderList();

        if(myStoreOrder.getStoreOrder() != null) {
            Order startingOrder = null;
            for(Order o : myStoreOrder.getStoreOrder()) {
                if(o.getOrderNumber() == MainActivity.getOrderNumArrayList().get(0)) {
                    startingOrder = o;
                }
            }
            if(startingOrder != null) {
                createPizzaList(startingOrder);
            }
        }

        listAdapter  = new ArrayAdapter<>(StoreOrdersActivity.this,
                R.layout.list_view_layout, R.id.listItem, pizzaList);
        storeOrderListView.setAdapter(listAdapter);

        buildOrderSpinner();
        buildButtons();

    }

    /**
     * Creates the order spinner that allows the user to select which order number to display.
     */
    public void buildOrderSpinner() {
        orderSpinner = findViewById(R.id.orderSpinner);
        spinAdapter = new ArrayAdapter<>(StoreOrdersActivity.this,
                android.R.layout.simple_spinner_item, orderList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(spinAdapter);

        orderSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Creates the removeOrder button and backButton, as well as their click events.
     */
    private void buildButtons() {
        removeOrderButton = findViewById(R.id.removeOrderButton);
        removeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOrderAlert = new AlertDialog.Builder(StoreOrdersActivity.this);

                removeOrderAlert.setTitle("REMOVE ORDER?")
                        .setMessage("Do you want to remove the order?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeOrder();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                toast.cancel();
                                toast = Toast.makeText(StoreOrdersActivity.this, "Order was not removed", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        })
                        .show();
            }
        });

        backButton = findViewById(R.id.backButton4);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
    }

    /**
     * Sets the list of pizzas to be strings of each pizza in the order.
     * @param order Order to be displayed based on the selected order number in the spinner.
     */
    public void createPizzaList(Order order) {
        if(order != null) {

            for(Pizza p : order.getOrder()) {
                pizzaList.add(toString(p));
            }
        }
    }

    /**
     * Creates the order list to match the selected order number.
     */
    public void createOrderList() {
        for(Integer i : MainActivity.getOrderNumArrayList()) {
            orderList.add(Integer.toString(i));
        }
        if(!(MainActivity.getOrderNumArrayList().size() <= STARTING_ORDER_NUMBER)) {
            int numOrders = MainActivity.getOrderNumArrayList().size() - 1;
            orderList.remove(numOrders);
        }
    }

    /**
     * Returns to the main screen
     */
    public void openMain() {
        Intent intent = new Intent(StoreOrdersActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Converts a pizza to a string to be displayed in the listview.
     * @param pizza pizza with information that is to be converted to a string.
     * @return String representing the pizza's characteristics.
     */
    public static String toString(Pizza pizza) {
        String output = "";
        if (pizza instanceof Deluxe) output += "DELUXE";
        if (pizza instanceof BBQChicken) output += "BBQ CHICKEN";
        if (pizza instanceof Meatzza) output += "MEATZZA";
        if (pizza instanceof BuildYourOwn) output += "BUILD YOUR OWN";
        if (isChicagoStyle(pizza.getCrust())) output += " (CHICAGO STYLE - ";
        else output += " (NEW YORK STYLE - ";
        output += pizza.getCrust() + "), ";
        for (Toppings t : pizza.getToppingsArrayList()) {
            output += t.toString() + ", ";
        }
        output += pizza.getSize() + ": $" + df.format(pizza.price());
        return output;
    }

    /**
     * Checks if a pizza is a ChicacgoStyle pizza by checking the crust.
     * @param crust crust of the pizza to be checked.
     * @return true if the crust is of the Chicago style, false if it is not (and is therefore of the New York Style).
     */
    private static boolean isChicagoStyle(Crust crust) {
        for (Crust c : ChicagoStyleCrusts) {
            if (c.equals(crust)) return true;
        }
        return false;
    }

    /**
     * Displays the information of the current order selected by the Spinner in the order ListView.
     * The information includes the pizzas in the order and the total cost of the order.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            orderNum = Integer.parseInt(parent.getItemAtPosition(position).toString());
            displayOrder();
        }
        catch(Exception e) {
            pizzaList.clear();
            listAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Displays the information of the current order selected by the ComboBox in the order ListView.
     * The information includes the pizzas in the order and the total cost of the order.
     */
    public void displayOrder() {
        Order orderToDisplay = null;
        for (Order o : myStoreOrder.getStoreOrder()) {
            if (o.getOrderNumber() == orderNum) {
                orderToDisplay = o;

            }
        }
        if (orderToDisplay != null) {

            pizzaList.clear();
            createPizzaList(orderToDisplay);
            listAdapter.notifyDataSetChanged();

            priceTextView.setText("TOTAL: $" + df.format(CurrentOrderActivity.totalPrice(orderToDisplay)));
        }

    }

    /**
     * Removes the current order selected by the spinner. Removes the order from the order arraylist as well as
     * the order number from the order number array list and spinner.
     */
    public void removeOrder() {
        try {
            Order orderToCancel = null;
            for(Order o : myStoreOrder.getStoreOrder()) {
                if(o.getOrderNumber() == orderNum) {
                    orderToCancel = o;
                }
            }
            if(myStoreOrder.remove(orderToCancel)) {
                int removeIndex = MainActivity.getOrderNumArrayList().indexOf(orderNum);
                orderList.remove(removeIndex);
                spinAdapter.notifyDataSetChanged();
                MainActivity.getOrderNumArrayList().remove(Integer.valueOf(orderNum));
                if(MainActivity.getOrderNumArrayList().size() == STARTING_ORDER_NUMBER) {
                    pizzaList.clear();
                    listAdapter.notifyDataSetChanged();
                    priceTextView.setText("TOTAL: $0.00");
                }
                orderNum = Integer.parseInt(orderSpinner.getSelectedItem().toString());
                displayOrder();
            }
            else {
                toast.cancel();
                toast = Toast.makeText(StoreOrdersActivity.this, "No order to remove", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }
        catch(Exception e) {
            pizzaList.clear();
            listAdapter.notifyDataSetChanged();
            priceTextView.setText("TOTAL: $0.00");
        }
        toast.cancel();
        toast = Toast.makeText(StoreOrdersActivity.this, "Order Removed", Toast.LENGTH_SHORT);
        toast.show();
    }

}