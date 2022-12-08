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

public class ChicagoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago);

        pizzaFactory = new ChicagoPizza();
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
        buildToppingsRecyclers();
        buildButtons();

        updatePrice();
        updateCrust();

    }

    public void buildButtons() {
        backButton = findViewById(R.id.backButton);
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

    public void buildFlavorSpinner() {
        flavorSpinner = findViewById(R.id.flavorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.flavors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flavorSpinner.setAdapter(adapter);
        flavorSpinner.setOnItemSelectedListener(this);
    }

    public void buildToppingsRecyclers() {
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
                else toastAddFail();
            }
        });

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
                else toastRemoveFail();
            }
        });
    }

    public void openMain() {
        Intent intent = new Intent(ChicagoActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Adds the Pizza to the current order, alerts the user, and then closes the window.
     */
    public void addToOrder() {
        addToOrderAlert = new AlertDialog.Builder(ChicagoActivity.this);

        addToOrderAlert.setTitle("ADD ORDER")
                .setMessage("Do you want to add the pizza to the order?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast.cancel();
                        toast = Toast.makeText(ChicagoActivity.this, "Pizza added to order", Toast.LENGTH_SHORT);
                        toast.show();
                        MainActivity.addToOrder(myPizza);
                        openMain();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toast.cancel();
                        toast = Toast.makeText(ChicagoActivity.this, "Pizza was not added", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .show();
    }

    public void toastAddFail() {
        toast.cancel();
        toast = Toast.makeText(this, "TOPPING COULD NOT BE ADDED", Toast.LENGTH_SHORT);
        toast.show();
        //Toast.makeText(this, "TOPPING COULD NOT BE ADDED", Toast.LENGTH_SHORT).show();
    }
    
    public void toastRemoveFail() {
        toast.cancel();
        toast = Toast.makeText(this, "TOPPING COULD NOT BE REMOVED", Toast.LENGTH_SHORT);
        toast.show();
        //Toast.makeText(this, "TOPPING COULD NOT BE REMOVED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String flavor = parent.getItemAtPosition(position).toString();
        canAddToppings = false;
        canRemoveToppings = false;

        if (flavor.equals("DELUXE")) {
            myPizza = pizzaFactory.createDeluxe();
            pizzaImage.setImageResource(R.drawable.deluxe_chicago_pizza);
        }
        if (flavor.equals("MEATZZA")) {
            myPizza = pizzaFactory.createMeatzza();
            pizzaImage.setImageResource(R.drawable.chicago_meatzza);
        }
        if (flavor.equals("BBQ CHICKEN")) {
            myPizza = pizzaFactory.createBBQChicken();
            pizzaImage.setImageResource(R.drawable.chicago_bbq);
        }
        if (flavor.equals("BUILD YOUR OWN")) {
            myPizza = pizzaFactory.createBuildYourOwn();
            canAddToppings = true;
            canRemoveToppings = true;
            pizzaImage.setImageResource(R.drawable.chicago_style_with_toppings);
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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

    public void checkSizeRadioButton(View v) {
        int radioId = sizeRadioGroup.getCheckedRadioButtonId();
        sizeRadioButton = findViewById(radioId);
        if (sizeRadioButton.getText().equals("SMALL")) myPizza.setSize(Size.valueOf("SMALL"));
        if (sizeRadioButton.getText().equals("MEDIUM")) myPizza.setSize(Size.valueOf("MEDIUM"));
        if (sizeRadioButton.getText().equals("LARGE")) myPizza.setSize(Size.valueOf("LARGE"));
        updatePrice();
    }

    public void updateSize() {
        int radioId = sizeRadioGroup.getCheckedRadioButtonId();
        sizeRadioButton = findViewById(radioId);
        if (sizeRadioButton.getText().equals("SMALL")) myPizza.setSize(Size.valueOf("SMALL"));
        if (sizeRadioButton.getText().equals("MEDIUM")) myPizza.setSize(Size.valueOf("MEDIUM"));
        if (sizeRadioButton.getText().equals("LARGE")) myPizza.setSize(Size.valueOf("LARGE"));
        updatePrice();
    }

    public void updatePrice() {
        String priceString = df.format(myPizza.price());
        priceTextView.setText("PRICE: $" + priceString);
    }

    public void updateCrust() {
        String crustString = myPizza.getCrust().toString();
        crustTextView.setText("CRUST: " + crustString);
    }

}