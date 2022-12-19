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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * CurrentOrder Activity Class is the Current Order screen. Allows the user to view the pizzas and order number
 * in the order; view the subtotal, tax, and total price; and remove pizzas from the order. It also allows a
 * user to place the order once completed.
 *
 * @author David Ma, Ethan Kwok
 */
public class CurrentOrderActivity extends AppCompatActivity {

    private TextView orderTextView, subtotalTextView, taxTextView, totalTextView;
    private ListView orderListView;
    private Button backButton, removeOrderButton, clearOrderButton, placeOrderButton;
    private Toast toast;
    private AlertDialog.Builder clearOrderAlert;

    private Order myOrder;
    private int itemPosition;
    private String pizzaInfo;
    private ArrayList<String> pizzaList;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final double TAX_RATE = 0.06625;

    private static final Crust[] ChicagoStyleCrusts = {Crust.valueOf("DEEP_DISH"), Crust.valueOf("PAN"),
            Crust.valueOf("STUFFED")};

    /**
     * Creation function used to set up the screen. Displays the current order of pizzas in the list view, as well
     * as the order number. If there is no order, is creates a new order. Displays the order subtotal, tax, and total.
     * @param savedInstanceState Bundle containing the saved instance when ChicagoActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        myOrder = MainActivity.getMyOrder();
        orderListView = findViewById(R.id.orderListView);
        pizzaList = new ArrayList<String>();
        createPizzaList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CurrentOrderActivity.this,
            R.layout.list_view_layout, R.id.listItem, pizzaList);
        orderListView.setAdapter(adapter);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pizzaInfo = adapter.getItem(position);
                itemPosition = position;
                view.setSelected(true);
            }
        });

        orderTextView = findViewById(R.id.orderTextView);
        subtotalTextView = findViewById(R.id.subtotalTextView);
        taxTextView = findViewById(R.id.taxTextView);
        totalTextView = findViewById(R.id.totalTextView);

        buildButtons(adapter);

        updateOrderNumber();
        updateSubtotal();
        updateTax();
        updateTotal();
    }

    /**
     * Defines the removeOrder button, clearOrder button, placeOrder button, and back button, as well as their
     * click events.
     * @param adapter ArrayAdapter of Strings set to the order listView.
     */
    private void buildButtons(ArrayAdapter<String> adapter) {
        removeOrderButton = findViewById(R.id.removePizzaButton);
        removeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeOrder(adapter);
            }
        });

        clearOrderButton = findViewById(R.id.clearOrderButton);
        clearOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearOrder(adapter);
            }
        });

        placeOrderButton = findViewById(R.id.placeOrderButton);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder(adapter);
            }
        });

        backButton = findViewById(R.id.backButton3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
    }

    /**
     * Sets the list of pizzas to be strings of each pizza in the order.
     */
    public void createPizzaList() {
        if(myOrder != null) {
            for(Pizza p : myOrder.getOrder()) {
                pizzaList.add(toString(p));
            }
        }
    }

    /**
     * Returns to the main screen
     */
    public void openMain() {
        Intent intent = new Intent(CurrentOrderActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Removes the selected pizza in the list view from the order. Adjusts the prices as necessary, and tells
     * the user whether or not a pizza has been deleted.
     * @param adapter ArrayAdapter of Strings set to the order listView.
     */
    private void removeOrder(ArrayAdapter<String> adapter) {
        if (pizzaInfo == null) {
            toast.cancel();
            toast = Toast.makeText(CurrentOrderActivity.this, "Select a pizza to delete", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            toast.cancel();
            String toastString = "Deleted " + pizzaInfo;
            toast = Toast.makeText(CurrentOrderActivity.this, toastString, Toast.LENGTH_SHORT);
            toast.show();

            myOrder.remove(myOrder.getOrder().get(itemPosition));
            pizzaList.remove(itemPosition);
            adapter.notifyDataSetChanged();
            pizzaInfo = null;
        }
    }

    /**
     * Removes all pizzas in the list view from the order. Adjusts the prices as necessary, and tells the user
     * whether or not the order has been successfully cleared.
     * @param adapter ArrayAdapter of Strings set to the order listView.
     */
    private void clearOrder(ArrayAdapter<String> adapter) {
        clearOrderAlert = new AlertDialog.Builder(CurrentOrderActivity.this);

        clearOrderAlert.setTitle("CLEAR ORDER?")
                .setMessage("Do you want to remove all pizzas from the order?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast.cancel();
                        toast = Toast.makeText(CurrentOrderActivity.this, "Order cleared", Toast.LENGTH_SHORT);
                        toast.show();
                        myOrder.getOrder().clear();
                        pizzaList.clear();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast.cancel();
                        toast = Toast.makeText(CurrentOrderActivity.this, "Order was not cleared", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .show();
    }

    /**
     * Places an order when pressed. This finalizes the order, sending it to the list of store orders and alerting
     * the user that the order has successfully been placed. It then closes the window. If the order is empty, it
     * does not do any of this and instead alerts the user that an empty order cannot be placed.
     * @param adapter ArrayAdapter of Strings set to the order listView.
     */
    public void placeOrder(ArrayAdapter<String> adapter) {
        if (myOrder.getOrder().size() == 0) {
            toast.cancel();
            toast = Toast.makeText(CurrentOrderActivity.this, "Cannot place an empty order", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        clearOrderAlert = new AlertDialog.Builder(CurrentOrderActivity.this);
        clearOrderAlert.setTitle("FINISH ORDER?")
                .setMessage("Do you want to place the order?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast.cancel();
                        toast = Toast.makeText(CurrentOrderActivity.this, "Order placed", Toast.LENGTH_SHORT);
                        toast.show();

                        MainActivity.addToStoreOrder(myOrder);
                        MainActivity.resetMyOrder();
                        adapter.notifyDataSetChanged();
                        openMain();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast.cancel();
                        toast = Toast.makeText(CurrentOrderActivity.this, "Order was not placed", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .show();
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

    public void updateOrderNumber() {
        String orderNumber = Integer.toString(myOrder.getOrderNumber());
        orderTextView.setText("ORDER #: " + orderNumber);
    }

    public void updateSubtotal() {
        String subtotalString = df.format(subtotalPrice(myOrder));
        subtotalTextView.setText(("SUBTOTAL: $" + subtotalString));
    }

    public void updateTax() {
        String taxString = df.format(taxPrice(myOrder));
        taxTextView.setText(("TAX: $" + taxString));
    }

    public void updateTotal() {
        String totalString = df.format(totalPrice(myOrder));
        totalTextView.setText(("TOTAL: $" + totalString));
    }

    /**
     * Calculates and returns the price of the order before tax.
     * @param myOrder Order whose price is to be calculated.
     * @return double representing the cost in dollars of the order before tax.
     */
    public static double subtotalPrice(Order myOrder) {
        double price = 0;
        try {
            for (Pizza p : myOrder.getOrder()) {
                price += p.price();
            }
            return price;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Calculates and returns the price of tax of a given order using a set tax rate.
     * @param myOrder Order whose tax is to be calculated.
     * @return double representing the cost in dollars of the tax of an order.
     */
    public static double taxPrice(Order myOrder) {
        double price = subtotalPrice(myOrder);
        price *= TAX_RATE;
        return price;
    }

    /**
     * Calculates and returns the total price of an order including tax.
     * @param myOrder Order whose total price is to be calculated.
     * @return double representing the cost in dollars of total price of an order including tax.
     */
    public static double totalPrice(Order myOrder) {
        return subtotalPrice(myOrder) + taxPrice(myOrder);
    }

}