package com.example.mycloset.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mycloset.main.MainActivity;
import com.example.mycloset.MessagingHelper;
import com.example.mycloset.NavHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.models.SecurityHelper;
import com.example.mycloset.models.User;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullNameEt, emailEt, passwordEt;
    private StorePersistenceHelper storePersistenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        storePersistenceHelper = StorePersistenceHelper.getInstance(getApplicationContext());
        fullNameEt = findViewById(R.id.fullName_reg);
        emailEt = findViewById(R.id.email_reg);
        passwordEt = findViewById(R.id.password_reg);
        Button doLoginButton = findViewById(R.id.btn_reg);
        Button moveToLogin = findViewById(R.id.btn_reg_to_signIn);
        // sends user to register screen
        moveToLogin.setOnClickListener((v) -> NavHelper.move(this, LoginActivity.class));
        // sends user to main screen if fields are validated
        doLoginButton.setOnClickListener((v) -> {
            if (isValidFields()) {
                String eName = SecurityHelper.Encrypt(fullNameEt.getText().toString());
                String eEmail = SecurityHelper.Encrypt(emailEt.getText().toString());
                String ePassword = SecurityHelper.Encrypt(passwordEt.getText().toString());

                storePersistenceHelper.saveUser(new User(eName, eEmail, ePassword));
                MessagingHelper.makeSnackBar(this, findViewById(R.id.register_layout), "You may login");
               new Timer().schedule(new TimerTask() {
                   @Override
                   public void run() {
                       finish();
                   }
               },500);
            } else
                MessagingHelper.makeSnackBar(this, findViewById(R.id.register_layout), "Please fill all the fields in order to register");
        });
    }

    /**
     * Checks if fields are valid
     *
     * @return true if fields are valid
     */
    private boolean isValidFields() {
        if (fullNameEt.getText().toString().trim().isEmpty())
            return false;
        if (emailEt.getText().toString().trim().isEmpty())
            return false;
        return !passwordEt.getText().toString().trim().isEmpty();
    }
}
