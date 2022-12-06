package com.example.androidpizzeria;

import java.util.ArrayList;

/**
 * Concrete subclass of pizza representing the Meatzza flavor of pizzas. Allows for the adding and removing of
 * toppings, and keeps track of the price of the Pizza.
 *
 * @author David Ma, Ethan Kwok
 */
public class Meatzza extends Pizza {

    /**
     * Calculates the price of the pizza based on the size and flavor.
     * @return double representing the price of the pizza.
     */
    public double price() {
        double price = this.getSize().getMeatzza();
        return price;
    }

    /**
     * Adds a topping to the pizza if possible by adding it to the pizza's topping arraylist.
     * @param obj Object that, if it is an instance of Toppings, will be added to the pizza if possible.
     * @return false if obj is not a topping or if the pizza already contains the topping. True if the topping is
     * successfully added.
     */
    public boolean add(Object obj) {
        if (obj instanceof Toppings) {
            Toppings newTopping = (Toppings) obj;
            ArrayList<Toppings> toppingList = getToppingsArrayList();
            if (toppingList.contains(newTopping)) return false;
            toppingList.add(newTopping);
            setToppingsArrayList(toppingList);
            return true;
        }
        return false;
    }

    /**
     * Removes a topping to the pizza if possible by removing it to the pizza's topping arraylist.
     * @param obj Object that, if it is an instance of Toppings, will be removed to the pizza if possible.
     * @return false if obj is not a topping or is unable to be removed. True if the topping is successfully removed.
     */
    public boolean remove(Object obj) {
        if (obj instanceof Toppings) {
            Toppings newTopping = (Toppings) obj;
            ArrayList<Toppings> toppingList = getToppingsArrayList();
            if (toppingList.remove(newTopping)) {
                setToppingsArrayList(toppingList);
                return true;
            }
        }
        return false;
    }

}
