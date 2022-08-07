package com.example.mycloset.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mycloset.ItemsRvAdapter;
import com.example.mycloset.NavHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.auth.LoginActivity;
import com.example.mycloset.models.MyClosetItem;
import com.example.mycloset.models.OnClosetItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvItems;
    private ItemsRvAdapter rvAdapter;
    private StorePersistenceHelper storePersistenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        storePersistenceHelper = StorePersistenceHelper.getInstance(getApplicationContext());
        rvItems = findViewById(R.id.rvItems_checkout);
        findViewById(R.id.back_cart).setOnClickListener((v) -> finish());
        rvAdapter = new ItemsRvAdapter(new ArrayList<>(storePersistenceHelper.getAllCheckoutItems()), item -> {
            Bundle b = new Bundle();
            b.putString("ClosetItem", new Gson().toJson(item));
            NavHelper.move(this, ItemDetailsActivity.class, b);
        },true);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(rvAdapter);
    }
}