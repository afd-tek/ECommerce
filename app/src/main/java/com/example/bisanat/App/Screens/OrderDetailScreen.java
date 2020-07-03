package com.example.bisanat.App.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.bisanat.App.Adapters.OrderDetailAdapter;
import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Order;
import com.example.bisanat.DAL.Entites.OrderLineItem;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailScreen extends AppCompatActivity {

    RecyclerView rvOrderItems;
    ImageButton btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_order_detail);

        rvOrderItems =findViewById(R.id.rv_order_detail_screen_items);
        btnGoBack = findViewById(R.id.ibtn_order_detail_screen_go_back);
        btnGoBack.setOnClickListener(v->{
            super.onBackPressed();
        });

        int id = getIntent().getIntExtra("OrderId",0);
        Order o = new Order();
        o.Id = id;
        Call<Order> orderCall = HttpServices.orderService.Get(id);
        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()){
                    if(response.body() != null && response.body().Id > 0){
                        OrderLineItem o = new OrderLineItem();
                        o.OrderId = response.body().Id;
                        Call<List<OrderLineItem>> itemCall = HttpServices.orderLineItemService.GetFilter(o);
                        itemCall.enqueue(new Callback<List<OrderLineItem>>() {
                            @Override
                            public void onResponse(Call<List<OrderLineItem>> call, Response<List<OrderLineItem>> response) {
                                if(response.isSuccessful()){
                                    if(response.body()!=null && response.body().size() > 0){
                                        OrderDetailAdapter adapter = new OrderDetailAdapter(OrderDetailScreen.this,response.body(), Utility._products);
                                        LinearLayoutManager manager = new LinearLayoutManager(OrderDetailScreen.this);
                                        rvOrderItems.setLayoutManager(manager);
                                        rvOrderItems.setAdapter(adapter);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<OrderLineItem>> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }
}
