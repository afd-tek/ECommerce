package com.example.bisanat.App.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Order;
import com.example.bisanat.DAL.Entites.OrderLineItem;
import com.example.bisanat.DAL.Entites.Product;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.DAL.Repository.ProductDAL;
import com.example.bisanat.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {

    List<OrderLineItem> items;
    Context context;
    List<Product> products;

    public OrderDetailAdapter(Context context, List<OrderLineItem> items, List<Product> products) {
        this.items = items;
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = Find(items.get(position).ProductId);
        if (p == null)
            return;
        byte[] decodedString = Base64.decode(p.Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.ivProductImage.setImageBitmap(decodedByte);
        holder.tvProductName.setText(p.Name);
        holder.tvItemQuantity.setText(new StringBuilder().append("Adet : ").append(items.get(position).Quantity));
        holder.tvSubTotal.setText(String.format("%.2f ₺", items.get(position).SubTotal));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvItemQuantity, tvSubTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_order_detail_item_product_image);
            tvProductName = itemView.findViewById(R.id.tv_corder_detail_item_name);
            tvItemQuantity = itemView.findViewById(R.id.tv_order_detail_item_quantity);
            tvSubTotal = itemView.findViewById(R.id.tv_order_detail_item_sub_total);
        }
    }

    private Product Find(int id) {
        for (Product p : products) {
            if (p.Id == id) {
                return p;
            }
        }
        return null;
    }


}
