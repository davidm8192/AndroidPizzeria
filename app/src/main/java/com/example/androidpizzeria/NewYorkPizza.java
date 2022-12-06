package com.example.androidpizzeria;

/**
 * Class that utilizes the abstract factory design factory to create new instances of the subclasses of Pizza.
 *
 * @author David Ma, Ethan Kwok
 */
public class NewYorkPizza implements PizzaFactory {

    /**
     * Creates a deluxe pizza with the appropriate toppings and crust.
     * @return Deluxe concrete subclass of the Pizza object
     */
    public Pizza createDeluxe() {
        Pizza pizza = new Deluxe();
        pizza.add(Toppings.valueOf("SAUSAGE"));
        pizza.add(Toppings.valueOf("PEPPERONI"));
        pizza.add(Toppings.valueOf("GREEN_PEPPER"));
        pizza.add(Toppings.valueOf("ONION"));
        pizza.add(Toppings.valueOf("MUSHROOM"));
        pizza.setCrust(Crust.valueOf("BROOKLYN"));
        return pizza;
    }

    /**
     * Creates a meatzza pizza with the appropriate toppings and crust.
     * @return Meatzza concrete subclass of the Pizza object
     */
    public Pizza createMeatzza() {
        Pizza pizza = new Meatzza();
        pizza.add(Toppings.valueOf("SAUSAGE"));
        pizza.add(Toppings.valueOf("PEPPERONI"));
        pizza.add(Toppings.valueOf("BEEF"));
        pizza.add(Toppings.valueOf("HAM"));
        pizza.setCrust(Crust.valueOf("HAND_TOSSED"));
        return pizza;
    }

    /**
     * Creates a BBQ chicken pizza with the appropriate toppings and crust.
     * @return BBQChicken concrete subclass of the Pizza object
     */
    public Pizza createBBQChicken() {
        Pizza pizza = new BBQChicken();
        pizza.add(Toppings.valueOf("BBQ_CHICKEN"));
        pizza.add(Toppings.valueOf("GREEN_PEPPER"));
        pizza.add(Toppings.valueOf("PROVOLONE"));
        pizza.add(Toppings.valueOf("CHEDDAR"));
        pizza.setCrust(Crust.valueOf("THIN"));
        return pizza;
    }

    /**
     * Creates a build your own pizza with the appropriate toppings and crust.
     * @return BuildYourOwn concrete subclass of the Pizza object
     */
    public Pizza createBuildYourOwn() {
        Pizza pizza = new BuildYourOwn();
        pizza.setCrust(Crust.valueOf("HAND_TOSSED"));
        return pizza;
    }

}
