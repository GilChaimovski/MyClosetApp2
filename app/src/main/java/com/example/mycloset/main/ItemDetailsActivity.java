package com.example.mycloset.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycloset.FileUtils;
import com.example.mycloset.MessagingHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.models.MyClosetItem;
import com.example.mycloset.models.SecurityHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

public class ItemDetailsActivity extends AppCompatActivity {

    private StorePersistenceHelper storePersistenceHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        storePersistenceHelper = StorePersistenceHelper.getInstance(getApplicationContext());
        ImageView itemIv = findViewById(R.id.item_image_details);
        TextView itemNameTv = findViewById(R.id.item_name_details);
        TextView itemPriceTv =findViewById(R.id.item_price_details);
        TextView itemCategoryTv = findViewById(R.id.item_category_details);
        Intent i = getIntent();
        Gson g = new Gson();
        MyClosetItem item = g.fromJson(i.getExtras().getString("ClosetItem"), MyClosetItem.class);

        String dName = SecurityHelper.Decrypt(item.getName());
        String dCategory = SecurityHelper.Decrypt(item.getCategory());

        itemCategoryTv.setText(dCategory);
        itemPriceTv.setText(String.valueOf(item.getPrice()));
        itemNameTv.setText(dName);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + item.getImage() + ".jpg");
        storageReference.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                itemIv.setImageBitmap(bitmap);
            }
        });

        findViewById(R.id.btn_add_to_cart).setOnClickListener(view ->  {
            if(storePersistenceHelper.getAllCheckoutItems().contains(item))
            {
                MessagingHelper.makeSnackBar(this,findViewById(R.id.details_layout),dName + " is already in the cart");
            }else {
                storePersistenceHelper.saveCheckoutItem(item);
                MessagingHelper.makeSnackBar(this, findViewById(R.id.details_layout), "Added " + dName + " to cart");
            }
        });

    }
}
