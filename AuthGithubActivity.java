package com.fireauth.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fireauth.auth.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import java.util.ArrayList;

public class AuthGithubActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button buttonGitHub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_yahoo);
        //github account email required to go to the github account
        //
        firebaseAuth = FirebaseAuth.getInstance();
        buttonGitHub = findViewById(R.id.button_oauth);
        buttonGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithYahooAuthProvider(OAuthProvider.newBuilder("github.com")
                        .addCustomParameter("login", "EditText.getText()")
                        .setScopes(
                                new ArrayList<String>(){
                                    {
                                        //permission for user email
                                        add("user:email");
                                    }
                                }
                        ).build()

                );
            }
        });
    }

    private void signInWithYahooAuthProvider(OAuthProvider provider) {
        Task<AuthResult> taskAuthResult = firebaseAuth.getPendingAuthResult();
        if (taskAuthResult != null){
            taskAuthResult.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error:\n"+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            firebaseAuth.startActivityForSignInWithProvider(this, provider).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser fu = firebaseAuth.getCurrentUser();
                    if (fu != null) {
                        Toast.makeText(getApplicationContext(), "Success Alert:\n" + fu.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Success Alert, email not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error Alert:\n"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
