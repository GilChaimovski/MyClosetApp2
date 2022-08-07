package com.example.mycloset.auth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mycloset.main.MainActivity;
import com.example.mycloset.MessagingHelper;
import com.example.mycloset.NavHelper;
import com.example.mycloset.R;
import com.example.mycloset.StorePersistenceHelper;
import com.example.mycloset.models.SecurityHelper;
import com.example.mycloset.models.User;
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

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 5;
    private EditText emailEt, passwordEt;

    private StorePersistenceHelper storePersistenceHelper;
    private GoogleSignInClient signInClient;
    private FirebaseAuth mAuth;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        signInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        Button btnGoogle = findViewById(R.id.btn_google);
        btnGoogle.setOnClickListener((v) -> {
            Intent signInIntent = signInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    NavHelper.move(LoginActivity.this, MainActivity.class);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            catch (Exception e) {
                Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
            }
        }
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

        String dEmail = SecurityHelper.Decrypt(pastUser.getEmail());
        String dPassword = SecurityHelper.Decrypt(pastUser.getPw());

        if (pastUser == null)
            return false;
        return dEmail.equals(emailEt.getText().toString()) && dPassword.equals(passwordEt.getText().toString());
    }
}
