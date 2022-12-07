package com.example.androidpizzeria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class ChicagoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner flavorSpinner;
    private RecyclerView allToppingsRecycler, addedToppingsRecycler;
    private RecyclerView.Adapter allToppingsAdapter, addedToppingsAdapter;
    private RecyclerView.LayoutManager allToppingsLayoutManager, addedToppingsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago);

        flavorSpinner = findViewById(R.id.flavorSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.flavors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flavorSpinner.setAdapter(adapter);
        flavorSpinner.setOnItemSelectedListener(this);

        ArrayList<AllToppings> allToppingsArrayList = new ArrayList<>();
        allToppingsArrayList.add(new AllToppings("LINE 1 TESTTTTTTTICL"));
        allToppingsArrayList.add(new AllToppings("LINE 2 TESTTTTTTTICL"));
        allToppingsArrayList.add(new AllToppings("LINE 3 TESTTTTTTTICL"));
        allToppingsRecycler = findViewById(R.id.allToppingsRecycler);
        allToppingsRecycler.setHasFixedSize(true);
        allToppingsLayoutManager = new LinearLayoutManager(this);
        allToppingsAdapter = new AllToppingsAdapter(allToppingsArrayList);
        allToppingsRecycler.setLayoutManager(allToppingsLayoutManager);
        allToppingsRecycler.setAdapter(allToppingsAdapter);

        ArrayList<AllToppings> addedToppingsArrayList = new ArrayList<>();
        addedToppingsArrayList.add(new AllToppings("POOP #1"));
        addedToppingsArrayList.add(new AllToppings("POOP #2"));
        addedToppingsArrayList.add(new AllToppings("POOP #3"));
        addedToppingsRecycler = findViewById(R.id.addedToppingsRecycler);
        addedToppingsRecycler.setHasFixedSize(true);
        addedToppingsLayoutManager = new LinearLayoutManager(this);
        addedToppingsAdapter = new AllToppingsAdapter(addedToppingsArrayList);
        addedToppingsRecycler.setLayoutManager(addedToppingsLayoutManager);
        addedToppingsRecycler.setAdapter(addedToppingsAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String flavor = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}