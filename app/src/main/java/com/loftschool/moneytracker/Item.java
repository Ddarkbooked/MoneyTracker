package com.loftschool.moneytracker;

import android.content.res.Resources;
import android.widget.TextView;

public class Item {

    private final String title;
    private final int price;

    public Item(String title, int price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }


    public int getPrice() {
        return price;
    }

}
