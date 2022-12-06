package com.example.androidpizzeria;

/**
 * Size enum class holds the list of all available pizza sizes and the cost of each flavor pizza for each size..
 *
 * @author David Ma, Ethan Kwok
 */
public enum Size {

    SMALL(14.99, 13.99, 15.99, 8.99),
    MEDIUM(16.99, 15.99, 17.99, 10.99),
    LARGE(18.99, 17.99, 19.99, 12.99);

    private final double deluxe;
    private final double BBQChicken;
    private final double meatzza;
    private final double buildYourOwn;

    /**
     * Creates a Size object for tracking the prices.
     * @param deluxe double representing the prices for deluxe pizzas.
     * @param BBQChicken double representing the prices for BBQ chicken pizzas.
     * @param meatzza double representing the prices for meatzza pizzas.
     * @param buildYourOwn double representing the prices for build your own pizzas.
     */
    Size(double deluxe, double BBQChicken, double meatzza, double buildYourOwn) {
        this.deluxe = deluxe;
        this.BBQChicken = BBQChicken;
        this.meatzza = meatzza;
        this.buildYourOwn = buildYourOwn;
    }

    /**
     * Getter method for the price of deluxe pizzas.
     * @return the price of the deluxe pizza.
     */
    public double getDeluxe() {
        return this.deluxe;
    }

    /**
     * Getter method for the price of BBQ chicken pizzas.
     * @return the price of the BBQ chicken pizza.
     */
    public double getBBQChicken() {
        return this.BBQChicken;
    }

    /**
     * Getter method for the price of meatzza pizzas.
     * @return the price of the meatzza pizza.
     */
    public double getMeatzza() {
        return this.meatzza;
    }

    /**
     * Getter method for the price of build your own pizzas.
     * @return the price of the build your own pizza.
     */
    public double getBuildYourOwn() {
        return this.buildYourOwn;
    }

}
