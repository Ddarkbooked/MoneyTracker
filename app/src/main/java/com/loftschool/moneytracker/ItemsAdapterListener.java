package com.loftschool.moneytracker;

public interface ItemsAdapterListener { // Слушает нажатия на наши айтемы
    void onItemClick(Item item, int position);
    void onItemLongClick(Item item, int position);
}
