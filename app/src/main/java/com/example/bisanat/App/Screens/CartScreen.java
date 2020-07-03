package com.example.bisanat.App.Screens;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bisanat.App.Adapters.CartAdapter;
import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Order;
import com.example.bisanat.DAL.Entites.OrderLineItem;
import com.example.bisanat.DAL.Entites.Product;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartScreen extends AppCompatActivity {

    RecyclerView rvItems;
    CartAdapter adapter;
    ExtendedFloatingActionButton fabCheckout;
    ImageButton ibtnClear;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_cart);

        ibtnClear = findViewById(R.id.ibtn_cart_screen_clear);
        fabCheckout = findViewById(R.id.fab_cart_screen_checkout);
        rvItems = findViewById(R.id.rv_cart_screen_items);
        if(Utility._cartItems != null && Utility._cartItems.size() > 0){
            ibtnClear.setVisibility(View.VISIBLE);
            adapter = new CartAdapter(this, Utility._cartItems, Utility._products,fabCheckout);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rvItems.setLayoutManager(manager);
            rvItems.setAdapter(adapter);
        }else{
            fabCheckout.setEnabled(false);
            fabCheckout.setText("Sepetiniz Boş!");
            ibtnClear.setVisibility(View.INVISIBLE);
        }

        fabCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order o = new Order();
                o.OrderFrom = HttpServices._currentUser.Id;
                o.OrderedAt = new Date();
                o.DeliveredAt = new Date();
                for(Product p : Utility._products){
                    if(p.Id == Utility._cartItems.get(0).ProductId){
                        o.OrderTo = p.PersonId;
                    }
                }
                double total = 0;
                for(OrderLineItem i : Utility._cartItems){
                    total+=i.SubTotal;
                }
                o.TotalPrice = total;
                Call<Order> give = HttpServices.orderService.Add(o);
                give.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null && response.body().Id > 0){
                                Order ordered=response.body();
                                for(OrderLineItem i : Utility._cartItems){
                                    i.OrderId = ordered.Id;
                                }
                                Call<List<OrderLineItem>> addProd = HttpServices.orderLineItemService.BulkInsert(Utility._cartItems);
                                addProd.enqueue(new Callback<List<OrderLineItem>>() {
                                    @Override
                                    public void onResponse(Call<List<OrderLineItem>> call, Response<List<OrderLineItem>> response) {
                                        if(response.isSuccessful()){
                                            if(response.body() != null && response.body().size() == Utility._cartItems.size() && response.body().stream().noneMatch(o -> o.Id == 0)){
                                                adapter.Clear();
                                                Utility.ShowToast(CartScreen.this,"Siparişiniz başarıyla alındı!");
                                            }else{
                                                Utility.ShowToast(CartScreen.this,"Siparişiniz detayı hatalı!");
                                            }
                                        }else{
                                            Utility.ShowToast(CartScreen.this,"Siparişiniz oluşturulamadı!");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<OrderLineItem>> call, Throwable t) {
                                        Utility.ShowToast(CartScreen.this,"Sunucu hatası!");
                                    }
                                });
                            }
                            else{
                                Utility.ShowToast(CartScreen.this,"Sipariş kaydı hatalı!");
                            }
                        }else{
                            Utility.ShowToast(CartScreen.this,"Sipariş oluşturma işlemi başarısız!" + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Utility.ShowToast(CartScreen.this,"Sunucu hatası!");
                    }
                });
            }
        });
    }

    public void onClickCart(View v){
        switch (v.getId()){
            case R.id.ibtn_cart_screen_go_back:
                super.onBackPressed();
                break;
            case R.id.ibtn_cart_screen_clear:
                Utility._cartItems.size();
                adapter.Clear();
                break;
        }
    }
}
