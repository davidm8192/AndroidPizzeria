package com.example.androidpizzeria;

/**
 * Interface class for customizable objects. Pizzas add toppings, orders add pizzas, and storeOrders add orders.
 */
public interface Customizable {
    boolean add (Object obj);
    boolean remove (Object obj);

}
