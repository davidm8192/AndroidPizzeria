package com.example.androidpizzeria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 * NewYorkStyleActivity Class is the User Interface screen for NewYork Style Pizzas. Allows the user to choose the
 * flavor of the pizza and the size. If the pizza is a build-your-own, it allows the user to add or remove toppings.
 * Displays the toppings on the pizza, the crust, and an image of the pizza. Keeps track of and displays the
 * price of the pizza.
 *
 * @author David Ma, Ethan Kwok
 */
public class NewYorkActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner flavorSpinner;
    private RecyclerView allToppingsRecycler, addedToppingsRecycler;
    private AllToppingsAdapter allToppingsAdapter, addedToppingsAdapter;
    private RecyclerView.LayoutManager allToppingsLayoutManager, addedToppingsLayoutManager;
    private TextView crustTextView, priceTextView;
    private RadioGroup sizeRadioGroup;
    private RadioButton sizeRadioButton;
    private Button backButton, addToOrderButton;
    private AlertDialog.Builder addToOrderAlert;

    private PizzaFactory pizzaFactory;
    private Pizza myPizza;
    private ArrayList<AllToppings> allToppingsArrayList, addedToppingsArrayList;
    private ImageView pizzaImage;
    private boolean canAddToppings, canRemoveToppings;

    private static final int MAX_TOPPINGS = 7;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private Toast toast;

    /**
     * Creation function used to set up the screen. The default pizza is a small deluxe pizza. Calls methods to
     * build the flavor spinner, toppings recycler views, and buttons.
     * @param savedInstanceState Bundle containing the saved instance when NewYorkActivity is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_york);

        pizzaFactory = new NewYorkPizza();
        myPizza = pizzaFactory.createDeluxe();
        myPizza.setSize(Size.valueOf("SMALL"));
        canAddToppings = false;
        canRemoveToppings = false;

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        priceTextView = findViewById(R.id.priceTextView);
        crustTextView = findViewById(R.id.crustTextView);
        sizeRadioGroup = findViewById(R.id.sizeRadioGroup);
        pizzaImage = findViewById(R.id.pizzaImage);
        buildFlavorSpinner();
        buildAllToppingsRecycler();
        buildAddedToppingsRecycler();
        buildButtons();

        updatePrice();
        updateCrust();

    }

    /**
     * Defines the back button and addToOrder button, as well as their associated click events.
     */
    public void buildButtons() {
        backButton = findViewById(R.id.backButton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        addToOrderButton = findViewById(R.id.addToOrderButton);
        addToOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToOrder();
            }
        });
    }

    /**
     * Defines the flavor spinner that allows users to select the flavor of their pizza.
     */
    public void buildFlavorSpinner() {
        flavorSpinner = findViewById(R.id.flavorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.flavors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flavorSpinner.setAdapter(adapter);
        flavorSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Defines the all toppings recycler view. Defines the click events.
     */
    public void buildAllToppingsRecycler() {
        allToppingsArrayList = new ArrayList<>();
        for (Toppings t : Toppings.values()) {
            allToppingsArrayList.add(new AllToppings(t.toString()));
        }
        allToppingsRecycler = findViewById(R.id.allToppingsRecycler);
        allToppingsRecycler.setHasFixedSize(true);
        allToppingsLayoutManager = new LinearLayoutManager(this);
        allToppingsAdapter = new AllToppingsAdapter(allToppingsArrayList);
        allToppingsRecycler.setLayoutManager(allToppingsLayoutManager);
        allToppingsRecycler.setAdapter(allToppingsAdapter);
        allToppingsAdapter.setOnItemClickListener(new AllToppingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (canAddToppings) addTopping(position);
                else {
                    toast.cancel();
                    toast = Toast.makeText(NewYorkActivity.this, "TOPPING COULD NOT BE ADDED", Toast.LENGTH_SHORT);
                    toast.show();
                };
            }
        });
    }

    /**
     * Defines the added toppings recycler view. Defines the click events.
     */
    public void buildAddedToppingsRecycler() {
        addedToppingsArrayList = new ArrayList<>();
        for (Toppings t : myPizza.getToppingsArrayList()) {
            addedToppingsArrayList.add(new AllToppings(t.toString()));
        }
        addedToppingsRecycler = findViewById(R.id.addedToppingsRecycler);
        addedToppingsRecycler.setHasFixedSize(true);
        addedToppingsLayoutManager = new LinearLayoutManager(this);
        addedToppingsAdapter = new AllToppingsAdapter(addedToppingsArrayList);
        addedToppingsRecycler.setLayoutManager(addedToppingsLayoutManager);
        addedToppingsRecycler.setAdapter(addedToppingsAdapter);
        addedToppingsAdapter.setOnItemClickListener(new AllToppingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (canRemoveToppings) removeTopping(position);
                else {
                    toast.cancel();
                    toast = Toast.makeText(NewYorkActivity.this, "TOPPING COULD NOT BE REMOVED", Toast.LENGTH_SHORT);
                    toast.show();
                };
            }
        });
    }

    /**
     * Returns to the main screen
     */
    public void openMain() {
        Intent intent = new Intent(NewYorkActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Adds the Pizza to the current order, alerts the user, and then closes the window.
     */
    public void addToOrder() {
        addToOrderAlert = new AlertDialog.Builder(NewYorkActivity.this);

        addToOrderAlert.setTitle("ADD ORDER")
                .setMessage("Do you want to add the pizza to the order?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast.cancel();
                        toast = Toast.makeText(NewYorkActivity.this, "Pizza added to order", Toast.LENGTH_SHORT);
                        toast.show();
                        MainActivity.addToOrder(myPizza);
                        openMain();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast.cancel();
                        toast = Toast.makeText(NewYorkActivity.this, "Pizza was not added", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .show();
    }

    /**
     * Changes the flavor of the Pizza to match the spinner value selected. Changes the image to match the new
     * flavor, and calls the methods to update the size, price, and crust.
     * @param parent AdapterView<> for spinners
     * @param view View for spinners
     * @param position int representing the selected item's position
     * @param id long representing the spinner's id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String flavor = parent.getItemAtPosition(position).toString();
        canAddToppings = false;
        canRemoveToppings = false;

        if (flavor.equals("DELUXE")) {
            myPizza = pizzaFactory.createDeluxe();
            pizzaImage.setImageResource(R.drawable.deluxe_ny_pizza);
        }
        if (flavor.equals("MEATZZA")) {
            myPizza = pizzaFactory.createMeatzza();
            pizzaImage.setImageResource(R.drawable.ny_meatzza);
        }
        if (flavor.equals("BBQ CHICKEN")) {
            myPizza = pizzaFactory.createBBQChicken();
            pizzaImage.setImageResource(R.drawable.ny_bbq);
        }
        if (flavor.equals("BUILD YOUR OWN")) {
            myPizza = pizzaFactory.createBuildYourOwn();
            canAddToppings = true;
            canRemoveToppings = true;
            pizzaImage.setImageResource(R.drawable.ny_pizza_with_toppings);
        }

        addedToppingsArrayList.clear();
        for (Toppings t : myPizza.getToppingsArrayList()) {
            addedToppingsArrayList.add(new AllToppings(t.toString()));
        }
        addedToppingsRecycler.setLayoutManager(addedToppingsLayoutManager);
        addedToppingsRecycler.setAdapter(addedToppingsAdapter);

        updateSize();
        updateCrust();
    }

    /**
     * Spinner class used for when no flavor is selected
     * @param parent AdapterView<> for spinners
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Adds the selected topping onto the Pizza if applicable and displays the newly added topping.
     * @param position int representing the position of the selected item in the recycler view.
     * @return true if the topping is successfully added, false if there is an exception.
     */
    public boolean addTopping(int position) {
        try {
            AllToppings selectedRecycler = allToppingsArrayList.get(position);
            toast.cancel();
            toast = Toast.makeText(this, selectedRecycler.getText() + " ADDED", Toast.LENGTH_SHORT);
            toast.show();
            Toppings selectedTopping = (Toppings.valueOf(selectedRecycler.getText()));
            if (myPizza.add(selectedTopping)) {
                addedToppingsArrayList.add(selectedRecycler);
                addedToppingsRecycler.setLayoutManager(addedToppingsLayoutManager);
                addedToppingsRecycler.setAdapter(addedToppingsAdapter);
            }
            if (myPizza.getToppingsArrayList().size() >= MAX_TOPPINGS) {
                canAddToppings = false;
            }
            updatePrice();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Removes the selected topping from the Pizza if applicable and removes the topping from the list of toppings.
     * @param position int representing the position of the selected item in the recycler view.
     * @return true if the topping is successfully removed, false if there is an exception.
     */
    public boolean removeTopping(int position) {
        try {
            AllToppings selectedRecycler = addedToppingsArrayList.get(position);
            toast.cancel();
            toast = Toast.makeText(this, selectedRecycler.getText() + " REMOVED", Toast.LENGTH_SHORT);
            toast.show();
            Toppings selectedTopping = (Toppings.valueOf(selectedRecycler.getText()));
            if (myPizza.remove(selectedTopping)) {
                addedToppingsArrayList.remove(selectedRecycler);
                addedToppingsRecycler.setLayoutManager(addedToppingsLayoutManager);
                addedToppingsRecycler.setAdapter(addedToppingsAdapter);
            }
            if (myPizza.getToppingsArrayList().size() < MAX_TOPPINGS) {
                canAddToppings = true;
            }
            updatePrice();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Changes the size of the Pizza to match the radio buttons when the radio button is changed. It then calls
     * the method to update the price.
     * @param v View of the radio button group that controls this method.
     */
    public void checkSizeRadioButton(View v) {
        int radioId = sizeRadioGroup.getCheckedRadioButtonId();
        sizeRadioButton = findViewById(radioId);
        if (sizeRadioButton.getText().equals("SMALL")) myPizza.setSize(Size.valueOf("SMALL"));
        if (sizeRadioButton.getText().equals("MEDIUM")) myPizza.setSize(Size.valueOf("MEDIUM"));
        if (sizeRadioButton.getText().equals("LARGE")) myPizza.setSize(Size.valueOf("LARGE"));
        updatePrice();
    }

    /**
     * Changes the size of the Pizza to match the radio buttons when the flavor is changed. It then calls the
     * method to update the price.
     */
    public void updateSize() {
        int radioId = sizeRadioGroup.getCheckedRadioButtonId();
        sizeRadioButton = findViewById(radioId);
        if (sizeRadioButton.getText().equals("SMALL")) myPizza.setSize(Size.valueOf("SMALL"));
        if (sizeRadioButton.getText().equals("MEDIUM")) myPizza.setSize(Size.valueOf("MEDIUM"));
        if (sizeRadioButton.getText().equals("LARGE")) myPizza.setSize(Size.valueOf("LARGE"));
        updatePrice();
    }

    /**
     * Finds the price of the Pizza and displays it in the appropriate decimal format.
     */
    public void updatePrice() {
        String priceString = df.format(myPizza.price());
        priceTextView.setText("PRICE: $" + priceString);
    }

    /**
     * Finds the crust of the Pizza and displays it.
     */
    public void updateCrust() {
        String crustString = myPizza.getCrust().toString();
        crustTextView.setText("CRUST: " + crustString);
    }

}