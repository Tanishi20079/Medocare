package com.example.medocare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    TextView username;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        username = findViewById(R.id.dashboardTextView);
        mAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        username.setText(extras.getString("user"));
        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //final FirebaseUser user = mAuth.getCurrentUser();
                mAuth.signOut();
                Intent intent = new Intent(Dashboard.this, SignInActivity.class);
                intent.putExtra("logoutstatus","true");
                startActivity(intent);
                finish();
                //startActivity(intent);
            }
        });


    }
}