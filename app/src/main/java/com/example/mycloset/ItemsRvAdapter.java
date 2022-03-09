package com.example.mycloset;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloset.models.MyClosetItem;
import com.example.mycloset.models.OnClosetItemClickListener;
import com.google.gson.Gson;

import java.util.List;

/**
 * This class is the adapter for the items recyclerView
 */
public class ItemsRvAdapter extends RecyclerView.Adapter<ItemsRvAdapter.ItemsViewHolder> {

    private List<MyClosetItem> itemList;
    private Gson g = new Gson();
    private OnClosetItemClickListener listener;
    boolean cart;

    public ItemsRvAdapter(List<MyClosetItem> itemList, OnClosetItemClickListener listener,boolean cart) {
        this.itemList = itemList;
        this.listener = listener;
        this.cart = cart;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.closet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        MyClosetItem item = itemList.get(position);
        if(cart) {
            holder.removeFromCart.setVisibility(View.VISIBLE);
            holder.removeFromCart.setOnClickListener((View view) ->  {
                StorePersistenceHelper.getInstance(view.getContext().getApplicationContext()).removeCheckoutItem(item);
                itemList.remove(item);
                notifyDataSetChanged();
            });
        }
        holder.itemIv.setImageBitmap(BitmapFactory.decodeFile(item.getImage()));
        holder.itemNameTv.setText(item.getName());
        holder.showMore.setOnClickListener((v) -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemsViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIv;
        TextView itemNameTv;
        TextView removeFromCart;
        Button showMore;

        public ItemsViewHolder(View v) {
            super(v);
            removeFromCart = v.findViewById(R.id.remove_from_cart);
            itemNameTv = v.findViewById(R.id.item_name_tv);
            itemIv = v.findViewById(R.id.item_image_view);
            showMore = v.findViewById(R.id.show_more_btn);
        }
    }
}
