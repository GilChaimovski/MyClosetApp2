package com.example.mycloset.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mycloset.ItemsRvAdapter;
import com.example.mycloset.NavHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.auth.LoginActivity;
import com.example.mycloset.models.MyClosetItem;
import com.example.mycloset.models.OnClosetItemClickListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rvItems;
    private ItemsRvAdapter rvAdapter;
    private StorePersistenceHelper storePersistenceHelper;
    private GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);
        storePersistenceHelper = StorePersistenceHelper.getInstance(getApplicationContext());
        rvItems = findViewById(R.id.rvItems);
        findViewById(R.id.button_to_add).setOnClickListener((v) -> NavHelper.move(this,AddItemActivity.class));
        rvAdapter = new ItemsRvAdapter(new ArrayList<>(storePersistenceHelper.getAllItems()), item -> {
            Bundle b = new Bundle();
            b.putString("ClosetItem", new Gson().toJson(item));
            NavHelper.move(this, ItemDetailsActivity.class, b);
        },false);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(rvAdapter);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.logout_menu:
                FirebaseAuth.getInstance().signOut();
                signInClient.signOut();
                NavHelper.move(this, LoginActivity.class);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
                break;
            case R.id.cart_menu:
                NavHelper.move(this,CartActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        rvAdapter = new ItemsRvAdapter(new ArrayList<>(storePersistenceHelper.getAllItems()), item -> {
            Bundle b = new Bundle();
            b.putString("ClosetItem", new Gson().toJson(item));
            NavHelper.move(this, ItemDetailsActivity.class, b);
        },false);
        rvItems.setAdapter(rvAdapter);
    }
}