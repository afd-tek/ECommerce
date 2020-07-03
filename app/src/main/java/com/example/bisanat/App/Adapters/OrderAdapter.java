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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bisanat.App.Screens.OrderDetailScreen;
import com.example.bisanat.App.Utility;
import com.example.bisanat.DAL.Entites.Order;
import com.example.bisanat.DAL.Entites.Person;
import com.example.bisanat.DAL.Network.HttpServices;
import com.example.bisanat.R;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    private List<Order> _Orders;

    public OrderAdapter(Context context, List<Order> _Orders) {
        this.context = context;
        this._Orders = _Orders;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        holder.container.setOnClickListener(v -> {
            Intent i = new Intent(context,OrderDetailScreen.class);
            i.putExtra("OrderId",_Orders.get(position).Id);
            context.startActivity(i);
        });

        if (_Orders.get(position).OrderTo == HttpServices._currentUser.Id) {
            Person p = find(_Orders.get(position).OrderFrom);
            holder.tvPersonName.setText(new StringBuilder().append("Kimden : ").append(p.FullName));
            String date = new SimpleDateFormat("dd/MM/yy HH:mm").format(_Orders.get(position).OrderedAt);
            holder.tvDate.setText(date);
            if (_Orders.get(position).OrderedAt.equals(_Orders.get(position).DeliveredAt))
                holder.tvState.setText("Teslim Edilmedi!");
            else holder.tvState.setText("Teslim Edildi!");
            holder.tvTotal.setText(String.format("%.2f ₺", _Orders.get(position).TotalPrice));


        } else {
            Person p = find(_Orders.get(position).OrderTo);
            holder.tvPersonName.setText(new StringBuilder().append("Kime : ").append(p.FullName));
            String date = new SimpleDateFormat("dd/MM/yy HH:mm").format(_Orders.get(position).OrderedAt);
            holder.tvDate.setText(date);

            if (_Orders.get(position).OrderedAt.equals(_Orders.get(position).DeliveredAt))
                holder.tvState.setText("Teslim Edilmedi!");
            else holder.tvState.setText("Teslim Edildi!");
            holder.tvTotal.setText(String.format("%.2f ₺", _Orders.get(position).TotalPrice));


        }

    }

    private Person find(int id) {
        for (Person p : Utility._people){
            if(p.Id == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return _Orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPersonName, tvDate, tvState, tvTotal;
        CardView container;
        public ViewHolder(View itemView) {
            super(itemView);
            tvPersonName = itemView.findViewById(R.id.tv_order_item_person);
            tvDate = itemView.findViewById(R.id.tv_order_item_ordered);
            tvState = itemView.findViewById(R.id.tv_order_item_state);
            tvTotal = itemView.findViewById(R.id.tv_order_item_total);
            container = itemView.findViewById(R.id.cv_order_item_container);

        }
    }
}