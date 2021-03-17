package com.example.medocare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private EditText email, pass;
    private Button signinButton;
    private TextView mTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        mTextView = findViewById(R.id.signupTextView);
        mAuth = FirebaseAuth.getInstance();
        signinButton = (Button)findViewById(R.id.buttonLogin);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    loginUser();
            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }
        });

    }

    private void loginUser(){
        String emailS = email.getText().toString();
        String passS = pass.getText().toString();
        if(!emailS.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailS).matches() && !passS.isEmpty()){
            mAuth.signInWithEmailAndPassword(emailS, passS)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(SignInActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignInActivity.this, NavigationActivity.class);
//                            startActivity(new Intent(SignInActivity.this, Dashboard.class));
                            i.putExtra("email",emailS);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}