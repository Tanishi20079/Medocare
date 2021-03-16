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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText name, email, pass1, pass2;
    private TextView mTextView;
    private Button signupButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.namereg);
        email = findViewById(R.id.emailreg);
        pass1 = findViewById(R.id.passreg1);
        pass2 = findViewById(R.id.passreg2);
        mTextView = findViewById(R.id.loginTextView);
        signupButton = (Button)findViewById(R.id.buttonreg);
        mAuth = FirebaseAuth.getInstance();

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                //intent.putExtra("logoutstatus","false");
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

    }
    private void createUser(){
        String nameS = name.getText().toString();
        String emailS = email.getText().toString();
        String pass1S = pass1.getText().toString();
        String pass2S = pass2.getText().toString();

        if(!nameS.isEmpty() && !emailS.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailS).matches()){
            if(!pass1S.isEmpty() || !pass2S.isEmpty()){
                    if(pass1S.equals(pass2S)){
                        mAuth.createUserWithEmailAndPassword(emailS,pass1S)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        User user = new User(nameS, emailS);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(MainActivity.this, "Name and Email is Saved", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        Toast.makeText(MainActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(MainActivity.this, "Password Unmatched", Toast.LENGTH_SHORT).show();
                    }
            }else{
                pass1.setError("Empty Fields are not Allowed");
                pass2.setError("Empty Fields are not Allowed");
            }

        }else if(emailS.isEmpty()){
            email.setError("Please fill up Email");
        }else{
            email.setError("Please enter Correct Email");
        }
    }
}