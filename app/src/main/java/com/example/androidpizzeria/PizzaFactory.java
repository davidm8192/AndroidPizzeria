package com.example.androidpizzeria;

/**
 * Interface class for implementing the abstract factory design pattern.
 *
 * @author David Ma, Ethan Kwok
 */
public interface PizzaFactory {
    Pizza createDeluxe();
    Pizza createMeatzza();
    Pizza createBBQChicken();
    Pizza createBuildYourOwn();

}
