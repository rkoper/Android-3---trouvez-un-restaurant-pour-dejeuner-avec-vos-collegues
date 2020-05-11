package com.m.sofiane.go4lunch.activity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.m.sofiane.go4lunch.R;

import java.util.Arrays;
import java.util.List;


public class loginactivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 777;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        createSignInIntent();
    }

        public void createSignInIntent () {
            List<AuthUI.IdpConfig> mProviders = Arrays.asList(
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.TwitterBuilder().build(),
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(mProviders)
                            .setTheme(R.style.MyTheme)
                            .setLogo(R.drawable.logogl)
                            .build(), MY_REQUEST_CODE);
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse mResponse = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                launchMainActivity();

            } else {
                Toast.makeText(this, R.string.Error_to_login, Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            }
        }
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, mainactivity.class);
        startActivity(intent);
    }

}