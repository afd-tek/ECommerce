package com.example.bisanat.App.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bisanat.App.Adapters.ProductAdapter;
import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.OrderLineItem;
import com.example.bisanat.DAL.Entites.Person;
import com.example.bisanat.DAL.Entites.Product;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.R;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends AppCompatActivity {

    RecyclerView rvProducts;
    Toolbar tbMain;
    ImageButton btnCart, btnLogout, btnOrders;
    EditText etSearch;
    LovelyProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);

        tbMain = findViewById(R.id.tb_main);
        btnCart = tbMain.findViewById(R.id.btn_toolbar_main_cart);
        btnLogout = tbMain.findViewById(R.id.btn_toolbar_main_logout);
        btnOrders = tbMain.findViewById(R.id.btn_toolbar_main_orders);
        rvProducts = findViewById(R.id.rv_main_products);
        etSearch = findViewById(R.id.et_main_search);

        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!TextUtils.isEmpty(etSearch.getText())) {
                    progressDialog = new LovelyProgressDialog(this)
                            .setIcon(R.drawable.ic_sync_light)
                            .setMessage("Sonuçlar filtreleniyor...")
                            .setTopColorRes(R.color.colorPrimaryDark);
                    progressDialog.show();
                    Product p = new Product();
                    p.Name = etSearch.getText().toString();
                    if(Utility._cartItems != null && Utility._cartItems.size() > 0){
                        for (Product pid : Utility._products){
                            if(pid.Id == Utility._cartItems.get(0).ProductId){
                                p.PersonId = pid.PersonId;
                            }
                        }
                    }
                    Call<List<Product>> prFiltered = HttpServices.productService.GetFilter(p);
                    prFiltered.enqueue(new Callback<List<Product>>() {
                        @Override
                        public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                            if (response.isSuccessful()) {
                                if (response.body() == null || response.body().size() <= 0) {
                                    Utility.ShowToast(MainScreen.this, "Aradığınız özelliklere ait ürün bulunamadı!");
                                } else {
                                    ProductAdapter adapter = new ProductAdapter(MainScreen.this, response.body());
                                    StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                                    rvProducts.setLayoutManager(manager);
                                    rvProducts.setAdapter(adapter);
                                }
                            } else {
                                Utility.ShowToast(MainScreen.this, response.message());
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<List<Product>> call, Throwable t) {
                            Utility.ShowToast(MainScreen.this, t.getMessage());
                            progressDialog.dismiss();
                        }
                    });
                }else{
                    ProductAdapter adapter = new ProductAdapter(MainScreen.this,Utility._products);
                    StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    rvProducts.setLayoutManager(manager);
                    rvProducts.setAdapter(adapter);
                }
            }
            return true;
        });

        btnCart.setOnClickListener(cart -> {
            startActivity(new Intent(MainScreen.this,CartScreen.class));
        });

        btnOrders.setOnClickListener(orders -> {
            startActivity(new Intent(MainScreen.this,OrderScreen.class));
        });

        btnLogout.setOnClickListener(logout -> {
            new LovelyStandardDialog(MainScreen.this)
                    .setIcon(R.drawable.ic_info_light)
                    .setMessage("Çıkış yapmak istediğinizden emin misiniz?")
                    .setPositiveButtonColorRes(R.color.colorPrimaryDark)
                    .setPositiveButton("Evet", positive -> {
                        Utility.ClearSavedUser(MainScreen.this);
                        startActivity(new Intent(MainScreen.this, LoginScreen.class));
                    })
                    .setNegativeButtonColorRes(R.color.colorPrimaryDark)
                    .setNegativeButton("Vazgeç", null)
                    .setTopColorRes(R.color.colorPrimaryDark).show();
        });

        Call<List<Product>> product = HttpServices.productService.GetAll();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },1000);

        product.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null || response.body().size() <= 0) {
                        Utility.ShowToast(MainScreen.this, "Aradığınız özelliklere ait ürün bulunamadı!");
                    } else {
                        Utility._products = response.body();
                        ProductAdapter adapter = new ProductAdapter(MainScreen.this, response.body());
                        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        rvProducts.setLayoutManager(manager);
                        rvProducts.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Utility.ShowToast(MainScreen.this, t.getMessage());
            }
        });
    }
}
