package com.example.medocare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
//import android.support.annotation.Nullable;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.Random;
public class UploadPrescription extends AppCompatActivity {

    DatabaseReference databaseReference;
    // views for button
    private Button btnSelect, btnUpload, btnClick, btnView;
    private EditText doctorName;
    // view for image view
    private ImageView imageView;
    private static final String FRAGMENT_NAME = "imageFragment";
    ImageRetainingFragment imageRetainingFragment;



    // Uri indicates, where the image will be picked from
    private Uri imageUri;

    // request code
    public static final int CAMERA_PERM_CODE = 101;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int CAMERA_REQUEST_CODE =25;
    private Bitmap image;
    private final Integer CAMIMAGE=123;
    String currentPhotoPath;
    String email;

    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);





//        ActionBar actionBar;
//        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(
//                Color.parseColor("#0F9D58"));
//        actionBar.setBackgroundDrawable(colorDrawable);

        // initialise views
        databaseReference = FirebaseDatabase.getInstance().getReference();
        btnSelect = findViewById(R.id.choosebutton);
        btnUpload = findViewById(R.id.uploadbutton);
        imageView = findViewById(R.id.imgView);
        btnClick = findViewById(R.id.clickphoto);
        btnView = findViewById(R.id.viewbutton);
        doctorName = findViewById(R.id.doctor_name);
        // get the Firebase  storage reference
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

//        if(savedInstanceState != null) {
//            Bitmap bitmap = savedInstanceState.getParcelable("image");
//            imageView.setImageBitmap(bitmap);
//        }
        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        btnClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                askCameraPermissions();
//                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(camera, 0);

//                clickImage();
            }
        });
        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String str=doctorName.getText().toString();
                String Tag="hello";
                Log.i(Tag,str);
                if(!str.isEmpty()) {
//             uploadImage();
                    upload();
                }
                else
                    Toast.makeText(UploadPrescription.this,"Kindly enter doctor name",Toast.LENGTH_SHORT).show();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ImageActivity.class));
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putInt("questionId",mCurrentIndex);
//        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
//        savedInstanceState.putParcelable("image", bitmap);
        super.onSaveInstanceState(savedInstanceState);
    }

    //    not used
    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
//            dispatchTakePictureIntent();
            clickImage();
        }

    }

    //    not used
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                dispatchTakePictureIntent();
                clickImage();
            }else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if(requestCode == PICK_IMAGE_REQUEST   && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            imageUri = data.getData();
            try {

                // Setting image on image view using Bitmap
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//                imageView.setImageBitmap(bitmap);
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(image);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

        if (requestCode == 0 && resultCode == RESULT_OK) {

            image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
        }
    }


    // to check whether camera is present or not, not used
    private boolean hasCamera()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //    not used
    private void clickImage(){

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 0);

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//
//                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//        }


    }

    // UploadImage method, not used
    private void uploadImage()
    {
        if (imageUri != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            Intent i =new Intent();
            email = i.getStringExtra("useremail");

            FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
            String filename = doctorName.getText().toString();
            StorageReference ref = storageReference.child( user.getUid()+"/"+filename+"/"+"Prescription.jpg");
            // adding listeners on upload
            // or failure of image
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(
                        UploadTask.TaskSnapshot taskSnapshot)
                {

                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();
                    Toast.makeText(UploadPrescription.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {

                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast.makeText(UploadPrescription.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(
                                UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });

        }
    }


    private void upload() {
        final ProgressBar p = findViewById(R.id.progressbar);

        p.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyyHH:mm");
        LocalDateTime now = LocalDateTime.now();
        String datetime = dtf.format(now);
        String filename = doctorName.getText().toString();
        filename=filename.toLowerCase();
        StorageReference ref = storageReference.child( user.getUid()+"/"+filename+"/"+"Prescription.jpg");

        byte[] b = stream.toByteArray();
        ref.putBytes(b)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        p.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri = uri;
                            }
                        });

                        Toast.makeText(UploadPrescription.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
                        doctorName.setText("");
                        imageView.setImageResource(android.R.color.transparent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        p.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        Toast.makeText(UploadPrescription.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void initializeImageRetainingFragment() {
        // find the retained fragment on activity restarts
        FragmentManager fragmentManager = getSupportFragmentManager();
        this.imageRetainingFragment = (ImageRetainingFragment) fragmentManager.findFragmentByTag(FRAGMENT_NAME);
        // create the fragment and bitmap the first time
        if (this.imageRetainingFragment == null) {
            this.imageRetainingFragment = new ImageRetainingFragment();
            fragmentManager.beginTransaction()
                    // Add a fragment to the activity state.
                    .add(this.imageRetainingFragment, FRAGMENT_NAME)
                    .commit();
        }
    }

    private void tryLoadImage() {
        if (this.imageRetainingFragment == null) {
            return;
        }
        Bitmap selectedImage = this.imageRetainingFragment.getImage();
        if (selectedImage == null) {
            return;
        }
        ImageView selectedImageView = (ImageView) findViewById(R.id.imgView);
        selectedImageView.setImageBitmap(selectedImage);
    }

}