package com.example.medocare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private TextView mTextView, logoutstat;
    private FirebaseAuth mAuth;
    //String logoutstatus;
    CheckBox remember;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        remember = findViewById(R.id.checkBoxRemember);
        logoutstat =findViewById(R.id.textView);

        logoutstat.setText("false");
        /*String logoutstatus = "false";
        logoutstat.setText("false");*/
        /*Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getString("logoutstatus") == "true") {

            logoutstat.setText(extras.getString("logoutstatus"));
            remember.setChecked(false);
        }*/

        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        mTextView = findViewById(R.id.signupTextView);

        mAuth = FirebaseAuth.getInstance();
        signinButton = (Button)findViewById(R.id.buttonLogin);

        //String finalLogoutstatus = logoutstat.getText().toString();





        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.putString("email",email.getText().toString());
                    editor.putString("pass",pass.getText().toString());
                    editor.apply();
                    Toast.makeText(SignInActivity.this, "Checked",Toast.LENGTH_SHORT).show();

                }else if(!compoundButton.isChecked()){

                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(SignInActivity.this, "Un-Checked",Toast.LENGTH_SHORT).show();

                }
            }
        });




        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox;

        checkbox = preferences.getString("remember","");
        String useremail = preferences.getString("email","");
        String userpass = preferences.getString("pass","");
        if(checkbox.equals("true") /*&& finalLogoutstatus == "false"*/){

            mAuth.signInWithEmailAndPassword(useremail,userpass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //Toast.makeText(SignInActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, Dashboard.class);
                            intent.putExtra("user", useremail);
                            startActivity(intent);
                            //startActivity(new Intent(SignInActivity.this, Dashboard.class));
                            //finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
            //rememberlogin(useremail,userpass);
            /*Intent intent = new Intent(SignInActivity.this, Dashboard.class);
            intent.putExtra("user",email.getText().toString());
            startActivity(intent);*/
        }else if(checkbox.equals("false" )/*|| finalLogoutstatus == "true"*/){
            Toast.makeText(this,"Sign in",Toast.LENGTH_SHORT).show();
        }



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





    private void rememberlogin(String uemail,String upass){
        //String emailS = uemail;
        //String passS = upass;
        //if(!emailS.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailS).matches() && !passS.isEmpty()){
            mAuth.signInWithEmailAndPassword(uemail, upass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //Toast.makeText(SignInActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this, Dashboard.class);
                            intent.putExtra("user", uemail);
                            startActivity(intent);
                            //startActivity(new Intent(SignInActivity.this, Dashboard.class));
                            //finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(SignInActivity.this, Dashboard.class);
                            intent.putExtra("user", emailS);
                            startActivity(intent);
                            //startActivity(new Intent(SignInActivity.this, Dashboard.class));
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