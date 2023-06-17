package com.example.moodmaster.googleFit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.example.moodmaster.R;

public class Huawei_API extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 0x1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_api);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/fitness.activity.read.write"),
                        new Scope("https://www.googleapis.com/auth/fitness.location.read.write"))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_OAUTH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // User signed in successfully.
                // Perform operations with the Fitness API here.
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the sign in request.
                // Possibly handle this situation by showing an error message or similar.
            }
        }
    }
}
