package com.example.bisanat.App.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bisanat.App.Screens.ProductDetailScreen;
import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Product;
import com.example.bisanat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    private List<Product> _products;

    public ProductAdapter(Context context, List<Product> _products) {
        this.context = context;
        this._products = _products;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        byte[] decodedString = Base64.decode(_products.get(position).Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.ivProductImage.setImageBitmap(decodedByte);
        holder.tvProductName.setText(_products.get(position).Name);
        holder.tvProductPrice.setText(String.format("%.2f", _products.get(position).Price));
        holder.ivProductImage.setOnClickListener(v -> {
            Intent i = new Intent(context, ProductDetailScreen.class);
            i.putExtra("ProductId",_products.get(position).Id);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return _products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_item_product_image);
            tvProductName = itemView.findViewById(R.id.tv_item_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_item_product_price);


        }
    }
}
