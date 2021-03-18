package com.example.medocare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class ImageActivity extends AppCompatActivity {
    private Button searchButton, clearButton, viewAllButton;
    private ImageView imageview;
    private EditText name;
    private StorageReference storageReference;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        searchButton = findViewById(R.id.search_button);
        clearButton = findViewById(R.id.clear_button);
        viewAllButton = findViewById(R.id.view_all_button);
        imageview = findViewById(R.id.image);
        name = findViewById(R.id.editTextName);
        storageReference = FirebaseStorage.getInstance().getReference();

        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImageActivity.this,"View all Prescriptions",Toast.LENGTH_SHORT).show();
            }
        });



        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                imageview.setImageResource(android.R.color.transparent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String doctor_name = name.getText().toString();
//                 if(doctor_name.contains("Dr."))
                doctor_name = doctor_name.toLowerCase();
//                 doctor_name = "Dr. "+doctor_name;
                user  = FirebaseAuth.getInstance().getCurrentUser();


                StorageReference ref = storageReference.child( user.getUid()+"/"+doctor_name+"/"+"Prescription.jpg");
                final long ONE_MEGABYTE = 1024 * 1024;
                ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);

                        imageview.setMinimumHeight(dm.heightPixels);
                        imageview.setMinimumWidth(dm.widthPixels);
                        imageview.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "No Prescription Found", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}