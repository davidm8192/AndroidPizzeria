package com.example.androidpizzeria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StoreOrder implements Customizable {

    private ArrayList<Order> myStoreOrder;

    public StoreOrder() {
        myStoreOrder = new ArrayList<>();
    }

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public boolean add(Object obj) {
        if (obj instanceof Order) {
            Order newOrder = (Order) obj;
            myStoreOrder.add(newOrder);
            return true;
        }
        return false;
    }

    public boolean remove(Object obj) {
        if (obj instanceof Order) {
            Order newOrder = (Order) obj;
            if (myStoreOrder.remove(newOrder))
                return true;
        }
        return false;
    }

    /*public void export() throws IOException {
        FileOutputStream output = new FileOutputStream("storeOrders.txt");

        for(Order o : myStoreOrder) {
            String order = "Order number: " + o.getOrderNumber() + "\n";
            for(Pizza p : o.getOrder()) {
                order += CurrentOrderController.toString(p);
                order += "\n";
            }
            //order = order + "Subtotal: $" + df.format(CurrentOrderController.subtotalPrice(o)) +
                    //", Tax: $" + df.format(CurrentOrderController.taxPrice(o)) +
                    //"\nTotal price: $" + df.format(CurrentOrderController.totalPrice(o)) + "\n\n";
            //byte[] textBytes = order.getBytes();
            //output.write(textBytes);
        }
    }*/

    public ArrayList<Order> getStoreOrder() {
        return this.myStoreOrder;
    }

}
