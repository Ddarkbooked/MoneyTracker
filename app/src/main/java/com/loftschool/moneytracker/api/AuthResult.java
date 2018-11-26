package com.loftschool.moneytracker.api;

import com.google.gson.annotations.SerializedName;

public class AuthResult {
    public String status;
    public int id;
    @SerializedName("auth_token") // Он сможет сопоставить что строчка ниже придет и он сможет засунуть токен в эту строчку
    public String token;
}
