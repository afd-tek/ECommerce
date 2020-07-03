package com.example.bisanat.App;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.widget.Toast;

import com.example.bisanat.App.Screens.LoginScreen;
import com.example.bisanat.DAL.Entites.OrderLineItem;
import com.example.bisanat.DAL.Entites.Person;
import com.example.bisanat.DAL.Entites.Product;

import java.util.ArrayList;
import java.util.List;

public class Utility {
    private static final String UserTAG = "UserCredit";
    public static List<Product> _products;
    public static List<OrderLineItem> _cartItems = new ArrayList<>();
    public static List<Person> _people = new ArrayList<>();

    public static Person GetSavedUser(Context activity){
        SharedPreferences sharedPref = activity.getSharedPreferences(UserTAG,Context.MODE_PRIVATE);
        int keepLogged = sharedPref.getInt("keepLogged",0);
        if(keepLogged == 0) return null;
        Person p = new Person();
        p.Email = sharedPref.getString("LoginEmail","");
        p.Password = sharedPref.getString("LoginPassword","");
        p.DeviceToken = GetDeviceToken(activity.getApplicationContext());
        return p;
    }

    public static void SaveUser(Context activity,Person p){
        SharedPreferences sharedPref = activity.getSharedPreferences(UserTAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("keepLogged",1);
        editor.putString("LoginEmail",p.Email);
        editor.putString("LoginPassword",p.Password);
        editor.apply();
    }

    public static String GetDeviceToken(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static void ClearSavedUser(Context activity){
        SharedPreferences sharedPref = activity.getSharedPreferences(UserTAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    public static void ShowToast(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
