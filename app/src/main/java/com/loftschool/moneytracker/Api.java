package com.loftschool.moneytracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("/items")
    Call<List<Item>> getItems(String type);

}
