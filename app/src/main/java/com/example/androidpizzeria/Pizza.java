package com.example.androidpizzeria;

import java.util.ArrayList;

/**
 * Abstract class representing the Pizzas to be added to each order. Gets and sets values of each Pizza, including
 * the toppings, crust, and size.
 *
 * @author David Ma, Ethan Kwok
 */
public abstract class Pizza implements Customizable {

    private ArrayList<Toppings> toppingsArrayList;
    private Crust crust;
    private Size size;
    public abstract double price();

    /**
     * Getter method that returns the arraylist of toppings or creates a new arraylist if it does not already exist.
     * @return ArrayList of toppings of the pizza.
     */
    public ArrayList<Toppings> getToppingsArrayList() {
        if (toppingsArrayList == null) toppingsArrayList = new ArrayList<>();
        return toppingsArrayList;
    }

    /**
     * Setter method that sets the arraylist of toppings on the pizza to a given arraylist.
     * @param newToppingsArrayList new ArrayList of toppings to set to the pizza.
     */
    public void setToppingsArrayList(ArrayList<Toppings> newToppingsArrayList) {
        this.toppingsArrayList = newToppingsArrayList;
    }

    /**
     * Getter method that returns the pizza's crust.
     * @return Crust enum representing the pizza's crust.
     */
    public Crust getCrust() {
        return this.crust;
    }

    /**
     * Setter method that sets the pizza's crust.
     * @param newCrust Crust enum representing the crust to assign to the pizza.
     */
    public void setCrust(Crust newCrust) {
        this.crust = newCrust;
    }

    /**
     * Getter method that returns the pizza's size
     * @return Size enum representing the size of the pizza.
     */
    public Size getSize() {
        return this.size;
    }

    /**
     * Setter method that sets the pizza's new size
     * @param newSize Size enum representing the new size to assign to the pizza.
     */
    public void setSize(Size newSize) {
        this.size = newSize;
    }

}
