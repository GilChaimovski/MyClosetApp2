package com.example.mycloset.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycloset.FileUtils;
import com.example.mycloset.MessagingHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.models.MyClosetItem;
import com.google.gson.Gson;

public class ItemDetailsActivity extends AppCompatActivity {


    private StorePersistenceHelper storePersistenceHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        findViewById(R.id.back_details).setOnClickListener((v) -> finish());
        storePersistenceHelper = StorePersistenceHelper.getInstance(getApplicationContext());
        ImageView itemIv = findViewById(R.id.item_image_details);
        TextView itemNameTv = findViewById(R.id.item_name_details);
        TextView itemPriceTv =findViewById(R.id.item_price_details);
        TextView itemCategoryTv = findViewById(R.id.item_category_details);
        Intent i = getIntent();
        Gson g = new Gson();
        MyClosetItem item = g.fromJson(i.getExtras().getString("ClosetItem"), MyClosetItem.class);
        itemIv.setImageBitmap(BitmapFactory.decodeFile(item.getImage()));
        itemCategoryTv.setText(item.getCategory());
        itemPriceTv.setText(String.valueOf(item.getPrice()));
        itemNameTv.setText(item.getName());

        findViewById(R.id.btn_add_to_cart).setOnClickListener(view ->  {
            if(storePersistenceHelper.getAllCheckoutItems().contains(item))
            {
                MessagingHelper.makeSnackBar(this,findViewById(R.id.details_layout),item.getName() + " is already in the cart");
            }else {
                storePersistenceHelper.saveCheckoutItem(item);
                MessagingHelper.makeSnackBar(this, findViewById(R.id.details_layout), "Added " + item.getName() + " to cart");
            }
        });

    }
}
