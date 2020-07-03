package com.example.bisanat.App.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.bisanat.App.Adapters.OrderAdapter;
import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Order;
import com.example.bisanat.DAL.Entites.OrderLineItem;
import com.example.bisanat.DAL.Entites.Person;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderScreen extends AppCompatActivity {

    MaterialButton btnTo,btnFrom;
    RecyclerView rvOrders;
    ImageButton btnGoBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_orders);

        btnTo = findViewById(R.id.btn_screen_order_to);
        btnFrom = findViewById(R.id.btn_screen_order_from);
        btnGoBack = findViewById(R.id.ibtn_order_screen_go_back);
        rvOrders = findViewById(R.id.rv_screen_order_orders);

        btnGoBack.setOnClickListener(v -> {
            super.onBackPressed();
        });



        btnTo.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        btnFrom.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Order o = new Order();
        o.OrderFrom = HttpServices._currentUser.Id;
        Call<List<Order>> orders = HttpServices.orderService.GetFilter(o);
        orders.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null && response.body().size() > 0){
                        OrderAdapter adapter = new OrderAdapter(OrderScreen.this,response.body());
                        LinearLayoutManager manager = new LinearLayoutManager(OrderScreen.this);
                        rvOrders.setLayoutManager(manager);
                        rvOrders.setAdapter(adapter);
                    }else{
                        Utility.ShowToast(OrderScreen.this,"Hiç Siparişiniz Yok!");
                    }
                }else{
                    Utility.ShowToast(OrderScreen.this,"Siparişleriniz getirilemedi!");
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Utility.ShowToast(OrderScreen.this,"Sunucu Hatası!");
            }
        });

        btnFrom.setOnClickListener(v -> {
            rvOrders.setAdapter(null);
            btnTo.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            btnFrom.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            Order o1 = new Order();
            o1.OrderFrom = HttpServices._currentUser.Id;
            Call<List<Order>> orders1 = HttpServices.orderService.GetFilter(o1);
            orders1.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if(response.isSuccessful()){
                        if(response.body() != null &&  response.body().size() > 0){
                            OrderAdapter adapter = new OrderAdapter(OrderScreen.this,response.body());
                            LinearLayoutManager manager = new LinearLayoutManager(OrderScreen.this);
                            rvOrders.setLayoutManager(manager);
                            rvOrders.setAdapter(adapter);
                        }else{
                            Utility.ShowToast(OrderScreen.this,"Hiç Siparişiniz Yok!");
                        }
                    }else{
                        Utility.ShowToast(OrderScreen.this,"Hiç Siparişiniz Yok!");
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Utility.ShowToast(OrderScreen.this,"Sunucu Hatası!");
                }
            });
        });


        btnTo.setOnClickListener(v -> {
            rvOrders.setAdapter(null);
            btnTo.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btnFrom.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            Order o1 = new Order();
            o1.OrderTo = HttpServices._currentUser.Id;
            Call<List<Order>> orders1 = HttpServices.orderService.GetFilter(o1);
            orders1.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if(response.isSuccessful()){
                        if(response.body() != null && response.body().size() > 0){
                            OrderAdapter adapter = new OrderAdapter(OrderScreen.this,response.body());
                            LinearLayoutManager manager = new LinearLayoutManager(OrderScreen.this);
                            rvOrders.setLayoutManager(manager);
                            rvOrders.setAdapter(adapter);
                        }else{
                            Utility.ShowToast(OrderScreen.this,"Hiç Siparişiniz Yok!");
                        }
                    }else{
                        Utility.ShowToast(OrderScreen.this,"Hiç Siparişiniz Yok!");
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Utility.ShowToast(OrderScreen.this,"Sunucu Hatası!");
                }
            });
        });
    }
}
