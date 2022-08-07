package com.example.mycloset.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mycloset.MessagingHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.models.MyClosetItem;
import com.example.mycloset.models.SecurityHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class AddItemActivity extends AppCompatActivity {

    Bitmap imageBitmap;

    EditText itemNameEt, itemCategoryEt, itemPriceEt;
    ImageView itemImageView, itemImageView2;
    Button addItemButton;
    StorePersistenceHelper storePersistenceHelper;

    StorageReference firebaseStorage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        findViewById(R.id.back_add).setOnClickListener((v) -> finish());
        storePersistenceHelper = StorePersistenceHelper.getInstance(getApplicationContext());
        itemNameEt = findViewById(R.id.item_name_add);
        itemCategoryEt = findViewById(R.id.item_category_add);
        itemPriceEt = findViewById(R.id.item_price_add);
        itemImageView = findViewById(R.id.item_add_image);
        itemImageView2 = findViewById(R.id.item_add_image2);
        addItemButton = findViewById(R.id.btn_add_to_store);

        firebaseStorage = FirebaseStorage.getInstance().getReference();

        addItemButton.setOnClickListener(view -> {
            if (isValidFields()) {
                // saves a new item to store

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                String randomUID = UUID.randomUUID().toString();

                UploadTask uploadTask = firebaseStorage.child("images/" + randomUID + ".jpg").putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String eName = SecurityHelper.Encrypt(itemNameEt.getText().toString());
                        String eCategory = SecurityHelper.Encrypt(itemCategoryEt.getText().toString());

                        storePersistenceHelper.saveItem(new MyClosetItem(Double.parseDouble(itemPriceEt.getText().toString()), eName, eCategory, randomUID));
                        finish();
                    }
                });
            } else
                MessagingHelper.makeSnackBar(this, findViewById(R.id.add_layout), "Please fill all the fields and select image in order to add item");

        });

        itemImageView.setOnClickListener((v) -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 0);
            }
        });

        itemImageView2.setOnClickListener((v) -> {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri u = null;

        if (requestCode == 0) {
            if (data != null) {
                u = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), u);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                itemImageView.setImageBitmap(imageBitmap);
                itemImageView2.setImageResource(R.drawable.camera);
            }
        }
        else if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");

                itemImageView2.setImageBitmap(imageBitmap);
                itemImageView.setImageResource(R.drawable.placeholder);
            }
        }
    }

    /**
     * Checks if fields are valid
     *
     * @return true if fields are valid
     */
    private boolean isValidFields() {

        if (itemNameEt.getText().toString().trim().isEmpty())
            return false;
        if (itemCategoryEt.getText().toString().trim().isEmpty())
            return false;
        return !itemPriceEt.getText().toString().trim().isEmpty();
    }
}
