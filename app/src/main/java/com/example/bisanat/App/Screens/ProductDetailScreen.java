package com.example.bisanat.App.Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bisanat.App.Adapters.CommentAdapter;
import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Comment;
import com.example.bisanat.DAL.Entites.OrderLineItem;
import com.example.bisanat.DAL.Entites.Product;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailScreen extends AppCompatActivity {

    Product product;
    ImageView ivProductImage;
    TextView tvProductName,tvProductPrice,tvQuantity;
    EditText etCommentBody;
    RecyclerView rvComments;
    int quantity = 1;
    CommentAdapter adapter;
    List<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_product_detail);
        initializeScreen();
    }

    private void initializeScreen() {
        int id = getIntent().getIntExtra("ProductId",0);
        if(id != 0){
            for (Product p: Utility._products){
                if(p.Id ==id){
                    product = p;
                }
            }
        }


        ivProductImage = findViewById(R.id.iv_product_detail_image);
        tvProductName = findViewById(R.id.tv_product_detail_name);
        tvProductPrice = findViewById(R.id.tv_product_detail_price);
        tvQuantity = findViewById(R.id.tv_product_detail_quantiy);
        rvComments = findViewById(R.id.rv_product_detail_comment);
        etCommentBody = findViewById(R.id.et_product_detail_comment_body);

        byte[] decodedString = Base64.decode(product.Image,Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ivProductImage.setImageBitmap(decodedByte);
        tvProductName.setText(product.Name);
        tvProductPrice.setText(String.format("%.2f ₺",product.Price));
        tvQuantity.setText(String.valueOf(quantity));

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Comment co = new Comment();
        co.ProductId = product.Id;
        Call<List<Comment>> listCall = HttpServices.commentService.GetFilter(co);
        listCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.isSuccessful()){
                    if(response.body().size() > 0){
                        comments = response.body();
                        adapter = new CommentAdapter(ProductDetailScreen.this,response.body());
                        LinearLayoutManager manager = new LinearLayoutManager(ProductDetailScreen.this);
                        rvComments.setLayoutManager(manager);
                        rvComments.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    public void onClickProductDetail(View v){
        switch (v.getId()){
            case R.id.ibtn_product_detail_decrease_quantity:
                if(quantity == 1){
                    Utility.ShowToast(this,"Ürün miktarı 1'den küçük olamaz!");
                }else{
                    quantity--;
                    tvQuantity.setText(String.valueOf(quantity));
                }
                break;
            case R.id.ibtn_product_detail_increase_quantity:
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
                break;
            case R.id.ibtn_product_detail_go_back:
                super.onBackPressed();
                break;
            case R.id.btn_product_detail_add_to_cart:
                OrderLineItem o = new OrderLineItem();
                o.ProductId = product.Id;
                o.Quantity = quantity;
                o.SubTotal = quantity * product.Price;
                Utility._cartItems.add(o);
                Utility.ShowToast(this,"Ürün Sepetinize Eklendi!");
                break;
            case R.id.btn_product_detail_send_comment:
                sendComment();
                break;
        }
    }

    private void sendComment() {
        Comment comment = new Comment();
        comment.ProductId = product.Id;
        comment.Message = etCommentBody.getText().toString();
        comment.PersonId = HttpServices._currentUser.Id;
        comment.Title = HttpServices._currentUser.FullName;
        comment.SendedAt = new Date();
        Call<Comment> commentCall = HttpServices.commentService.Add(comment);
        commentCall.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.isSuccessful()){
                    if(response.body() != null && response.body().Id > 0){
                        if(comments != null){
                            comments.add(response.body());
                            adapter.notifyDataSetChanged();
                        }else{
                            comments = new ArrayList<>();
                            comments.add(response.body());
                            adapter = new CommentAdapter(ProductDetailScreen.this,comments);
                            LinearLayoutManager manager = new LinearLayoutManager(ProductDetailScreen.this);
                            rvComments.setLayoutManager(manager);
                            rvComments.setAdapter(adapter);
                        }
                        Utility.ShowToast(ProductDetailScreen.this,"Yorumunuz başarı ile gönderildi!");
                        etCommentBody.setText("");
                    }else{
                        Utility.ShowToast(ProductDetailScreen.this,"Yorumunuz kaydedilemedi!");
                    }

                }else{
                    Utility.ShowToast(ProductDetailScreen.this,"Yorumunuz gönderilemedi!");
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Utility.ShowToast(ProductDetailScreen.this,"Şu anda bu işlem gerçekleştirilemiyor!");
            }
        });
    }
}
