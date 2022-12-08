package com.example.androidpizzeria;

import androidx.appcompat.app.AppCompatActivity;

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

public class CurrentOrderActivity extends AppCompatActivity {

    private Order myOrder;
    private TextView orderTextView, subtotalTextView, taxTextView, totalTextView;
    private ListView orderListView;
    private Button backButton, removeOrderButton, clearOrderButton, placeOrderButton;
    private Toast toast;

    private int itemPosition;
    private String pizzaInfo;
    private ArrayList<String> pizzaList;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final double TAX_RATE = 0.06625;

    private static final Crust[] ChicagoStyleCrusts = {Crust.valueOf("DEEP_DISH"), Crust.valueOf("PAN"),
            Crust.valueOf("STUFFED")};

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
            android.R.layout.simple_list_item_single_choice, pizzaList);
        orderListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        orderListView.setAdapter(adapter);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pizzaInfo = adapter.getItem(position);
                itemPosition = position;
            }
        });

        orderTextView = findViewById(R.id.orderTextView);
        subtotalTextView = findViewById(R.id.subtotalTextView);
        taxTextView = findViewById(R.id.taxTextView);
        totalTextView = findViewById(R.id.totalTextView);

        removeOrderButton = findViewById(R.id.removeOrderButton);
        removeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.cancel();
                String toastString = pizzaInfo + " has been deleted";
                toast = Toast.makeText(CurrentOrderActivity.this, toastString, Toast.LENGTH_SHORT);
                toast.show();

                pizzaList.remove(itemPosition);
                adapter.notifyDataSetChanged();
            }
        });

        clearOrderButton = findViewById(R.id.clearOrderButton);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        backButton = findViewById(R.id.backButton3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        updateOrderNumber();
        updateSubtotal();
        updateTax();
        updateTotal();
    }

    public void createPizzaList() {
        if(myOrder != null) {
            for(Pizza p : myOrder.getOrder()) {
                pizzaList.add(toString(p));
            }
        }
    }

    public void openMain() {
        Intent intent = new Intent(CurrentOrderActivity.this, MainActivity.class);
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

    public void updateOrderNumber() {
        String orderNumber = Integer.toString(myOrder.getOrderNumber());
        orderTextView.setText("ORDER #: " + orderNumber);
    }

    public void updateSubtotal() {
        String subtotalString = Double.toString(subtotalPrice(myOrder));
        subtotalTextView.setText(("SUBTOTAL: $" + subtotalString));
    }

    public void updateTax() {
        String taxString = Double.toString(taxPrice(myOrder));
        taxTextView.setText(("TAX: $" + taxString));
    }

    public void updateTotal() {
        String totalString = Double.toString(totalPrice(myOrder));
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