package com.example.mycloset.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mycloset.MessagingHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.models.MyClosetItem;

public class AddItemActivity extends AppCompatActivity {

    private String uriString;
    EditText itemNameEt, itemCategoryEt, itemPriceEt;
    ImageView itemImageView;
    Button addItemButton;
    StorePersistenceHelper storePersistenceHelper;

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
        addItemButton = findViewById(R.id.btn_add_to_store);
        addItemButton.setOnClickListener(view -> {
            if (isValidFields()) {
                // saves a new item to store
                storePersistenceHelper.saveItem(new MyClosetItem(Double.parseDouble(itemPriceEt.getText().toString()), itemNameEt.getText().toString(), itemCategoryEt.getText().toString(), uriString));
                finish();
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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (data != null) {
                Uri u = data.getData();
                itemImageView.setImageURI(u);
                // Create file path for selected image
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(u,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                System.out.println(cursor.getString(columnIndex));
                this.uriString = cursor.getString(columnIndex);
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
        if (uriString == null || uriString.trim().isEmpty())
            return false;
        return !itemPriceEt.getText().toString().trim().isEmpty();
    }
}
