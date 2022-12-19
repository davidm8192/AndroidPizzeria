package com.example.androidpizzeria;

/**
 * Recycler View Items class that creates the boxes of each recycler view.
 * 
 * @author David Ma, Ethan Kwok
 */
public class AllToppings {
    private String mText;

    /**
     * Constructor creating a selection box for the recycler view.
     * @param text Text to be placed in the doc.
     */
    public AllToppings(String text) {
        mText = text;
    }

    /**
     * Getter method for the text in the selected box.
     * @return String representing the text of the AllToppings box.
     */
    public String getText() {
        return mText;
    }
}
