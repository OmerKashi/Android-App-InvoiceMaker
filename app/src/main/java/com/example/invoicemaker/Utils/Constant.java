package com.example.invoicemaker.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.invoicemaker.Model.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Constant {

    public static void saveCartData(Context context, ArrayList<Item> cartArrayList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // creating a new variable for gson.
        Gson gson = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(cartArrayList);
        prefs.edit().putString("myCartData", json).commit();
    }

    public static ArrayList<Item> getCartData(Context context) {
        ArrayList<Item> cartArrayList=new ArrayList<Item>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json =  prefs.getString("myCartData",null);
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        cartArrayList = gson.fromJson(json, type);
        if(cartArrayList==null){
            cartArrayList=new ArrayList<Item>();
        }
        return cartArrayList;
    }

}
