package com.example.mycloset.auth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mycloset.main.MainActivity;
import com.example.mycloset.MessagingHelper;
import com.example.mycloset.NavHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEt, passwordEt;

    private StorePersistenceHelper storePersistenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storePersistenceHelper = StorePersistenceHelper.getInstance(getApplicationContext());
        setContentView(R.layout.activity_login);
        emailEt = findViewById(R.id.email_signIn);
        passwordEt = findViewById(R.id.password_signIn);
        Button doLoginButton = findViewById(R.id.btn_signIn);
        Button moveToRegister = findViewById(R.id.btn_signIn_to_reg);
        // sends user to register screen
        moveToRegister.setOnClickListener((v) -> NavHelper.move(this, RegisterActivity.class));
        // sends user to main screen if fields are validated
        doLoginButton.setOnClickListener((v) -> {
            if (isValidFields())
                if (isUserValidAndExists()) {
                    MessagingHelper.makeSnackBar(this,findViewById(R.id.login_layout),"Welcome, " + storePersistenceHelper.getUserAndCache().getFullName());
                    NavHelper.move(this, MainActivity.class);
                    finish();
                } else
                    MessagingHelper.makeSnackBar(this, findViewById(R.id.login_layout), "Incorrect Credentials");
            else
                MessagingHelper.makeSnackBar(this, findViewById(R.id.login_layout), "Please fill all the fields in order to login");
        });
    }

    /**
     * Checks if fields are valid
     * @return true if fields are valid
     */
    private boolean isValidFields() {
        if (emailEt.getText().toString().trim().isEmpty())
            return false;
        return !passwordEt.getText().toString().trim().isEmpty();
    }
    /**
     * Checks if user exists and data is valid
     * @return true if user exists and data is valid
     */
    private boolean isUserValidAndExists() {
        User pastUser = storePersistenceHelper.getUserAndCache();
        if (pastUser == null)
            return false;
        return pastUser.getEmail().equals(emailEt.getText().toString()) && pastUser.getPw().equals(passwordEt.getText().toString());
    }
}
