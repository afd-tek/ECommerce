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

import androidx.recyclerview.widget.RecyclerView;

import com.example.bisanat.App.Screens.ProductDetailScreen;
import com.example.bisanat.DAL.Entites.Comment;
import com.example.bisanat.DAL.Entites.Product;
import com.example.bisanat.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    private List<Comment> _comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this._comments = comments;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        holder.tvCommentBody.setText(_comments.get(position).Message);
        holder.tvCommentTitle.setText(_comments.get(position).Title);
        String date = new SimpleDateFormat("dd/MM/yy HH:mm").format(_comments.get(position).SendedAt);
        holder.tvSentAt.setText(date);

    }

    @Override
    public int getItemCount() {
        return _comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentBody, tvCommentTitle, tvSentAt;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCommentBody = itemView.findViewById(R.id.tv_item_comment_body);
            tvCommentTitle = itemView.findViewById(R.id.tv_item_comment_user);
            tvSentAt = itemView.findViewById(R.id.tv_item_comment_sent_at);


        }
    }
}