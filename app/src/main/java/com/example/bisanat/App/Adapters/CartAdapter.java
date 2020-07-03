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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<OrderLineItem> items;
    Context context;
    List<Product> products;
    ExtendedFloatingActionButton fabCheckout;

    public CartAdapter(Context context, List<OrderLineItem> items, List<Product> products, ExtendedFloatingActionButton fabCheckout) {
        this.items = items;
        this.context = context;
        this.products = products;
        this.fabCheckout = fabCheckout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.ViewHolder(view);
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
        holder.tvItemQuantity.setText(String.valueOf(items.get(position).Quantity));
        holder.tvSubTotal.setText(String.format("%.2f ₺", items.get(position).SubTotal));
        holder.ibtnIncrease.setOnClickListener(v -> {
            items.get(position).Quantity++;
            items.get(position).SubTotal = items.get(position).Quantity * p.Price;
            notifyItemChanged(position);
        });
        holder.ibtnDecrease.setOnClickListener(v -> {
            items.get(position).Quantity--;
            items.get(position).SubTotal = items.get(position).Quantity * p.Price;
            notifyItemChanged(position);
        });
        double total = 0;
        for(OrderLineItem o : items){
            total += o.SubTotal;
        }
        fabCheckout.setText(String.format("Sipariş Ver - %.2f ₺",total));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvItemQuantity, tvSubTotal;
        ImageButton ibtnDecrease, ibtnIncrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_cart_item_product_image);
            tvProductName = itemView.findViewById(R.id.tv_cart_item_product_name);
            tvItemQuantity = itemView.findViewById(R.id.tv_cart_item_quantiy);
            tvSubTotal = itemView.findViewById(R.id.tv_cart_item_sub_total);
            ibtnDecrease = itemView.findViewById(R.id.ibtn_item_cart_decrease_quantity);
            ibtnIncrease = itemView.findViewById(R.id.ibtn_item_cart_increase_quantity);
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

    public void Clear() {
        int size = getItemCount();
        items = new ArrayList<>();
        Utility._cartItems = new ArrayList<>();
        notifyItemRangeRemoved(0, size);
        fabCheckout.setEnabled(false);
        fabCheckout.setText("Sepetiniz Boş");
    }
}
